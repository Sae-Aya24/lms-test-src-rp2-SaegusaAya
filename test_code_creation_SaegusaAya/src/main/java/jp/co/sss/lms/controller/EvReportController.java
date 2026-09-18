package jp.co.sss.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.dto.EvReportDto;
import jp.co.sss.lms.form.EvReportForm;
import jp.co.sss.lms.service.EvReportService;

/**
 * 評価レポートコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/evReport")
public class EvReportController {

	@Autowired
	private EvReportService evReportService;

	/**
	 * Task.103 評価レポート一覧画面 初期表示
	 * 
	 * @param evReportForm
	 * @param model
	 * @return 評価レポート一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(EvReportForm evReportForm, Model model) {

		List<EvReportDto> evReportDtoList = evReportService
				.getEvReportDtoList(evReportForm.getReportName());
		model.addAttribute("evReportDtoList", evReportDtoList);

		return "evReport/list";
	}

	/**
	 * Task.103 評価レポート一覧画面 『コピー』ボタン押下
	 * 
	 * @param evReportForm
	 * @param redirectAttributes
	 * @return 評価レポート一覧画面
	 */
	@RequestMapping(path = "/list", params = "copy", method = RequestMethod.POST)
	public String copy(EvReportForm evReportForm, RedirectAttributes redirectAttributes) {

		String message = evReportService.copy(evReportForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/evReport/list";
	}

	/**
	 * Task.103 評価レポート一覧画面 『削除』ボタン押下
	 * 
	 * @param evReportForm
	 * @param redirectAttributes
	 * @return 評価レポート一覧画面
	 */
	@RequestMapping(path = "/list", params = "delete", method = RequestMethod.POST)
	public String delete(EvReportForm evReportForm, RedirectAttributes redirectAttributes) {

		String message = evReportService.delete(evReportForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/evReport/list";
	}

}
