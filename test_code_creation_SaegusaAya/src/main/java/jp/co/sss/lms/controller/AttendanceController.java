package jp.co.sss.lms.controller;

import java.text.ParseException;
import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.dto.AttendanceManagementDto;
import jp.co.sss.lms.dto.LmsUserDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.UserDetailDto;
import jp.co.sss.lms.form.AttendanceForm;
import jp.co.sss.lms.form.AttendanceSearchForm;
import jp.co.sss.lms.form.BulkRegistForm;
import jp.co.sss.lms.service.StudentAttendanceService;
import jp.co.sss.lms.service.UserService;
import jp.co.sss.lms.util.AttendanceUtil;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.LoginUserUtil;

/**
 * 勤怠管理コントローラ
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/attendance")
public class AttendanceController {

	@Autowired
	private StudentAttendanceService studentAttendanceService;
	@Autowired
	private AttendanceUtil attendanceUtil;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private UserService userService;

	/**
	 * 勤怠管理画面 初期表示
	 * 
	 * @param lmsUserId
	 * @param courseId
	 * @param model
	 * @return 勤怠管理画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/detail", method = RequestMethod.GET)
	public String index(Integer lmsUserId, Integer courseId, Model model) throws ParseException {

		if (loginUserUtil.isStudent()) {
			lmsUserId = loginUserDto.getLmsUserId();
			courseId = loginUserDto.getCourseId();
			// Task.25 過去日が未入力の場合の表示
			boolean notEnterFlg = studentAttendanceService.notEnterCheck();
			model.addAttribute("notEnterFlg", notEnterFlg);
		} else {
			// Task.71
			LmsUserDto lmsUserDto = userService.getUserDetail(lmsUserId);
			model.addAttribute("lmsUserDto", lmsUserDto);
		}

		// 勤怠一覧の取得
		List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
				.getAttendanceManagement(courseId, lmsUserId);
		model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

		return "attendance/detail";
	}

	/**
	 * 勤怠管理画面 『出勤』ボタン押下
	 * 
	 * @param model
	 * @return 勤怠管理画面
	 */
	@RequestMapping(path = "/detail", params = "punchIn", method = RequestMethod.POST)
	public String punchIn(Model model) {

		// 更新前のチェック
		String error = studentAttendanceService.punchCheck(Constants.CODE_VAL_ATWORK);
		model.addAttribute("error", error);
		// 勤怠登録
		if (error == null) {
			String message = studentAttendanceService.setPunchIn();
			model.addAttribute("message", message);
		}
		// 一覧の再取得
		List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
				.getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
		model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

		return "attendance/detail";
	}

	/**
	 * 勤怠管理画面 『退勤』ボタン押下
	 * 
	 * @param model
	 * @return 勤怠管理画面
	 */
	@RequestMapping(path = "/detail", params = "punchOut", method = RequestMethod.POST)
	public String punchOut(Model model) {

		// 更新前のチェック
		String error = studentAttendanceService.punchCheck(Constants.CODE_VAL_LEAVING);
		model.addAttribute("error", error);
		// 勤怠登録
		if (error == null) {
			String message = studentAttendanceService.setPunchOut();
			model.addAttribute("message", message);
		}
		// 一覧の再取得
		List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
				.getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
		model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

		return "attendance/detail";
	}

	/**
	 * 勤怠管理画面 『勤怠情報を直接編集する』リンク押下
	 * 
	 * @param model
	 * @return 勤怠情報直接変更画面
	 */
	@RequestMapping(path = "/update")
	public String update(Model model) {
		// 勤怠管理リストの取得
		List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
				.getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
		// 勤怠フォームの生成
		AttendanceForm attendanceForm = studentAttendanceService
				.setAttendanceForm(attendanceManagementDtoList);
		model.addAttribute("attendanceForm", attendanceForm);
		return "attendance/update";
	}

	/**
	 * 勤怠情報直接変更画面 『更新』ボタン押下
	 * 
	 * @param attendanceForm
	 * @param model
	 * @param result
	 * @return 勤怠管理画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/update", params = "complete", method = RequestMethod.POST)
	public String complete(AttendanceForm attendanceForm, Model model, BindingResult result)
			throws ParseException {

		// Task.26 出退勤時間の入力方法変更
		studentAttendanceService.formatConversion(attendanceForm);

		// Task.27 ①入力チェックの実装
		studentAttendanceService.updateInputCheck(attendanceForm, result);
		if (result.hasErrors()) {
			attendanceForm.setBlankTimes(attendanceUtil.setBlankTime());
			attendanceForm.setHourMap(attendanceUtil.getHourMap());
			attendanceForm.setMinuteMap(attendanceUtil.getMinuteMap());
			return "attendance/update";
		}

		// 更新
		String message = studentAttendanceService.update(attendanceForm);
		model.addAttribute("message", message);

		// 一覧の再取得
		List<AttendanceManagementDto> attendanceManagementDtoList = studentAttendanceService
				.getAttendanceManagement(loginUserDto.getCourseId(), loginUserDto.getLmsUserId());
		model.addAttribute("attendanceManagementDtoList", attendanceManagementDtoList);

		return "attendance/detail";
	}

	/**
	 * Task.57 勤怠情報確認（受講生一覧）画面 初期表示
	 * 
	 * @param model
	 * @return 勤怠情報確認（受講生一覧）画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list")
	public String list(AttendanceSearchForm attendanceSearchForm, Model model)
			throws ParseException {

		// 検索エリア情報取得
		studentAttendanceService.getAttendanceSearchForm(attendanceSearchForm, model);

		// 受講生勤怠情報取得
		List<UserDetailDto> userDetailDtoList = studentAttendanceService
				.getAttendanceDetailDtoList(attendanceSearchForm);
		model.addAttribute("userDetailDtoList", userDetailDtoList);

		return "attendance/list";
	}

	/**
	 * Task.58 勤怠一括登録画面 初期表示
	 * 
	 * @param bulkRegistForm
	 * @return 勤怠一括登録画面
	 */
	@RequestMapping(path = "/bulkRegist")
	public String bulkRegist(BulkRegistForm bulkRegistForm) {
		studentAttendanceService.setBulkRegistForm(bulkRegistForm);
		return "attendance/bulkRegist";
	}

	/**
	 * Task.58 勤怠一括登録画面 『検索』ボタン押下
	 * 
	 * @param bulkRegistForm
	 * @param result
	 * @return 勤怠一括登録画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/bulkRegist", params = "search")
	public String bulkRegistSearch(@Valid BulkRegistForm bulkRegistForm, BindingResult result)
			throws ParseException {

		studentAttendanceService.searchInputCheck(bulkRegistForm, result);
		if (!result.hasErrors()) {
			studentAttendanceService.getUserAttendance(bulkRegistForm);
		}

		studentAttendanceService.setBulkRegistForm(bulkRegistForm);

		return "attendance/bulkRegist";
	}

	/**
	 * Task.58 勤怠一括登録画面 『確定する』ボタン押下
	 * 
	 * @param bulkRegistForm
	 * @param result
	 * @param redirectAttributes
	 * @return 勤怠一括登録画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/bulkRegist", params = "complete", method = RequestMethod.POST)
	public String bulkRegistComplete(BulkRegistForm bulkRegistForm, BindingResult result,
			RedirectAttributes redirectAttributes) throws ParseException {

		// 入力チェック
		studentAttendanceService.bulkRegistInputCheck(bulkRegistForm, result);
		if (result.hasErrors()) {
			studentAttendanceService.setBulkRegistForm(bulkRegistForm);
			return "attendance/bulkRegist";
		}

		// 勤怠一括登録処理
		String message = studentAttendanceService.bulkUpdate(bulkRegistForm);

		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("bulkRegistForm", bulkRegistForm);
		return "redirect:/attendance/bulkRegist?search";
	}

}