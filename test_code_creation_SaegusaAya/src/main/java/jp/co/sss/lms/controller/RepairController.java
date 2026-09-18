package jp.co.sss.lms.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.lms.dto.RepairDto;
import jp.co.sss.lms.form.RepairForm;
import jp.co.sss.lms.service.RepairService;

/**
 * 改修履歴コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/repair")
public class RepairController {

	@Autowired
	private RepairService repairService;

	/**
	 * Task.87 改修履歴一覧画面 初期表示
	 * 
	 * @param model
	 * @return 改修履歴一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(Model model) {

		List<RepairDto> repairDtoList = repairService.getRepaiirDtoList();
		model.addAttribute("repairDtoList", repairDtoList);

		return "repair/list";
	}

	/**
	 * Task.87 改修履歴一覧画面 『削除』ボタン押下
	 * 
	 * @param repairForm
	 * @return 改修履歴一覧画面
	 */
	@RequestMapping(path = "/list", params = "delete", method = RequestMethod.POST)
	public String delete(RepairForm repairForm) {
		repairService.delete(repairForm);
		return "redirect:/repair/list";
	}

	/**
	 * Task.88 改修履歴一覧画面 『新規登録』または『変更』ボタン押下
	 * 
	 * @param repairForm
	 * @return 改修登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(RepairForm repairForm) {
		repairService.setRepairForm(repairForm);
		return "repair/regist";
	}

	/**
	 * Task.88 改修登録画面『登録』または『変更』ボタン押下
	 * 
	 * @param repairForm
	 * @param result
	 * @return 改修履歴一覧画面
	 */
	@RequestMapping(path = "/regist", params = "complete", method = RequestMethod.POST)
	public String complete(@Valid RepairForm repairForm, BindingResult result) {

		if (result.hasErrors()) {
			return "repair/regist";
		}

		repairService.regist(repairForm);

		return "redirect:/repair/list";
	}

}
