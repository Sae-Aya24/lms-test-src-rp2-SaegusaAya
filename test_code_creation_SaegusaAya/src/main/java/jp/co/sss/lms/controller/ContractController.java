package jp.co.sss.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.lms.dto.AgreementConsentDto;
import jp.co.sss.lms.dto.AgreementDto;
import jp.co.sss.lms.service.ContractService;

/**
 * 契約情報コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/contract")
public class ContractController {

	@Autowired
	private ContractService contractService;

	/**
	 * 過去契約一覧画面 初期表示
	 * 
	 * @param model
	 * @return 過去契約一覧画面
	 */
	@RequestMapping(path = "/history/list")
	public String historyList(Model model) {
		List<AgreementConsentDto> agreementConsentDtoList = contractService
				.getAgreementConsentDtoList();
		model.addAttribute("agreementConsentDtoList", agreementConsentDtoList);
		return "contract/history/list";
	}

	/**
	 * Task.64 過去契約一覧画面 『確認』ボタン押下
	 * 
	 * @param agreementConsentId
	 * @param model
	 * @return 契約確認画面
	 */
	@RequestMapping(path = "/history/detail", method = RequestMethod.POST)
	public String historyDetail(Integer agreementConsentId, Model model) {
		AgreementDto agreementDto = contractService.getAgreementDto(agreementConsentId);
		model.addAttribute("agreementDto", agreementDto);
		return "contract/history/detail";
	}

	/**
	 * Task.65 契約書確認画面 初期表示
	 * 
	 * @param model
	 * @return 契約書確認画面
	 */
	@RequestMapping(path = "/agreement/regist", method = RequestMethod.GET)
	public String agreementRegist(Model model) {
		AgreementDto agreementDto = contractService.getDisagreementDto();
		model.addAttribute("agreementDto", agreementDto);
		return "contract/agreement/regist";
	}

	/**
	 * Task.65 契約書確認画面 『企業情報登録』ボタン押下
	 * 
	 * @param agreementConsentId
	 * @return 企業情報編集画面
	 */
	@RequestMapping(path = "/agreement/regist", method = RequestMethod.POST)
	public String agreementRegistPost(Integer agreementConsentId) {
		contractService.updateConsentFlg(agreementConsentId);
		return "redirect:/subsidy/company/update";
	}

}
