package jp.co.sss.lms.controller;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import jakarta.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.dto.DailyReportDto;
import jp.co.sss.lms.dto.UserDailyReportDto;
import jp.co.sss.lms.form.DailyReportDetailForm;
import jp.co.sss.lms.form.DailyReportDownloadForm;
import jp.co.sss.lms.form.DailyReportSearchForm;
import jp.co.sss.lms.form.DailyReportSubmitForm;
import jp.co.sss.lms.service.ReportService;

/**
 * レポートコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/report")
public class ReportController {

	@Autowired
	private ReportService reportService;

	/**
	 * レポート登録画面 初期表示
	 * 
	 * @param dailyReportSubmitForm
	 * @return レポート登録画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String submit(@ModelAttribute DailyReportSubmitForm dailyReportSubmitForm)
			throws ParseException {
		reportService.getDailyReport(dailyReportSubmitForm);
		return "report/regist";
	}

	/**
	 * レポート登録画面 『提出する』ボタン押下
	 * 
	 * @param dailyReportSubmitForm
	 * @return セクション詳細画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/complete", method = RequestMethod.POST)
	public String complete(DailyReportSubmitForm dailyReportSubmitForm, BindingResult result)
			throws ParseException {

		// Task.20 ①入力チェックを実装する
		reportService.reportSubmitInputCheck(dailyReportSubmitForm, result);
		if (result.hasErrors()) {
			return "report/regist";
		}

		// レポート登録/更新
		reportService.submit(dailyReportSubmitForm);

		// Task.21 セクションIDがNULLの場合、ユーザー詳細画面へ遷移
		if (dailyReportSubmitForm.getSectionId() == null) {
			return "redirect:/user/detail";
		}
		return "redirect:/section/detail?sectionId=" + dailyReportSubmitForm.getSectionId();
	}

	/**
	 * ユーザー詳細画面 『ダウンロード（レポート）』ボタン押下
	 * 
	 * @param reportDownloadForm
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(path = "/download", method = RequestMethod.POST)
	@ResponseBody
	public void download(DailyReportDownloadForm dailyReportDownloadForm,
			HttpServletResponse response) throws IOException {
		reportService.download(dailyReportDownloadForm.getDailyReportId(),
				dailyReportDownloadForm.getDailyReportSubmitId(), response);
	}

	/**
	 * Task.22 レポート詳細画面 初期表示
	 * 
	 * @param dailyReportSubmitId
	 * @param model
	 * @return レポート詳細画面
	 */
	@RequestMapping(path = "/detail")
	public String detail(@ModelAttribute("dailyReportSubmitId") Integer dailyReportSubmitId,
			Model model) {
		DailyReportDto dailyReportDto = reportService.getDailyReportDto(dailyReportSubmitId);
		model.addAttribute("dailyReportDto", dailyReportDto);
		return "report/detail";
	}

	/**
	 * Task.22 レポート詳細画面 『フィードバックコメントを送信する』ボタン押下
	 * 
	 * @param dailyReportDetailForm
	 * @param model
	 * @return レポート詳細画面
	 */
	@RequestMapping(path = "/feedback/regist", method = RequestMethod.POST)
	public String feedbackRegist(DailyReportDetailForm dailyReportDetailForm,
			RedirectAttributes redirectAttributes) {
		reportService.feedbackRegist(dailyReportDetailForm);
		redirectAttributes.addFlashAttribute("dailyReportSubmitId",
				dailyReportDetailForm.getDailyReportSubmitId());
		return "redirect:/report/detail";
	}

	/**
	 * Task.22 レポート詳細画面 『削除』リンク押下
	 * 
	 * @param dailyReportDetailForm
	 * @param redirectAttributes
	 * @return レポート詳細画面
	 */
	@RequestMapping(path = "/feedback/delete", method = RequestMethod.POST)
	public String feedbackDelete(DailyReportDetailForm dailyReportDetailForm,
			RedirectAttributes redirectAttributes) {
		reportService.feedbackDelete(dailyReportDetailForm);
		redirectAttributes.addFlashAttribute("dailyReportSubmitId",
				dailyReportDetailForm.getDailyReportSubmitId());
		return "redirect:/report/detail";
	}

	/**
	 * Task.22 レポート詳細画面 『編集』リンク押下
	 * 
	 * @param dailyReportDetailForm
	 * @param redirectAttributes
	 * @return レポート詳細画面
	 */
	@RequestMapping(path = "/feedback/update", method = RequestMethod.POST)
	public String feedbackUpdate(DailyReportDetailForm dailyReportDetailForm,
			RedirectAttributes redirectAttributes) {
		reportService.feedbackUpdate(dailyReportDetailForm);
		redirectAttributes.addFlashAttribute("dailyReportSubmitId",
				dailyReportDetailForm.getDailyReportSubmitId());
		return "redirect:/report/detail";
	}

	/**
	 * Task.50 レポート一覧画面 初期表示
	 * 
	 * @param dailyReportSearchForm
	 * @return レポート一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(@ModelAttribute DailyReportSearchForm dailyReportSearchForm) {

		reportService.setDailyReportSearchForm(dailyReportSearchForm);
		reportService.setToday(dailyReportSearchForm);

		return "report/list";
	}

	/**
	 * Task.50 レポート一覧画面 『検索』ボタン押下
	 * 
	 * @param dailyReportSearchForm
	 * @param result
	 * @param model
	 * @return レポート一覧画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list", name = "search", method = RequestMethod.POST)
	public String search(@Validated DailyReportSearchForm dailyReportSearchForm,
			BindingResult result, Model model) throws ParseException {
		reportService.setDailyReportSearchForm(dailyReportSearchForm);

		reportService.searchReportInputCheck(dailyReportSearchForm, result);
		if (result.hasErrors()) {
			return "report/list";
		}

		List<UserDailyReportDto> userDailyReportDtoList = reportService
				.getUserDailyReportDtoList(dailyReportSearchForm);
		model.addAttribute("userDailyReportDtoList", userDailyReportDtoList);

		return "report/list";
	}

	/**
	 * Task.50 レポート一覧画面 『チェックしたレポートをダウンロード』ボタン押下
	 * 
	 * @param dailyReportDownloadForm
	 * @param response
	 * @param model
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	@RequestMapping(path = "/downloadList", method = RequestMethod.POST)
	@ResponseBody
	public void downloadList(DailyReportDownloadForm dailyReportDownloadForm,
			HttpServletResponse response, Model model) throws NumberFormatException, IOException {
		reportService.downloadZip(dailyReportDownloadForm, response);
	}

}
