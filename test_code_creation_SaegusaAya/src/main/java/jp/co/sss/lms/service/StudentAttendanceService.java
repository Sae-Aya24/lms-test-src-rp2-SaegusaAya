package jp.co.sss.lms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.AttendanceManagementDto;
import jp.co.sss.lms.dto.CompanyDto;
import jp.co.sss.lms.dto.CourseDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.PlaceDto;
import jp.co.sss.lms.dto.UserAttendanceDto;
import jp.co.sss.lms.dto.UserDetailDto;
import jp.co.sss.lms.entity.TCompanyAttendance;
import jp.co.sss.lms.entity.TStudentAttendance;
import jp.co.sss.lms.enums.AttendanceStatusEnum;
import jp.co.sss.lms.form.AttendanceForm;
import jp.co.sss.lms.form.AttendanceSearchForm;
import jp.co.sss.lms.form.BulkRegistForm;
import jp.co.sss.lms.form.DailyAttendanceForm;
import jp.co.sss.lms.mapper.MLmsUserMapper;
import jp.co.sss.lms.mapper.MPlaceMapper;
import jp.co.sss.lms.mapper.TCompanyAttendanceMapper;
import jp.co.sss.lms.mapper.TStudentAttendanceMapper;
import jp.co.sss.lms.mapper.TUserPlaceMapper;
import jp.co.sss.lms.util.AttendanceUtil;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.DateUtil;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.PlaceUtil;
import jp.co.sss.lms.util.TrainingTime;

/**
 * 勤怠情報（受講生入力）サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class StudentAttendanceService {
	@Autowired
	private TrainingTime trainingTime;
	@Autowired
	private DateUtil dateUtil;
	@Autowired
	private AttendanceUtil attendanceUtil;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MLmsUserMapper mLmsUserMapper;
	@Autowired
	private MPlaceMapper mPlaceMapper;
	@Autowired
	private TCompanyAttendanceMapper tCompanyAttendanceMapper;
	@Autowired
	private TUserPlaceMapper tUserPlaceMapper;
	@Autowired
	private TStudentAttendanceMapper tStudentAttendanceMapper;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private CourseService courseService;
	@Autowired
	private CompanyService companyService;

	/**
	 * 勤怠一覧情報取得
	 * 
	 * @param courseId
	 * @param lmsUserId
	 * @return 勤怠管理画面用DTOリスト
	 */
	public List<AttendanceManagementDto> getAttendanceManagement(Integer courseId,
			Integer lmsUserId) {
		// 勤怠管理リストの取得
		List<AttendanceManagementDto> attendanceManagementDtoList = tStudentAttendanceMapper
				.getAttendanceManagement(courseId, lmsUserId, Constants.DB_FLG_FALSE);
		for (AttendanceManagementDto dto : attendanceManagementDtoList) {
			// 中抜け時間を設定
			if (dto.getBlankTime() != null) {
				TrainingTime blankTime = attendanceUtil.calcBlankTime(dto.getBlankTime());
				dto.setBlankTimeValue(String.valueOf(blankTime));
			}
			// 遅刻早退区分判定
			AttendanceStatusEnum statusEnum = AttendanceStatusEnum.getEnum(dto.getStatus());
			if (statusEnum != null) {
				dto.setStatusDispName(statusEnum.name);
			}
		}
		return attendanceManagementDtoList;
	}

	/**
	 * 出退勤更新前のチェック
	 * 
	 * @param attendanceType
	 * @return エラーメッセージ
	 */
	public String punchCheck(Short attendanceType) {
		Date trainingDate = attendanceUtil.getTrainingDate();
		// 権限チェック
		if (!loginUserUtil.isStudent()) {
			return messageUtil.getMessage(Constants.VALID_KEY_AUTHORIZATION);
		}
		// 研修日チェック
		if (!attendanceUtil.isWorkDay(loginUserDto.getCourseId(), trainingDate)) {
			return messageUtil.getMessage(Constants.VALID_KEY_ATTENDANCE_NOTWORKDAY);
		}
		// 登録情報チェック
		TStudentAttendance tStudentAttendance = tStudentAttendanceMapper
				.findByLmsUserIdAndTrainingDate(loginUserDto.getLmsUserId(), trainingDate,
						Constants.DB_FLG_FALSE);
		switch (attendanceType) {
		case Constants.CODE_VAL_ATWORK:
			if (tStudentAttendance != null
					&& !tStudentAttendance.getTrainingStartTime().equals("")) {
				// 本日の勤怠情報は既に入力されています。直接編集してください。
				return messageUtil.getMessage(Constants.VALID_KEY_ATTENDANCE_PUNCHALREADYEXISTS);
			}
			break;
		case Constants.CODE_VAL_LEAVING:
			if (tStudentAttendance == null
					|| tStudentAttendance.getTrainingStartTime().equals("")) {
				// 出勤情報がないため退勤情報を入力出来ません。
				return messageUtil.getMessage(Constants.VALID_KEY_ATTENDANCE_PUNCHINEMPTY);
			}
			if (!tStudentAttendance.getTrainingEndTime().equals("")) {
				// 本日の勤怠情報は既に入力されています。直接編集してください。
				return messageUtil.getMessage(Constants.VALID_KEY_ATTENDANCE_PUNCHALREADYEXISTS);
			}
			TrainingTime trainingStartTime = new TrainingTime(
					tStudentAttendance.getTrainingStartTime());
			TrainingTime trainingEndTime = new TrainingTime();
			if (trainingStartTime.compareTo(trainingEndTime) > 0) {
				// 退勤時刻は出勤時刻より後でなければいけません。
				return messageUtil.getMessage(Constants.VALID_KEY_ATTENDANCE_TRAININGTIMERANGE);
			}
			break;
		}
		return null;
	}

	/**
	 * 出勤ボタン処理
	 * 
	 * @return 完了メッセージ
	 */
	public String setPunchIn() {
		// 当日日付
		Date date = new Date();
		// 本日の研修日
		Date trainingDate = attendanceUtil.getTrainingDate();
		// 現在の研修時刻
		TrainingTime trainingStartTime = new TrainingTime();
		// 遅刻早退ステータス
		AttendanceStatusEnum attendanceStatusEnum = attendanceUtil.getStatus(trainingStartTime,
				null);
		// 研修日の勤怠情報取得
		TStudentAttendance tStudentAttendance = tStudentAttendanceMapper
				.findByLmsUserIdAndTrainingDate(loginUserDto.getLmsUserId(), trainingDate,
						Constants.DB_FLG_FALSE);
		if (tStudentAttendance == null) {
			// 登録処理
			tStudentAttendance = new TStudentAttendance();
			tStudentAttendance.setLmsUserId(loginUserDto.getLmsUserId());
			tStudentAttendance.setTrainingDate(trainingDate);
			tStudentAttendance.setTrainingStartTime(trainingStartTime.toString());
			tStudentAttendance.setTrainingEndTime("");
			tStudentAttendance.setStatus(attendanceStatusEnum.code);
			tStudentAttendance.setNote("");
			tStudentAttendance.setAccountId(loginUserDto.getAccountId());
			tStudentAttendance.setDeleteFlg(Constants.DB_FLG_FALSE);
			tStudentAttendance.setFirstCreateUser(loginUserDto.getLmsUserId());
			tStudentAttendance.setFirstCreateDate(date);
			tStudentAttendance.setLastModifiedUser(loginUserDto.getLmsUserId());
			tStudentAttendance.setLastModifiedDate(date);
			tStudentAttendance.setBlankTime(null);
			tStudentAttendanceMapper.insert(tStudentAttendance);
		} else {
			// 更新処理
			tStudentAttendance.setTrainingStartTime(trainingStartTime.toString());
			tStudentAttendance.setStatus(attendanceStatusEnum.code);
			tStudentAttendance.setDeleteFlg(Constants.DB_FLG_FALSE);
			tStudentAttendance.setLastModifiedUser(loginUserDto.getLmsUserId());
			tStudentAttendance.setLastModifiedDate(date);
			tStudentAttendanceMapper.update(tStudentAttendance);
		}
		// 完了メッセージ
		return messageUtil.getMessage(Constants.PROP_KEY_ATTENDANCE_UPDATE_NOTICE);
	}

	/**
	 * 退勤ボタン処理
	 * 
	 * @return 完了メッセージ
	 */
	public String setPunchOut() {
		// 当日日付
		Date date = new Date();
		// 本日の研修日
		Date trainingDate = attendanceUtil.getTrainingDate();
		// 研修日の勤怠情報取得
		TStudentAttendance tStudentAttendance = tStudentAttendanceMapper
				.findByLmsUserIdAndTrainingDate(loginUserDto.getLmsUserId(), trainingDate,
						Constants.DB_FLG_FALSE);
		// 出退勤時刻
		TrainingTime trainingStartTime = new TrainingTime(
				tStudentAttendance.getTrainingStartTime());
		TrainingTime trainingEndTime = new TrainingTime();
		// 遅刻早退ステータス
		AttendanceStatusEnum attendanceStatusEnum = attendanceUtil.getStatus(trainingStartTime,
				trainingEndTime);
		// 更新処理
		tStudentAttendance.setTrainingEndTime(trainingEndTime.toString());
		tStudentAttendance.setStatus(attendanceStatusEnum.code);
		tStudentAttendance.setDeleteFlg(Constants.DB_FLG_FALSE);
		tStudentAttendance.setLastModifiedUser(loginUserDto.getLmsUserId());
		tStudentAttendance.setLastModifiedDate(date);
		tStudentAttendanceMapper.update(tStudentAttendance);
		// 完了メッセージ
		return messageUtil.getMessage(Constants.PROP_KEY_ATTENDANCE_UPDATE_NOTICE);
	}

	/**
	 * 勤怠フォームへ設定
	 * 
	 * @param attendanceList
	 * @return 勤怠編集フォーム
	 */
	public AttendanceForm setAttendanceForm(
			List<AttendanceManagementDto> attendanceManagementDtoList) {

		AttendanceForm attendanceForm = new AttendanceForm();
		attendanceForm.setAttendanceList(new ArrayList<DailyAttendanceForm>());
		attendanceForm.setLmsUserId(loginUserDto.getLmsUserId());
		attendanceForm.setUserName(loginUserDto.getUserName());
		attendanceForm.setLeaveFlg(loginUserDto.getLeaveFlg());
		attendanceForm.setBlankTimes(attendanceUtil.setBlankTime());
		// Task.26 出退勤時間の入力方法変更
		attendanceForm.setHourMap(attendanceUtil.getHourMap());
		attendanceForm.setMinuteMap(attendanceUtil.getMinuteMap());

		// 途中退校している場合のみ設定
		if (loginUserDto.getLeaveDate() != null) {
			attendanceForm
					.setLeaveDate(dateUtil.dateToString(loginUserDto.getLeaveDate(), "yyyy-MM-dd"));
			attendanceForm.setDispLeaveDate(
					dateUtil.dateToString(loginUserDto.getLeaveDate(), "yyyy年M月d日"));
		}

		// 勤怠管理リストの件数分、日次の勤怠フォームに移し替え
		for (AttendanceManagementDto attendanceManagementDto : attendanceManagementDtoList) {
			DailyAttendanceForm dailyAttendanceForm = new DailyAttendanceForm();
			dailyAttendanceForm
					.setStudentAttendanceId(attendanceManagementDto.getStudentAttendanceId());
			dailyAttendanceForm
					.setTrainingDate(dateUtil.toString(attendanceManagementDto.getTrainingDate()));
			dailyAttendanceForm
					.setTrainingStartTime(attendanceManagementDto.getTrainingStartTime());
			dailyAttendanceForm.setTrainingEndTime(attendanceManagementDto.getTrainingEndTime());

			// Task.26 出退勤時間の入力方法変更
			dailyAttendanceForm.setTrainingStartTimeHour(
					attendanceUtil.getHour(attendanceManagementDto.getTrainingStartTime()));
			dailyAttendanceForm.setTrainingStartTimeMinute(
					attendanceUtil.getMinute(attendanceManagementDto.getTrainingStartTime()));
			dailyAttendanceForm.setTrainingEndTimeHour(
					attendanceUtil.getHour(attendanceManagementDto.getTrainingEndTime()));
			dailyAttendanceForm.setTrainingEndTimeMinute(
					attendanceUtil.getMinute(attendanceManagementDto.getTrainingEndTime()));

			dailyAttendanceForm.setStatus(String.valueOf(attendanceManagementDto.getStatus()));
			dailyAttendanceForm.setNote(attendanceManagementDto.getNote());
			dailyAttendanceForm.setSectionName(attendanceManagementDto.getSectionName());
			dailyAttendanceForm.setIsToday(attendanceManagementDto.getIsToday());
			dailyAttendanceForm.setDispTrainingDate(dateUtil
					.dateToString(attendanceManagementDto.getTrainingDate(), "yyyy年M月d日(E)"));
			dailyAttendanceForm.setStatusDispName(attendanceManagementDto.getStatusDispName());
			if (attendanceManagementDto.getBlankTime() != null) {
				dailyAttendanceForm.setBlankTime(attendanceManagementDto.getBlankTime());
				dailyAttendanceForm.setBlankTimeValue(String.valueOf(
						attendanceUtil.calcBlankTime(attendanceManagementDto.getBlankTime())));
			}
			attendanceForm.getAttendanceList().add(dailyAttendanceForm);
		}
		System.out.println("★" + attendanceForm);
		return attendanceForm;
	}

	/**
	 * 勤怠登録・更新処理
	 * 
	 * @param attendanceForm
	 * @return 完了メッセージ
	 * @throws ParseException
	 */
	public String update(AttendanceForm attendanceForm) throws ParseException {

		Integer lmsUserId = loginUserUtil.isStudent() ? loginUserDto.getLmsUserId()
				: attendanceForm.getLmsUserId();

		// 現在の勤怠情報（受講生入力）リストを取得
		List<TStudentAttendance> tStudentAttendanceList = tStudentAttendanceMapper
				.findByLmsUserId(lmsUserId, Constants.DB_FLG_FALSE);

		// 入力された情報を更新用のエンティティに移し替え
		Date date = new Date();
		for (DailyAttendanceForm dailyAttendanceForm : attendanceForm.getAttendanceList()) {

			// 更新用エンティティ作成
			TStudentAttendance tStudentAttendance = new TStudentAttendance();
			// 日次勤怠フォームから更新用のエンティティにコピー
			BeanUtils.copyProperties(dailyAttendanceForm, tStudentAttendance);
			// 研修日付
			tStudentAttendance
					.setTrainingDate(dateUtil.parse(dailyAttendanceForm.getTrainingDate()));
			// 現在の勤怠情報リストのうち、研修日が同じものを更新用エンティティで上書き
			for (TStudentAttendance entity : tStudentAttendanceList) {
				if (entity.getTrainingDate().equals(tStudentAttendance.getTrainingDate())) {
					tStudentAttendance = entity;
					break;
				}
			}
			tStudentAttendance.setLmsUserId(lmsUserId);
			tStudentAttendance.setAccountId(loginUserDto.getAccountId());
			// 出勤時刻整形
			TrainingTime trainingStartTime = null;
			trainingStartTime = new TrainingTime(dailyAttendanceForm.getTrainingStartTime());
			tStudentAttendance.setTrainingStartTime(trainingStartTime.getFormattedString());
			// 退勤時刻整形
			TrainingTime trainingEndTime = null;
			trainingEndTime = new TrainingTime(dailyAttendanceForm.getTrainingEndTime());
			tStudentAttendance.setTrainingEndTime(trainingEndTime.getFormattedString());
			// 中抜け時間
			tStudentAttendance.setBlankTime(dailyAttendanceForm.getBlankTime());
			// 遅刻早退ステータス
			if ((trainingStartTime != null || trainingEndTime != null)
					&& !dailyAttendanceForm.getStatusDispName().equals("欠席")) {
				AttendanceStatusEnum attendanceStatusEnum = attendanceUtil
						.getStatus(trainingStartTime, trainingEndTime);
				tStudentAttendance.setStatus(attendanceStatusEnum.code);
			}
			// 備考
			tStudentAttendance.setNote(dailyAttendanceForm.getNote());
			// 更新者と更新日時
			tStudentAttendance.setLastModifiedUser(loginUserDto.getLmsUserId());
			tStudentAttendance.setLastModifiedDate(date);
			// 削除フラグ
			tStudentAttendance.setDeleteFlg(Constants.DB_FLG_FALSE);
			// 登録用Listへ追加
			tStudentAttendanceList.add(tStudentAttendance);
		}
		// 登録・更新処理
		for (TStudentAttendance tStudentAttendance : tStudentAttendanceList) {
			if (tStudentAttendance.getStudentAttendanceId() == null) {
				tStudentAttendance.setFirstCreateUser(loginUserDto.getLmsUserId());
				tStudentAttendance.setFirstCreateDate(date);
				tStudentAttendanceMapper.insert(tStudentAttendance);
			} else {
				tStudentAttendanceMapper.update(tStudentAttendance);
			}
		}
		// 完了メッセージ
		return messageUtil.getMessage(Constants.PROP_KEY_ATTENDANCE_UPDATE_NOTICE);
	}

	/**
	 * Task.25 過去日の未入力チェック
	 * 
	 * @param trainingDate
	 * @return チェック結果
	 * @throws ParseException
	 */
	public Boolean notEnterCheck() throws ParseException {
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		Date trainingDate = df.parse(df.format(date));
		Integer notEnterCount = tStudentAttendanceMapper.notEnterCount(loginUserDto.getLmsUserId(),
				Constants.DB_FLG_FALSE, trainingDate);
		return notEnterCount > 0 ? true : false;
	}

	/**
	 * Task.26 出退勤時間の入力方法変更
	 * 
	 * @param attendanceForm
	 */
	public void formatConversion(AttendanceForm attendanceForm) {
		for (DailyAttendanceForm dailyAttendanceForm : attendanceForm.getAttendanceList()) {
			// 出勤時間をhh:mm形式に設定
			if (dailyAttendanceForm.getTrainingStartTimeHour() != null
					&& dailyAttendanceForm.getTrainingStartTimeMinute() != null) {
				dailyAttendanceForm.setTrainingStartTime(String.format("%02d",
						dailyAttendanceForm.getTrainingStartTimeHour()) + ":"
						+ String.format("%02d", dailyAttendanceForm.getTrainingStartTimeMinute()));
			}
			// 退勤時間をhh:mm形式に設定
			if (dailyAttendanceForm.getTrainingEndTimeHour() != null
					&& dailyAttendanceForm.getTrainingEndTimeMinute() != null) {
				dailyAttendanceForm.setTrainingEndTime(String.format("%02d",
						dailyAttendanceForm.getTrainingEndTimeHour()) + ":"
						+ String.format("%02d", dailyAttendanceForm.getTrainingEndTimeMinute()));
			}
		}
	}

	/**
	 * Task.27 勤怠入力チェック
	 * 
	 * @param attendanceForm
	 */
	public void updateInputCheck(AttendanceForm attendanceForm, BindingResult result) {

		// 入力パラメータ．勤怠リスト[n]の件数分入力チェックを行う
		for (int i = 0; i < attendanceForm.getAttendanceList().size(); i++) {
			DailyAttendanceForm attendance = attendanceForm.getAttendanceList().get(i);

			// 備考が100文字を超える場合
			if (!StringUtils.isBlank(attendance.getNote()) && attendance.getNote().length() > 100) {
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].note", messageUtil.getMessage(
								Constants.VALID_KEY_MAXLENGTH, new String[] { "備考", "100" })));
			}
			// 出勤時間の時間がブランクで分が入力されている場合
			if (attendance.getTrainingStartTimeHour() == null
					&& attendance.getTrainingStartTimeMinute() != null) {
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].trainingStartTimeHour",
						messageUtil.getMessage(Constants.INPUT_INVALID, new String[] { "出勤時間" })));
			}
			// 出勤時間の時間が入力されていて分がブランクの場合
			if (attendance.getTrainingStartTimeHour() != null
					&& attendance.getTrainingStartTimeMinute() == null) {
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].trainingStartTimeMinute",
						messageUtil.getMessage(Constants.INPUT_INVALID, new String[] { "出勤時間" })));
			}
			// 退勤時間の時間がブランクで分が入力されている場合
			if (attendance.getTrainingEndTimeHour() == null
					&& attendance.getTrainingEndTimeMinute() != null) {
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].trainingEndTimeHour",
						messageUtil.getMessage(Constants.INPUT_INVALID, new String[] { "退勤時間" })));
			}
			// 退勤時間の時間が入力されていて分がブランクの場合
			if (attendance.getTrainingEndTimeHour() != null
					&& attendance.getTrainingEndTimeMinute() == null) {
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].trainingEndTimeMinute",
						messageUtil.getMessage(Constants.INPUT_INVALID, new String[] { "退勤時間" })));
			}
			// 出勤時間に入力なし＆退勤時間に入力ありの場合
			if (attendance.getTrainingStartTimeHour() == null
					&& attendance.getTrainingStartTimeMinute() == null
					&& attendance.getTrainingEndTimeHour() != null
					&& attendance.getTrainingEndTimeMinute() != null) {
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].trainingStartTimeHour",
						messageUtil.getMessage(Constants.VALID_KEY_ATTENDANCE_PUNCHINEMPTY)));
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].trainingStartTimeMinute", ""));
			}
			if (result.hasErrors()) {
				continue;
			}
			// 出勤時間＞退勤時間の場合
			TrainingTime trainingStartTime = new TrainingTime(attendance.getTrainingStartTime());
			TrainingTime trainingEndTime = new TrainingTime(attendance.getTrainingEndTime());
			if (trainingStartTime.compareTo(trainingEndTime) > 0) {
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].trainingEndTimeHour",
						messageUtil.getMessage(Constants.VALID_KEY_ATTENDANCE_TRAININGTIMERANGE,
								new String[] { String.valueOf(i), String.valueOf(i) })));
				result.addError(new FieldError(result.getObjectName(),
						"attendanceList[" + i + "].trainingEndTimeMinute", ""));
				continue;
			}
			// 中抜け時間が勤務時間（出勤時間～退勤時間までの時間）を超える場合
			if (attendance.getBlankTime() != null) {
				TrainingTime startTime = new TrainingTime(0, 0);
				TrainingTime endTime = new TrainingTime(0, 0);
				if (!StringUtils.isBlank(attendance.getTrainingStartTime())
						&& !StringUtils.isBlank(attendance.getTrainingEndTime())) {
					if (trainingTime.isValidTrainingTime(attendance.getTrainingStartTime())
							&& trainingTime.isValidTrainingTime(attendance.getTrainingEndTime())) {
						startTime = new TrainingTime(attendance.getTrainingStartTime());
						endTime = new TrainingTime(attendance.getTrainingEndTime());
					}
				}
				String jukoTime = String.valueOf(attendanceUtil.calcJukoTime(startTime, endTime));
				int t = attendanceUtil.reverseBlankTime(jukoTime);
				if (t <= attendance.getBlankTime()) {
					result.addError(new FieldError(result.getObjectName(),
							"attendanceList[" + i + "].blankTime",
							messageUtil.getMessage(Constants.VALID_KEY_ATTENDANCE_BLANKTIMEERROR)));
				}
			}
		}
	}

	/**
	 * Task.57 検索エリア情報取得
	 * 
	 * @param attendanceSearchForm
	 * @param model
	 */
	public void getAttendanceSearchForm(AttendanceSearchForm attendanceSearchForm, Model model) {

		if (loginUserUtil.isTeacher()) {
			// コースDTOリスト取得
			List<CourseDto> courseDtoList = courseService.getCourseDtoList();
			model.addAttribute("courseDtoList", courseDtoList);
			// 会場DTO取得
			PlaceDto placeDto = placeService.getPlaceDto();
			model.addAttribute("placeDto", placeDto);
			// 企業DTOリスト取得
			List<CompanyDto> companyDtoList = companyService.getCompanyDtoList();
			model.addAttribute("companyDtoList", companyDtoList);
		}

		// Task.71
		if (loginUserUtil.isCompany()) {
			// コース終了経過月設定
			attendanceSearchForm
					.setPastTimeLabel(messageUtil.getMessage("setting.search.pastTimeLabel"));
		}

	}

	/**
	 * Task.57 勤怠情報一覧取得
	 * 
	 * @param attendanceSearchForm
	 * @return ユーザー基本情報DTOリスト
	 */
	public List<UserDetailDto> getAttendanceDetailDtoList(AttendanceSearchForm attendanceSearchForm)
			throws ParseException {

		List<Integer> placeIdList = new ArrayList<>();
		placeIdList = loginUserUtil.isTeacher()
				? tUserPlaceMapper.getPlaceId(loginUserDto.getLmsUserId(), Constants.DB_FLG_FALSE)
				: null;
		String role = Constants.CODE_VAL_ROLL_STUDENT;
		Short leaveFlg = null;
		Date pastDate = null;

		// Task.71 経過フラグがオフの場合の条件を追加
		if (!loginUserUtil.isTeacher() && (Objects.isNull(attendanceSearchForm.getPastFlg())
				|| attendanceSearchForm.getPastFlg() != 1)) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(sdf.format(date));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Integer pastTime = Integer.parseInt(messageUtil.getMessage("setting.search.pastTime"));
			calendar.add(Calendar.MONTH, -pastTime);
			pastDate = calendar.getTime();
		}

		// Task.71
		String companyName = loginUserUtil.isCompany() ? loginUserDto.getCompanyName()
				: attendanceSearchForm.getCompanyName();

		return mLmsUserMapper.getUserDetailForSearch(attendanceSearchForm.getCourseName(),
				companyName, attendanceSearchForm.getUserName(), placeIdList, role, leaveFlg,
				pastDate, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.58 勤怠一括登録フォームの初期設定
	 * 
	 * @param bulkRegistForm
	 */
	public void setBulkRegistForm(BulkRegistForm bulkRegistForm) {
		PlaceDto placeDto = placeService.getPlaceDto();
		String[] note = placeDto.getPlaceNote().split(PlaceUtil.REGEX_CHAR);
		String placeName = placeDto.getPlaceName() + "(" + note[1] + ")";
		bulkRegistForm.setPlaceName(placeName);
	}

	/**
	 * Task.58 検索時入力チェック
	 * 
	 * @param bulkRegistForm
	 * @param result
	 * @throws ParseException
	 */
	public void searchInputCheck(BulkRegistForm bulkRegistForm, BindingResult result)
			throws ParseException {
		// フォームの入力チェックでエラーがある場合はチェックを行わない
		if (result.hasErrors()) {
			return;
		}
		// 期間TOがシステム日付より未来日の場合はエラー
		String sysDate = dateUtil.getCurrentDateString().substring(0, 10).replace("/", "-");
		if (sysDate.compareTo(bulkRegistForm.getSearchPeriodTo()) < 0) {
			result.addError(new FieldError(result.getObjectName(), "searchPeriodFrom",
					messageUtil.getMessage(Constants.VALID_KEY_SEARCHTORANGEERROR,
							new String[] { "期間（from）" })));
			return;
		}
		// 期間FROMが期間TOより未来日の場合はエラー
		if (bulkRegistForm.getSearchPeriodTo()
				.compareTo(bulkRegistForm.getSearchPeriodFrom()) < 0) {
			result.addError(new FieldError(result.getObjectName(), "searchPeriodTo",
					messageUtil.getMessage(Constants.VALID_KEY_SEARCHPERIODCOMPAREERROR,
							new String[] { "期間（to）", "期間（from）" })));
			return;
		}
		int diffDay = 0;
		diffDay = dateUtil.differenceDays(bulkRegistForm.getSearchPeriodTo().replaceAll("-", "/"),
				bulkRegistForm.getSearchPeriodFrom().replaceAll("-", "/"));
		// 検索範囲がひと月を超える場合はエラー
		if (diffDay > 30) {
			result.addError(new FieldError(result.getObjectName(), "searchPeriodTo",
					messageUtil.getMessage(Constants.VALID_KEY_SEARCHSETTINGOVER,
							new String[] { "検索期間", "30日" })));
			return;
		}
	}

	/**
	 * Task.58 ユーザ勤怠情報の取得
	 * 
	 * @param bulkRegistForm
	 * @throws ParseException
	 */
	public void getUserAttendance(BulkRegistForm bulkRegistForm) throws ParseException {
		// 会場のユーザー勤怠情報DTOリストを取得
		Integer placeId = bulkRegistForm.getPlaceId() == null ? loginUserDto.getPlaceId()
				: bulkRegistForm.getPlaceId();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date from = sdf.parse(bulkRegistForm.getSearchPeriodFrom());
		Date to = sdf.parse(bulkRegistForm.getSearchPeriodTo());
		List<UserAttendanceDto> userAttendanceDtoList = mPlaceMapper.getUserAttendanceDto(placeId,
				from, to, Constants.DB_FLG_FALSE);

		// リストの件数分、日次の勤怠フォームに移し替え
		Date date = null;
		int index = 0;
		SimpleDateFormat sdfDisp = new SimpleDateFormat("yyyy年M月d日(E)");
		bulkRegistForm.setDailyAttendanceFormList(new ArrayList<DailyAttendanceForm>());
		final String BLANK_STR = "[未入力]";
		for (UserAttendanceDto userAttendanceDto : userAttendanceDtoList) {
			DailyAttendanceForm dailyAttendanceForm = new DailyAttendanceForm();
			dailyAttendanceForm.setTrainingDate(sdf.format(userAttendanceDto.getTrainingDate()));
			dailyAttendanceForm
					.setDispTrainingDate(sdfDisp.format(userAttendanceDto.getTrainingDate()));
			dailyAttendanceForm.setUserName(userAttendanceDto.getUserName());
			dailyAttendanceForm.setCourseName(userAttendanceDto.getCourseName());
			dailyAttendanceForm.setCompanyAttendanceId(userAttendanceDto.getCompanyAttendanceId());
			dailyAttendanceForm.setLmsUserId(String.valueOf(userAttendanceDto.getLmsUserId()));
			// 出勤
			if (StringUtils.isBlank(userAttendanceDto.getTrainingStartTime())) {
				dailyAttendanceForm.setTrainingStartTimeRaw(BLANK_STR);
			} else {
				dailyAttendanceForm
						.setTrainingStartTimeRaw(userAttendanceDto.getTrainingStartTime());
				// 出勤時間丸め処理
				TrainingTime startTime = new TrainingTime(userAttendanceDto.getTrainingStartTime());
				// 15分単位切上げ
				startTime = startTime.roundUp();
				startTime = trainingTime.max(startTime, Constants.SSS_WORK_START_TIME);
				dailyAttendanceForm.setTrainingStartTimeRounded(startTime.getFormattedString());
			}
			// 退勤
			if (StringUtils.isBlank(userAttendanceDto.getTrainingEndTime())) {
				dailyAttendanceForm.setTrainingEndTimeRaw(BLANK_STR);
			} else {
				dailyAttendanceForm.setTrainingEndTimeRaw(userAttendanceDto.getTrainingEndTime());
				// 退勤時間丸め処理
				TrainingTime endTime = new TrainingTime(userAttendanceDto.getTrainingEndTime());
				// 15分単位切捨て
				endTime = endTime.roundDown();
				// 受講生退勤、SSS定時のうち最も早いものを終了時刻とする
				endTime = trainingTime.min(endTime, Constants.SSS_WORK_END_TIME);
				dailyAttendanceForm.setTrainingEndTimeRounded(endTime.getFormattedString());
			}
			// 休憩時間を設定
			dailyAttendanceForm.setBlankTime(userAttendanceDto.getBlankTime());
			if (userAttendanceDto.getBlankTime() != null) {
				TrainingTime blankTime = attendanceUtil
						.calcBlankTime(userAttendanceDto.getBlankTime());
				dailyAttendanceForm.setBlankTimeValue(String.valueOf(blankTime));
			}
			// 遅刻早退区分判定
			dailyAttendanceForm.setStatus(String.valueOf(userAttendanceDto.getStatus()));
			AttendanceStatusEnum statusEnum = AttendanceStatusEnum
					.getEnum(userAttendanceDto.getStatus());
			if (statusEnum != null) {
				dailyAttendanceForm.setStatusDispName(statusEnum.name);
			}
			// 備考
			dailyAttendanceForm.setNote(userAttendanceDto.getNote());
			// インデックスの設定
			if (date == null) {
				dailyAttendanceForm.setIndex(String.valueOf(index));
				date = userAttendanceDto.getTrainingDate();
			} else if (date.compareTo(userAttendanceDto.getTrainingDate()) == 0) {
				dailyAttendanceForm.setIndex(String.valueOf(index));
			} else {
				dailyAttendanceForm.setIndex(String.valueOf(++index));
				date = userAttendanceDto.getTrainingDate();
			}
			bulkRegistForm.getDailyAttendanceFormList().add(dailyAttendanceForm);
		}
	}

	/**
	 * Task.58 勤怠一括登録入力チェック
	 * 
	 * @param bulkRegistForm
	 * @param result
	 */
	public void bulkRegistInputCheck(BulkRegistForm bulkRegistForm, BindingResult result) {

		int attendanceListSize = bulkRegistForm.getDailyAttendanceFormList().size();
		for (int i = 0; i < attendanceListSize; i++) {
			DailyAttendanceForm attendanceForm = bulkRegistForm.getDailyAttendanceFormList().get(i);
			if (bulkRegistForm.getTargetIndex().equals(attendanceForm.getIndex())) {
				if (attendanceForm.getIsAbsent() != null && attendanceForm.getIsAbsent()) {
					if (!StringUtils.isBlank(attendanceForm.getTrainingStartTime())
							|| !StringUtils.isBlank(attendanceForm.getTrainingEndTime())) {
						// 欠席なのに、開始か終了の時刻が入っていたらエラーとする
						String message = messageUtil.getMessage(
								Constants.VALID_KEY_ABSENTANDTRAININGTIMEEXISTSBULK,
								new String[] { attendanceForm.getDispTrainingDate() });
						String propertyName = "dailyAttendanceFormList[" + i + "].";
						if (StringUtils.isBlank(attendanceForm.getTrainingStartTime())) {
							propertyName += "trainingStartTime";
						} else {
							propertyName += "trainingEndTime";
						}
						result.addError(
								new FieldError(result.getObjectName(), propertyName, message));
					}
				}

				if (attendanceForm.getIsAbsent() == null) {
					if (StringUtils.isBlank(attendanceForm.getTrainingStartTime())
							|| StringUtils.isBlank(attendanceForm.getTrainingEndTime())) {
						// 出席の場合で、開始と終了の時刻が入っていない場合エラーとする
						String message = messageUtil.getMessage(
								Constants.VALID_KEY_REQUIREDTRAININGTIMEBULK,
								new String[] { attendanceForm.getDispTrainingDate() });
						String propertyName = "dailyAttendanceFormList[" + i + "].";
						if (StringUtils.isBlank(attendanceForm.getTrainingStartTime())) {
							propertyName += "trainingStartTime";
						} else {
							propertyName += "trainingEndTime";
						}
						result.addError(
								new FieldError(result.getObjectName(), propertyName, message));
					}
				}

				if (!StringUtils.isBlank(attendanceForm.getTrainingStartTime())) {
					if (!trainingTime.isValidTrainingTime(attendanceForm.getTrainingStartTime())) {
						String message = messageUtil.getMessage(
								Constants.VALID_KEY_TRAININGTIMEBULK,
								new String[] { attendanceForm.getDispTrainingDate() });
						result.addError(new FieldError(result.getObjectName(),
								"dailyAttendanceFormList[" + i + "].trainingStartTime", message));
					}
				}

				// 退勤時刻が入力されていた場合、時刻形式でないとエラー
				if (!StringUtils.isBlank(attendanceForm.getTrainingEndTime())) {
					if (!trainingTime.isValidTrainingTime(attendanceForm.getTrainingEndTime())) {
						String message = messageUtil.getMessage(
								Constants.VALID_KEY_TRAININGTIMEBULK,
								new String[] { attendanceForm.getDispTrainingDate() });
						result.addError(new FieldError(result.getObjectName(),
								"dailyAttendanceFormList[" + i + "].trainingEndTime", message));
					}
				}

				if (result.hasErrors()) {
					continue;
				}

				TrainingTime trainingStartTime = new TrainingTime(
						attendanceForm.getTrainingStartTime());
				TrainingTime trainingEndTime = new TrainingTime(
						attendanceForm.getTrainingEndTime());

				// 出勤が24時を超えていたらエラー
				TrainingTime endOfDate = new TrainingTime("24:00");
				if (!trainingStartTime.isBlank() && trainingStartTime.compareTo(endOfDate) >= 0) {
					String message = messageUtil.getMessage(Constants.VALID_KEY_MAXVALBULK,
							new String[] { attendanceForm.getDispTrainingDate(), "開始時間",
									endOfDate.getFormattedString() });
					result.addError(new FieldError(result.getObjectName(),
							"dailyAttendanceFormList[" + i + "].trainingStartTime", message));
				}

				// 退勤が24時を超えていたらエラー
				if (!trainingEndTime.isBlank() && trainingEndTime.compareTo(endOfDate) >= 0) {
					String message = messageUtil.getMessage(Constants.VALID_KEY_MAXVALBULK,
							new String[] { attendanceForm.getDispTrainingDate(), "終了時間",
									endOfDate.getFormattedString() });
					result.addError(new FieldError(result.getObjectName(),
							"dailyAttendanceFormList[" + i + "].trainingEndTime", message));
				}

				// 出勤時刻と退勤時刻が入力されていた場合、退勤時刻の方が出勤時刻より早かったらエラー
				if (!StringUtils.isBlank(attendanceForm.getTrainingStartTime())
						&& !StringUtils.isBlank(attendanceForm.getTrainingEndTime())) {
					if (trainingStartTime.compareTo(trainingEndTime) > 0) {
						String message = messageUtil.getMessage(
								Constants.VALID_KEY_ATTENDANCE_TRAININGTIMERANGEBULK,
								new String[] { attendanceForm.getDispTrainingDate() });
						result.addError(new FieldError(result.getObjectName(),
								"dailyAttendanceFormList[" + i + "].trainingEndTime", message));
					}
				}
			}
		}
	}

	/**
	 * Task.58 勤怠一括登録
	 * 
	 * @param bulkRegistForm
	 * @return 完了メッセージ
	 * @throws ParseException
	 */
	public String bulkUpdate(BulkRegistForm bulkRegistForm) throws ParseException {
		List<TCompanyAttendance> tCompanyAttendanceList = new ArrayList<>();

		// 登録用entityのListをformから作成
		for (DailyAttendanceForm attendanceForm : bulkRegistForm.getDailyAttendanceFormList()) {
			if (bulkRegistForm.getTargetIndex().equals(attendanceForm.getIndex())) {

				TCompanyAttendance tCompanyAttendance = tCompanyAttendanceMapper
						.findByCompanyAttendanceId(attendanceForm.getCompanyAttendanceId(),
								Constants.DB_FLG_FALSE);

				Date now = new Date();
				if (tCompanyAttendance == null) {
					tCompanyAttendance = new TCompanyAttendance();
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					tCompanyAttendance.setTrainingDate(sdf.parse(attendanceForm.getTrainingDate()));
					tCompanyAttendance.setDeleteFlg(Constants.DB_FLG_FALSE);
					tCompanyAttendance.setFirstCreateUser(loginUserDto.getLmsUserId());
					tCompanyAttendance.setFirstCreateDate(now);
				}
				tCompanyAttendance.setLmsUserId(Integer.parseInt(attendanceForm.getLmsUserId()));
				tCompanyAttendance.setAccountId(loginUserDto.getAccountId());
				tCompanyAttendance.setLastModifiedUser(loginUserDto.getLmsUserId());
				tCompanyAttendance.setLastModifiedDate(now);

				// 出勤時刻整形
				TrainingTime trainingStartTime = null;
				trainingStartTime = new TrainingTime(attendanceForm.getTrainingStartTime());
				tCompanyAttendance.setTrainingStartTime(trainingStartTime.getFormattedString());

				// 退勤時刻整形
				TrainingTime trainingEndTime = null;
				trainingEndTime = new TrainingTime(attendanceForm.getTrainingEndTime());
				tCompanyAttendance.setTrainingEndTime(trainingEndTime.getFormattedString());

				// 出勤・退勤どちらか入力されている時のみステータスを設定
				if (trainingStartTime != null || trainingEndTime != null) {
					AttendanceStatusEnum attendanceStatusEnum;
					if (attendanceForm.getIsAbsent() != null && attendanceForm.getIsAbsent()) {
						attendanceStatusEnum = AttendanceStatusEnum.ABSENT;
					} else {
						attendanceStatusEnum = attendanceUtil.getStatus(trainingStartTime,
								trainingEndTime);
					}
					tCompanyAttendance.setStatus(attendanceStatusEnum.code);
				}

				// 登録用Listへ追加
				tCompanyAttendanceList.add(tCompanyAttendance);
			}
			// 1件ずつ登録作業を行う。IDの有無でINSERT or UPDATEを判断
			for (TCompanyAttendance tCompanyAttendance : tCompanyAttendanceList) {
				if (tCompanyAttendance.getCompanyAttendanceId() == null) {
					tCompanyAttendanceMapper.insert(tCompanyAttendance);
				} else {
					tCompanyAttendanceMapper.update(tCompanyAttendance);
				}
			}
		}
		// 完了メッセージ
		return messageUtil.getMessage(Constants.PROP_KEY_ATTENDANCE_UPDATE_NOTICE);
	}

}
