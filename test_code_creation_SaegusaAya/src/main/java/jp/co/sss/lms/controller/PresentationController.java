package jp.co.sss.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.lms.dto.PresentationDto;
import jp.co.sss.lms.form.PresentationForm;
import jp.co.sss.lms.service.PresentationService;
import jp.co.sss.lms.util.LoginUserUtil;

/**
 * 成果報告会コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/presentation")
public class PresentationController {

	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private PresentationService presentationService;

	/**
	 * Task.60 成果報告会一覧画面 初期表示
	 * 
	 * @param model
	 * @return 成果報告会一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(Model model) {
		List<PresentationDto> presentationDtoList = presentationService.getPresentationList();
		model.addAttribute("presentationDtoList", presentationDtoList);
		// Task.75 予約期限日判定フラグ
		if (loginUserUtil.isCompany()) {
			boolean notEnterFlg = presentationService.notReserveCheck();
			model.addAttribute("notEnterFlg", notEnterFlg);
		}
		return "presentation/list";
	}

	/**
	 * Task.61 成果報告予約状況詳細画面 初期表示
	 * 成果報告会一覧画面 『確認する』ボタン押下
	 * 
	 * @param presentationScheduleId
	 * @param model
	 * @return 成果報告予約状況詳細画面
	 */
	@RequestMapping(path = "/reserveStatusDetail")
	public String reserveStatusDetail(Integer presentationScheduleId, Model model) {
		PresentationDto presentationDto = presentationService
				.getReserveStatusDetail(presentationScheduleId);
		model.addAttribute("presentationDto", presentationDto);
		return "presentation/reserveStatusDetail";
	}

	/**
	 * Task.62 チーム編成一覧画面 初期表示
	 * 
	 * @param model
	 * @return チーム編成一覧画面
	 */
	@RequestMapping(path = "/teamList")
	public String teamList(Model model) {
		List<PresentationDto> presentationDtoList = presentationService.getPresentationList();
		model.addAttribute("presentationDtoList", presentationDtoList);
		return "presentation/teamList";
	}

	/**
	 * Task.63 チーム編成詳細画面 初期表示
	 * チーム編成一覧画面 『確認する』ボタン押下
	 * 
	 * @param presentationScheduleId
	 * @param presentationTeamId
	 * @param model
	 * @return チーム編成詳細画面
	 */
	@RequestMapping(path = "/teamDetail")
	public String teamDetail(Integer presentationScheduleId, Integer presentationTeamId, Model model) {

		PresentationDto presentationDto = presentationService
				.getPresentationTeamDetailDtoList(presentationScheduleId, presentationTeamId);
		model.addAttribute("presentationDto", presentationDto);

		return "presentation/teamDetail";
	}

	/**
	 * Task.76 成果報告参加予約(新規予約)画面 初期表示
	 * 成果報告会一覧画面 『予約する』ボタン押下
	 * 
	 * @param presentationScheduleId
	 * @param model
	 * @return 成果報告参加予約(新規予約)画面
	 */
	@RequestMapping(path = "/reserveRegist")
	public String reserveRegist(Integer presentationScheduleId, Model model) {
		PresentationDto presentationDto = presentationService
				.getReserveDetail(presentationScheduleId);
		model.addAttribute("presentationDto", presentationDto);
		return "presentation/reserveRegist";
	}

	/**
	 * Task.76 成果報告会予約(新規予約)画面 『予約する』ボタン押下
	 * 
	 * @param presentationForm
	 * @return 成果報告会一覧画面
	 */
	@RequestMapping(path = "/reserveRegist", method = RequestMethod.POST)
	public String reserveRegistComplete(PresentationForm presentationForm) {

		presentationService.regist(presentationForm);

		return "redirect:/presentation/list";
	}

	/**
	 * Task.77 成果報告会一覧画面 『予約を変更する』ボタン押下
	 * 
	 * @param presentationScheduleId
	 * @param model
	 * @return 成果報告会予約(予約変更)画面
	 */
	@RequestMapping(path = "/reserveUpdate")
	public String reserveUpdate(Integer presentationScheduleId, Model model) {

		PresentationDto presentationDto = presentationService
				.getReserveDetail(presentationScheduleId);
		model.addAttribute("presentationDto", presentationDto);

		return "presentation/reserveUpdate";
	}

	/**
	 * Task.77 成果報告会予約(予約変更)画面 『予約を変更する』ボタン押下
	 * 
	 * @param presentationForm
	 * @return 成果報告会一覧画面
	 */
	@RequestMapping(path = "/reserveUpdate", method = RequestMethod.POST)
	public String reserveUpdateComplete(PresentationForm presentationForm) {

		presentationService.regist(presentationForm);

		return "redirect:/presentation/list";
	}

	
}
