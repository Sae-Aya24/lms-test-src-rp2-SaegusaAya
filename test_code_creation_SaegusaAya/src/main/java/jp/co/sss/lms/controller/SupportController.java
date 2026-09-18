package jp.co.sss.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import jp.co.sss.lms.dto.OperatorDto;
import jp.co.sss.lms.service.SupportService;
import jp.co.sss.lms.util.Constants;

/**
 * Task.41 サポートセンターコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/support")
public class SupportController {

	@Autowired
	private SupportService supportService;

	/**
	 * Task.41 サポートセンター問い合わせ画面 初期表示
	 * 
	 * @param model
	 * @return サポートセンター問い合わせ画面
	 */
	@RequestMapping(path = "")
	public String index(Model model) {

		// オペレーターの取得（単元別）
		List<OperatorDto> unitOperatorDtoList = supportService
				.getOperator(Constants.CODE_VAL_OPERATOR_TYPE_UNIT);
		model.addAttribute("unitOperatorDtoList", unitOperatorDtoList);

		// オペレーターの取得（直通）
		List<OperatorDto> directOperatorDtoList = supportService
				.getOperator(Constants.CODE_VAL_OPERATOR_TYPE_DIRECT);
		model.addAttribute("directOperatorDtoList", directOperatorDtoList);

		return "support/index";
	}

}
