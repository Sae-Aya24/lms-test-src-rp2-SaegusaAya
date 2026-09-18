package jp.co.sss.lms.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.form.CompanyListForm;
import jp.co.sss.lms.form.CompanyRegistForm;
import jp.co.sss.lms.service.CompanyService;

/**
 * 企業情報コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/company")
public class CompanyController {

	@Autowired
	private CompanyService companyService;

	/**
	 * Task.104 企業一覧画面 初期表示
	 * 
	 * @param companyForm
	 * @param result
	 * @return 企業一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(CompanyListForm companyListForm) {
		companyService.setCompanyListForm(companyListForm);
		return "company/list";
	}

	/**
	 * Task.104 企業一覧画面 『検索』ボタン押下
	 * 
	 * @param companyForm
	 * @param result
	 * @param redirectAttributes
	 * @return 企業一覧画面
	 */
	@RequestMapping(path = "/list", params = "search")
	public String search(CompanyListForm companyListForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		companyService.searchCheck(companyListForm, result);
		if (result.hasErrors()) {
			companyService.setCompanyListForm(companyListForm);
			return "company/list";
		}
		redirectAttributes.addFlashAttribute("companyListForm", companyListForm);
		return "redirect:/company/list";
	}

	/**
	 * Task.104 企業一覧画面 『削除』ボタン押下
	 * 
	 * @param companyListForm
	 * @param result
	 * @param redirectAttributes
	 * @return 企業一覧画面
	 */
	@RequestMapping(path = "/list", params = "delete", method = RequestMethod.POST)
	public String delete(CompanyListForm companyListForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		companyService.checkCompanyDelete(companyListForm, result);
		if (result.hasErrors()) {
			companyService.setCompanyListForm(companyListForm);
			return "company/list";
		}
		String message = companyService.delete(companyListForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/company/list";
	}

	/**
	 * Task.105 企業一覧画面 『新規登録』または『変更』ボタン押下
	 * 
	 * @param companyRegistForm
	 * @return 企業登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(CompanyRegistForm companyRegistForm) {
		companyService.setCompanyRegistForm(companyRegistForm);
		return "company/regist";
	}

	/**
	 * Task.105 企業登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param companyRegistForm
	 * @param result
	 * @param redirectAttributes
	 * @return 企業一覧画面
	 */
	@RequestMapping(path = "/regist", params = "complete", method = RequestMethod.POST)
	public String registComplete(@Valid CompanyRegistForm companyRegistForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "company/regist";
		}

		String message = companyService.registComplete(companyRegistForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/company/list";
	}

}
