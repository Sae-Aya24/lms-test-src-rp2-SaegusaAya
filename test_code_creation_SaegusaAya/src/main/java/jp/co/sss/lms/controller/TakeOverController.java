package jp.co.sss.lms.controller;

import java.text.ParseException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.validation.Valid;
import jp.co.sss.lms.dto.TakeOverScheduleDto;
import jp.co.sss.lms.form.TakeOverCandidateListForm;
import jp.co.sss.lms.form.TakeOverScheduleRegistForm;
import jp.co.sss.lms.form.TakeOverScheduleReserveForm;
import jp.co.sss.lms.form.TakeOverSelectCompanyForm;
import jp.co.sss.lms.form.TakeOverSelectDateForm;
import jp.co.sss.lms.service.TakeOverService;

/**
 * 引継面談／会場見学コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/takeOver")
public class TakeOverController {

	@Autowired
	private TakeOverService takeOverService;

	/**
	 * Task.59 引継面談／会場見学 スケジュール一覧画面 初期表示
	 * 
	 * @param model
	 * @return 引継面談／会場見学 スケジュール一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(Model model) {
		List<TakeOverScheduleDto> takeOverScheduleDtoList = takeOverService
				.getTakeOverScheduleDtoList();
		model.addAttribute("takeOverScheduleDtoList", takeOverScheduleDtoList);
		return "takeOver/list";
	}

	/**
	 * Task.73 引継面談／会場見学 時間予約（一覧）画面 初期表示
	 * 
	 * @param takeOverScheduleReserveForm
	 * @return 引継面談／会場見学 時間予約（一覧）画面
	 */
	@RequestMapping(path = "/detail", method = RequestMethod.POST)
	public String detail(@ModelAttribute TakeOverScheduleReserveForm takeOverScheduleReserveForm) {

		takeOverService.setTakeOverScheduleDetailDtoList(takeOverScheduleReserveForm);
		return "takeOver/detail";
	}

	/**
	 * Task.74 引継面談／会場見学 時間予約（登録・変更・削除）画面 初期表示
	 * 
	 * @param takeOverScheduleReserveForm
	 * @return 引継面談／会場見学 時間予約（登録・変更・削除）画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(@ModelAttribute TakeOverScheduleReserveForm takeOverScheduleReserveForm) {
		return "takeOver/regist";
	}

	/**
	 * Task.74 引継面談／会場見学 時間予約（登録・変更・削除）画面 『予約する』『予約を変更する』ボタン押下
	 * 
	 * @param takeOverScheduleReserveForm
	 * @param result
	 * @param redirectAttributes
	 * @return 引継面談／会場見学 スケジュール一覧画面
	 */
	@RequestMapping(path = "/regist", params = "update", method = RequestMethod.POST)
	public String registUpdate(
			@ModelAttribute TakeOverScheduleReserveForm takeOverScheduleReserveForm,
			BindingResult result, RedirectAttributes redirectAttributes) {
		takeOverService.takeOverScheduleReserveFormInputCheck(takeOverScheduleReserveForm, result);
		if (result.hasErrors()) {
			return "takeOver/regist";
		}
		String message = takeOverService
				.updateTakeOverScheduleDetailDto(takeOverScheduleReserveForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/takeOver/list";
	}

	/**
	 * Task.74 引継面談／会場見学 時間予約（登録・変更・削除）画面 『予約を取り消す』ボタン押下
	 * 
	 * @param takeOverScheduleReserveForm
	 * @param redirectAttributes
	 * @return 引継面談／会場見学 スケジュール一覧画面
	 */
	@RequestMapping(path = "/regist", params = "delete", method = RequestMethod.POST)
	public String registDelete(TakeOverScheduleReserveForm takeOverScheduleReserveForm,
			RedirectAttributes redirectAttributes) {
		String message = takeOverService
				.deleteTakeOverScheduleDetailDto(takeOverScheduleReserveForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/takeOver/list";
	}

	/**
	 * Task.108 引継面談／会場見学 実施日一覧画面 初期表示
	 * 
	 * @param takeOverCandidateListForm
	 * @return 引継面談／会場見学 実施日一覧画面
	 */
	@RequestMapping(path = "/candidateList")
	public String candidateList(
			@ModelAttribute TakeOverCandidateListForm takeOverCandidateListForm) {
		takeOverService.getTakeOverScheduleDtoListAll(takeOverCandidateListForm);
		return "takeOver/candidateList";
	}

	/**
	 * Task.108 引継面談／会場見学 実施日一覧画面 『削除』ボタン押下
	 * 
	 * @param takeOverCandidateListForm
	 * @param redirectAttributes
	 * @return 引継面談／会場見学 実施日一覧画面
	 */
	@RequestMapping(path = "/candidateList", params = "delete", method = RequestMethod.POST)
	public String delete(TakeOverCandidateListForm takeOverCandidateListForm,
			RedirectAttributes redirectAttributes) {
		takeOverService.deleteSchedule(takeOverCandidateListForm);
		redirectAttributes.addFlashAttribute("takeOverCandidateListForm",
				takeOverCandidateListForm);
		return "redirect:/takeOver/candidateList";
	}

	/**
	 * Task.108 引継面談／会場見学 実施日一覧画面 『公開』ボタン押下
	 * 
	 * @param takeOverCandidateListForm
	 * @param redirectAttributes
	 * @return 引継面談／会場見学 実施日一覧画面
	 */
	@RequestMapping(path = "/candidateList", params = "publish", method = RequestMethod.POST)
	public String publish(TakeOverCandidateListForm takeOverCandidateListForm,
			RedirectAttributes redirectAttributes) {
		takeOverService.publishSchedule(takeOverCandidateListForm);
		redirectAttributes.addFlashAttribute("takeOverCandidateListForm",
				takeOverCandidateListForm);
		return "redirect:/takeOver/candidateList";
	}

	/**
	 * Task.108 引継面談／会場見学 実施日一覧画面 『取消』ボタン押下
	 * 
	 * @param takeOverCandidateListForm
	 * @param redirectAttributes
	 * @return 引継面談／会場見学 実施日一覧画面
	 */
	@RequestMapping(path = "/candidateList", params = "cancel", method = RequestMethod.POST)
	public String cancel(TakeOverCandidateListForm takeOverCandidateListForm,
			RedirectAttributes redirectAttributes) {
		takeOverService.cancelPublish(takeOverCandidateListForm);
		redirectAttributes.addFlashAttribute("takeOverCandidateListForm",
				takeOverCandidateListForm);
		return "redirect:/takeOver/candidateList";
	}

	/**
	 * Task.109 引継面談／会場見学 実施日登録（実施日時登録）画面 初期表示
	 * 
	 * @param takeOverScheduleRegistForm
	 * @return 引継面談／会場見学 実施日登録（実施日時登録）画面
	 */
	@RequestMapping(path = "/registDate")
	public String registDate(
			@ModelAttribute TakeOverScheduleRegistForm takeOverScheduleRegistForm) {
		return "takeOver/registDate";
	}

	/**
	 * Task.108 引継面談／会場見学 実施日一覧画面 『編集』ボタン押下 <br />
	 * Task.109 引継面談／会場見学 実施日登録（実施日時登録）画面 『対象企業を選択する』ボタン押下
	 * 
	 * @param takeOverScheduleRegistForm
	 * @param result
	 * @return 引継面談／会場見学 実施日登録（会場／企業選択）画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/registDate", method = RequestMethod.POST)
	public String registDatePost(@Valid TakeOverScheduleRegistForm takeOverScheduleRegistForm,
			BindingResult result) throws ParseException {
		// Task.108
		takeOverService.takeOverScheduleRegistFormInputCheck(takeOverScheduleRegistForm, result);
		if (result.hasErrors()) {
			return "takeOver/registDate";
		}
		// Task.109
		int meetingScheduleId = takeOverService.registSchedule(takeOverScheduleRegistForm);
		return "redirect:/takeOver/selectCompany?meetingScheduleId=" + meetingScheduleId;
	}

	/**
	 * Task.110 引継面談／会場見学 実施日登録（会場／企業選択）画面 初期表示
	 * 
	 * @param takeOverCandidateListForm
	 * @param redirectAttributes
	 * @param takeOverSelectCompanyForm
	 * @return 引継面談／会場見学 実施日登録（会場／企業選択）画面
	 */
	@RequestMapping(path = "/selectCompany")
	public String selectCompany(TakeOverCandidateListForm takeOverCandidateListForm,
			RedirectAttributes redirectAttributes,
			@ModelAttribute TakeOverSelectCompanyForm takeOverSelectCompanyForm) {
		Boolean result = takeOverService.beforeSelectCompanyInputCheck(takeOverCandidateListForm);
		if (result) {
			redirectAttributes.addFlashAttribute("takeOverCandidateListForm",
					takeOverCandidateListForm);
			return "redirect:/takeOver/candidateList";
		}

		takeOverService.setSelectCompanyForm(takeOverSelectCompanyForm);
		return "takeOver/selectCompany";
	}

	/**
	 * Task.110 引継面談／会場見学 実施日登録（会場／企業選択）画面 『選択した企業を対象とする』ボタン押下
	 * 
	 * @param takeOverSelectCompanyForm
	 * @param result
	 * @return 引継面談／会場見学 実施日登録（日程登録）画面
	 */
	@RequestMapping(path = "/selectCompany", method = RequestMethod.POST)
	public String selectCompanyPost(TakeOverSelectCompanyForm takeOverSelectCompanyForm,
			BindingResult result) {
		takeOverService.selectCompanyFormInputCheck(takeOverSelectCompanyForm, result);
		if (result.hasErrors()) {
			takeOverService.setSelectCompanyForm(takeOverSelectCompanyForm);
			return "takeOver/selectCompany";
		}
		takeOverService.registPlaceAndCompany(takeOverSelectCompanyForm);
		return "redirect:/takeOver/selectDate?meetingScheduleId="
				+ takeOverSelectCompanyForm.getMeetingScheduleId();
	}

	/**
	 * Task.111 引継面談／会場見学 実施日登録（日程登録）画面 初期表示
	 * 
	 * @param takeOverSelectDateForm
	 * @return 引継面談／会場見学 実施日登録（日程登録）画面
	 */
	@RequestMapping(path = "/selectDate")
	public String selectDate(@ModelAttribute TakeOverSelectDateForm takeOverSelectDateForm) {
		takeOverService.setSelectDateForm(takeOverSelectDateForm);
		return "takeOver/selectDate";
	}

	/**
	 * Task.111 引継面談／会場見学 実施日登録（日程登録）画面 『スケジュールを登録する』ボタン押下
	 * 
	 * @param takeOverSelectDateForm
	 * @param result
	 * @param redirectAttributes
	 * @return 引継面談／会場見学 実施日一覧画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/selectDate", method = RequestMethod.POST)
	public String selectDatePost(TakeOverSelectDateForm takeOverSelectDateForm,
			BindingResult result, RedirectAttributes redirectAttributes) throws ParseException {
		takeOverService.selectDateFormInputCheck(takeOverSelectDateForm, result);
		if (result.hasErrors()) {
			return "takeOver/selectDate";
		}
		TakeOverCandidateListForm takeOverCandidateListForm = takeOverService
				.selectDate(takeOverSelectDateForm);
		redirectAttributes.addFlashAttribute("takeOverCandidateListForm",
				takeOverCandidateListForm);
		return "redirect:/takeOver/candidateList";
	}

}
