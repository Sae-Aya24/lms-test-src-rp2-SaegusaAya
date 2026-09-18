package jp.co.sss.lms.controller;

import java.text.ParseException;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.form.HolidayForm;
import jp.co.sss.lms.service.HolidayService;

/**
 * Task.86 休日情報コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/holiday")
public class HolidayController {

	@Autowired
	private HolidayService holidayService;

	/**
	 * Task.86 休暇日一覧画面 初期表示
	 * 
	 * @param holidayForm
	 * @return 休暇日一覧画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list")
	public String list(HolidayForm holidayForm) throws ParseException {
		holidayService.setHolidayForm(holidayForm);
		return "holiday/list";
	}

	/**
	 * Task.86 休暇日一覧画面 『検索』ボタン押下
	 * 
	 * @param holidayForm
	 * @param result
	 * @return 休暇日一覧画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list", params = "search", method = RequestMethod.POST)
	public String search(@Valid HolidayForm holidayForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "holiday/list";
		}
		redirectAttributes.addFlashAttribute("holidayForm", holidayForm);
		return "redirect:/holiday/list";
	}

	/**
	 * Task.86 休暇日一覧画面 『決定』ボタン押下
	 * 
	 * @param holidayForm
	 * @param result
	 * @param redirectAttributes
	 * @return 休暇日一覧画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list", params = "regist", method = RequestMethod.POST)
	public String regist(HolidayForm holidayForm, BindingResult result,
			RedirectAttributes redirectAttributes) throws ParseException {

		holidayService.registInputCheck(holidayForm, result);
		if (result.hasErrors()) {
			return "holiday/list";
		}

		String message = holidayService.regist(holidayForm);
		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("holidayForm", holidayForm);
		return "redirect:/holiday/list";
	}

}
