package jp.co.sss.lms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.lms.form.CompanySubsidyForm;
import jp.co.sss.lms.service.SubsidyService;

/**
 * 助成金情報コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/subsidy")
public class SubsidyController {

	@Autowired
	private SubsidyService subsidyService;

	/**
	 * Task.66 企業情報編集画面 初期表示
	 * 
	 * @param model
	 * @return 企業情報編集画面
	 */
	@RequestMapping("/company/update")
	public String companyUpdate(@ModelAttribute CompanySubsidyForm companySubsidyForm) {
		subsidyService.setCompanySubsidyForm(companySubsidyForm);
		return "subsidy/company/update";
	}

	/**
	 * Task.66 企業情報編集画面 『登録する』ボタン押下
	 * 
	 * @param companySubsidyForm
	 * @param result
	 * @return 受講生登録画面
	 */
	@RequestMapping(path = "/company/update", method = RequestMethod.POST)
	public String companyUpdatePost(CompanySubsidyForm companySubsidyForm, BindingResult result) {
		subsidyService.companyUpdateInputCheck(companySubsidyForm, result);
		if (result.hasErrors()) {
			return "subsidy/company/update";
		}
		subsidyService.registCompanySubsidyInfo(companySubsidyForm);
		return "redirect:/student/regist";
	}

}
