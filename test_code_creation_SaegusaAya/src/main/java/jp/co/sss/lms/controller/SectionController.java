package jp.co.sss.lms.controller;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.lms.form.SectionDetailForm;
import jp.co.sss.lms.service.SectionService;

/**
 * セクションコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/section")
public class SectionController {

	@Autowired
	private SectionService sectionService;

	/**
	 * セクション詳細画面 初期表示
	 * 
	 * @param sectionId セクションID
	 * @param model     モデル
	 * @return セクション詳細画面
	 * @throws ParseException
	 */
	@RequestMapping("/detail")
	public String detail(@ModelAttribute SectionDetailForm sectionDetailForm)
			throws ParseException {
		// パラメータチェック
		String message = sectionService.checkSectionId(sectionDetailForm.getSectionId());
		if (!message.isEmpty()) {
			return "illegal";
		}
		// セクション詳細情報の取得
		sectionService.getSectionDetail(sectionDetailForm);
		return "section/detail";
	}

	/**
	 * Task.116 セクション詳細画面 セクション詳細を編集エリア『変更する』ボタン押下
	 * 
	 * @param sectionDetailForm
	 * @param result
	 * @return セクション詳細画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/registDetail", params = "updateDetail", method = RequestMethod.POST)
	public String updateDetail(SectionDetailForm sectionDetailForm, BindingResult result)
			throws ParseException {
		sectionService.sectionDescriptionInputCheck(sectionDetailForm, result);
		if (!result.hasErrors()) {
			sectionService.sectionDescriptionUpdate(sectionDetailForm);
		}
		sectionService.getSectionDetail(sectionDetailForm);
		return "section/detail";
	}

	/**
	 * Task.116 セクション詳細画面 試験を追加するエリア『登録する』ボタン押下
	 * 
	 * @param sectionDetailForm
	 * @param result
	 * @return セクション詳細画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/registDetail", params = "addExam", method = RequestMethod.POST)
	public String addExam(SectionDetailForm sectionDetailForm, BindingResult result)
			throws ParseException {
		sectionService.sectionExamInputCheck(sectionDetailForm, result);
		if (!result.hasErrors()) {
			sectionService.sectionExamRegist(sectionDetailForm);
		}
		sectionService.getSectionDetail(sectionDetailForm);
		return "section/detail";
	}

	/**
	 * Task.116 セクション詳細画面 本日の試験エリア『削除』ボタン押下
	 * 
	 * @param sectionDetailForm
	 * @return セクション詳細画面
	 */
	@RequestMapping(path = "/registDetail", params = "delExam", method = RequestMethod.POST)
	public String delExam(SectionDetailForm sectionDetailForm) {
		sectionService.sectionExamDelete(sectionDetailForm);
		sectionService.getSectionDetail(sectionDetailForm);
		return "section/detail";
	}

	/**
	 * Task.116 セクション詳細画面 レポートを追加するエリア『登録する』ボタン押下
	 * 
	 * @param sectionDetailForm
	 * @param result
	 * @return セクション詳細画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/registDetail", params = "addReport", method = RequestMethod.POST)
	public String addReport(SectionDetailForm sectionDetailForm, BindingResult result)
			throws ParseException {
		sectionService.sectionReportInputCheck(sectionDetailForm, result);
		if (!result.hasErrors()) {
			sectionService.sectionReportRegist(sectionDetailForm);
		}
		sectionService.getSectionDetail(sectionDetailForm);
		return "section/detail";
	}

	/**
	 * Task.116 セクション詳細画面 本日のレポートエリア『削除』ボタン押下
	 * 
	 * @param sectionDetailForm
	 * @return セクション詳細画面
	 */
	@RequestMapping(path = "/registDetail", params = "delReport", method = RequestMethod.POST)
	public String delReport(SectionDetailForm sectionDetailForm) {
		sectionService.sectionReportDelete(sectionDetailForm);
		sectionService.getSectionDetail(sectionDetailForm);
		return "section/detail";
	}

}
