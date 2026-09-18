package jp.co.sss.lms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.lms.dto.CompanyDto;
import jp.co.sss.lms.dto.CourseDto;
import jp.co.sss.lms.dto.DailyReportDto;
import jp.co.sss.lms.dto.ExamResultDto;
import jp.co.sss.lms.dto.LmsUserDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.MeetingDto;
import jp.co.sss.lms.dto.MyAccountDto;
import jp.co.sss.lms.dto.PlaceDto;
import jp.co.sss.lms.dto.UserDetailDto;
import jp.co.sss.lms.entity.MLmsUser;
import jp.co.sss.lms.entity.MUser;
import jp.co.sss.lms.entity.TCourseUser;
import jp.co.sss.lms.entity.TUserCompany;
import jp.co.sss.lms.entity.TUserFssUser;
import jp.co.sss.lms.entity.TUserPlace;
import jp.co.sss.lms.enums.LmsUserRoleEnum;
import jp.co.sss.lms.form.UserListForm;
import jp.co.sss.lms.mapper.MLmsUserMapper;
import jp.co.sss.lms.mapper.MUserMapper;
import jp.co.sss.lms.mapper.TCourseUserMapper;
import jp.co.sss.lms.mapper.TDailyReportSubmitMapper;
import jp.co.sss.lms.mapper.TExamResultMapper;
import jp.co.sss.lms.mapper.TMeetingMapper;
import jp.co.sss.lms.mapper.TUserCompanyMapper;
import jp.co.sss.lms.mapper.TUserFssUserMapper;
import jp.co.sss.lms.mapper.TUserPlaceMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.StringUtil;

/**
 * ユーザー情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class UserService {

	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private HttpSession session;
	@Autowired
	private MLmsUserMapper mLmsUserMapper;
	@Autowired
	private MUserMapper mUserMapper;
	@Autowired
	private CourseService courseService;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private TDailyReportSubmitMapper tDailyReportSubmitMapper;
	@Autowired
	private TExamResultMapper tExamResultMapper;
	@Autowired
	private TUserPlaceMapper tUserPlaceMapper;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private TUserFssUserMapper tUserFssUserMapper;
	@Autowired
	private TCourseUserMapper tCourseUserMapper;
	@Autowired
	private TUserCompanyMapper tUserCompanyMapper;
	@Autowired
	private TMeetingMapper tMeetingMapper;

	/**
	 * セキュリティ同意フラグ登録
	 */
	public void updateSecurityFlg() {
		Date today = new Date();
		MUser mUser = mUserMapper.findByUserId(loginUserDto.getUserId(), Constants.DB_FLG_FALSE);
		mUser.setSecurityAgreeFlg(Constants.CODE_VAL_SECURITY_AGREE);
		mUser.setLastModifiedUser(loginUserDto.getUserId());
		mUser.setLastModifiedDate(today);
		boolean updateFlg = mUserMapper.updateSecrityFlg(mUser);
		if (updateFlg) {
			loginUserDto.setSecurityAgreeFlg(Constants.CODE_VAL_SECURITY_AGREE);
			session.setAttribute("loginUserDto", loginUserDto);
		}
	}

	/**
	 * ユーザー詳細DTOの取得
	 * 
	 * @return ユーザー詳細DTO
	 */
	public LmsUserDto getUserDetail(Integer lmsUserId) {

		lmsUserId = loginUserUtil.isStudent() ? loginUserDto.getLmsUserId() : lmsUserId;
		LmsUserDto lmsUserDto = new LmsUserDto();

		UserDetailDto userDetailDto = mLmsUserMapper.getUserDetail(lmsUserId,
				Constants.DB_FLG_FALSE);
		lmsUserDto.setUserDetailDto(userDetailDto);

		List<ExamResultDto> examResultDtoList = tExamResultMapper.getExamResultDto(lmsUserId,
				loginUserDto.getAccountId(), Constants.DB_FLG_FALSE);
		lmsUserDto.setExamResultDtoList(examResultDtoList);

		List<DailyReportDto> dailyReportDtoList = tDailyReportSubmitMapper
				.getDailyReportSubmitList(lmsUserId, Constants.DB_FLG_FALSE);

		// Task.21
		for (DailyReportDto dailyReportDto : dailyReportDtoList) {
			if (dailyReportDto.getLastFeedbackDate() != null) {
				Calendar calendar = Calendar.getInstance();
				calendar.setTime(dailyReportDto.getLastFeedbackDate());
				calendar.add(Calendar.DAY_OF_MONTH, 7);
				Calendar now = Calendar.getInstance();
				if (calendar.compareTo(now) >= 0) {
					dailyReportDto.setFbNewFlg(true);
				} else {
					dailyReportDto.setFbNewFlg(false);
				}
			} else {
				dailyReportDto.setFbNewFlg(false);
			}
		}
		lmsUserDto.setDailyReportDtoList(dailyReportDtoList);

		// Task.46
		if (loginUserUtil.isTeacher() || loginUserUtil.isAdmin()) {
			List<MeetingDto> meetingDtoList = tMeetingMapper.getMeetingDtoList(lmsUserId,
					Constants.DB_FLG_FALSE);
			lmsUserDto.setMeetingDtoList(meetingDtoList);
		}

		return lmsUserDto;
	}

	/**
	 * Task.43 ユーザー検索フォームの設定
	 * 
	 * @param userListForm
	 */
	public void setUserListForm(UserListForm userListForm) {

		// コースDTOリスト取得
		List<CourseDto> courseDtoList = courseService.getCourseDtoList();
		userListForm.setCourseDtoList(courseDtoList);

		// 会場DTO取得
		if (loginUserUtil.isAdmin()) {
			// Task.79
			List<PlaceDto> placeDtoList = placeService.getPlaceDtoList(null);
			userListForm.setPlaceDtoList(placeDtoList);
		} else {
			PlaceDto placeDto = placeService.getPlaceDto();
			userListForm.setPlaceId(placeDto.getPlaceId());
			userListForm.setPlaceName(placeDto.getPlaceName());
			userListForm.setPlaceNote(placeDto.getPlaceNote());
		}

		// 企業DTOリスト取得
		List<CompanyDto> companyDtoList = companyService.getCompanyDtoList();
		userListForm.setCompanyDtoList(companyDtoList);

		// Task.79 権限Enum取得
		if (loginUserUtil.isAdmin()) {
			userListForm.setRoleEnum(LmsUserRoleEnum.values());
		}
		// Task.79 経過期間ラベル設定
		userListForm.setPastTimeLabel(messageUtil.getMessage("setting.search.pastTimeLabel"));

	}

	/**
	 * Task.43 ユーザー基本情報DTOリスト取得
	 * 
	 * @param userSearchForm
	 * @return ユーザー基本情報DTOリスト
	 * @throws ParseException
	 */
	public List<UserDetailDto> getUserDetailDtoList(UserListForm userListForm)
			throws ParseException {
		// 会場IDリストを取得
		List<Integer> placeIdList = new ArrayList<>();
		if (loginUserUtil.isAdmin()) {
			// Task.79
			if (userListForm.getPlaceId() != null) {
				placeIdList.add(userListForm.getPlaceId());
			}
		} else {
			placeIdList = tUserPlaceMapper.getPlaceId(loginUserDto.getLmsUserId(),
					Constants.DB_FLG_FALSE);
		}
		// リクエストパラメータの設定
		String role = loginUserUtil.isAdmin() ? userListForm.getRole()
				: Constants.CODE_VAL_ROLL_STUDENT;
		// Task.79
		Short leaveFlg = loginUserUtil.isAdmin() ? userListForm.getLeaveFlg() : null;
		Date pastDate = null;
		if (!loginUserUtil.isAdmin()
				|| (Objects.isNull(userListForm.getPastFlg()) || userListForm.getPastFlg() != 1)) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(sdf.format(date));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Integer pastTime = Integer.parseInt(messageUtil.getMessage("setting.search.pastTime"));
			calendar.add(Calendar.MONTH, -pastTime);
			pastDate = calendar.getTime();
		}
		return mLmsUserMapper.getUserDetailForSearch(userListForm.getCourseName(),
				userListForm.getCompanyName(), userListForm.getUserName(), placeIdList, role,
				leaveFlg, pastDate, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.43 ユーザー検索フォームの入力チェック
	 * 
	 * @param userSearchForm
	 * @param result
	 */
	public void searchInputCheck(UserListForm userListForm, BindingResult result) {
		if (userListForm.getCompanyName() != null && 1 < StringUtil.separatorCount(
				Constants.SEPARATE_ADDRESS_JUDGE_CHARACTER.charAt(0),
				userListForm.getCompanyName())) {
			result.addError(new FieldError(result.getObjectName(), "companyName",
					messageUtil.getMessage(Constants.VALID_KEY_SEARCHTOCOMPANYNAMEERROR)));
		}
	}

	/**
	 * Task.48 マイアカウント情報取得
	 * 
	 * @return マイアカウントDTO
	 */
	public MyAccountDto getMyAccountDto() {

		// Task.48
//		MPlace mPlace = mPlaceMapper.findByPlaceId(loginUserDto.getPlaceId(),
//				Constants.DB_HIDDEN_FLG_FALSE, Constants.DB_FLG_FALSE);
//		String role = LmsUserRoleEnum.getEnum(loginUserDto.getRole()).value;
//		MyAccountDto myAccountDto = new MyAccountDto();
//		myAccountDto.setUserName(loginUserDto.getUserName());
//		myAccountDto.setRole(loginUserDto.getRole());
//		myAccountDto.setRoleName(role);
//		myAccountDto.setCourseName(loginUserDto.getCourseName());
//		myAccountDto.setPlaceName(mPlace.getPlaceName());
//		myAccountDto.setPlaceNote(mPlace.getPlaceNote());

		// Task.49
		UserDetailDto userDetailDto = mLmsUserMapper.getUserDetail(loginUserDto.getLmsUserId(),
				Constants.DB_FLG_FALSE);
		MyAccountDto myAccountDto = new MyAccountDto();
		BeanUtils.copyProperties(userDetailDto, myAccountDto);
		String role = LmsUserRoleEnum.getEnum(userDetailDto.getRole()).value;
		myAccountDto.setRoleName(role);
		return myAccountDto;
	}

	/**
	 * Task.68 ユーザー一覧(受講生一覧) 企業担当者用受講生情報取得
	 * 
	 * @param userListForm
	 * @exception ParseException
	 * @return ユーザー基本情報DTOリスト
	 */
	public List<UserDetailDto> getStudentUserDetailDtoList(UserListForm userListForm)
			throws ParseException {

		// コース終了後 6ヵ月 を経過した受講生フラグ
		Date pastDate = null;
		if (loginUserUtil.isCompany()
				&& (Objects.isNull(userListForm.getPastFlg()) || userListForm.getPastFlg() != 1)) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(sdf.format(date));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Integer pastTime = Integer.parseInt(messageUtil.getMessage("setting.search.pastTime"));
			calendar.add(Calendar.MONTH, -pastTime);
			pastDate = calendar.getTime();
		}
		return mLmsUserMapper.getStudentUserDetailForSearch(loginUserDto.getCompanyName(),
				userListForm.getUserName(), Constants.CODE_VAL_ROLL_STUDENT, pastDate,
				Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.70 ユーザー一覧(受講生以外のユーザー) 企業担当者用受講生以外情報取得
	 * 
	 * @exception ParseException
	 * @return ユーザー基本情報DTOリスト
	 */
	public List<UserDetailDto> getCompanyUserDetailDtoList() throws ParseException {
		return mLmsUserMapper.getCompanyUserDetailForSearch(loginUserDto.getCompanyName(),
				Constants.CODE_VAL_ROLL_STUDENT, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.70 ユーザー一覧(受講生以外のユーザー) ユーザー削除
	 * 
	 * @param userListForm
	 * @return 完了メッセージ
	 */
	public String companyUserDelete(UserListForm userListForm) {
		Date date = new Date();
		// ユーザ・共有ユーザ紐付けテーブル
		TUserFssUser tUserFssUser = new TUserFssUser();
		tUserFssUser.setUserId(userListForm.getUserId());
		tUserFssUser.setDeleteFlg(Constants.DB_FLG_TRUE);
		tUserFssUser.setLastModifiedUser(loginUserDto.getLmsUserId());
		tUserFssUser.setLastModifiedDate(date);
		tUserFssUserMapper.updateDeleteFlg(tUserFssUser);
		// ユーザー・企業紐付けテーブル
		TUserCompany tUserCompany = new TUserCompany();
		tUserCompany.setLmsUserId(userListForm.getLmsUserId());
		tUserCompany.setDeleteFlg(Constants.DB_FLG_TRUE);
		tUserCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
		tUserCompany.setLastModifiedDate(date);
		tUserCompanyMapper.updateDeleteFlg(tUserCompany);
		// ユーザーマスタ
		MUser mUser = new MUser();
		mUser.setUserId(userListForm.getUserId());
		mUser.setDeleteFlg(Constants.DB_FLG_TRUE);
		mUser.setLastModifiedUser(loginUserDto.getLmsUserId());
		mUser.setLastModifiedDate(date);
		mUserMapper.updateDeleteFlg(mUser);
		// LMSユーザーマスタ
		MLmsUser mLmsUser = new MLmsUser();
		mLmsUser.setLmsUserId(userListForm.getLmsUserId());
		mLmsUser.setDeleteFlg(Constants.DB_FLG_TRUE);
		mLmsUser.setLastModifiedUser(loginUserDto.getLmsUserId());
		mLmsUser.setLastModifiedDate(date);
		mLmsUserMapper.updateDeleteFlg(mLmsUser);
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { "ユーザー" });
	}

	/**
	 * Task.79 ユーザー一括削除
	 * 
	 * @param lmsUserIdList
	 * @return 完了メッセージ
	 */
	public String bulkDelete(List<Integer> lmsUserIdList) {

		for (Integer lmsUserId : lmsUserIdList) {
			MLmsUser mLmsUser = mLmsUserMapper.findByLmsUserId(lmsUserId, Constants.DB_FLG_FALSE);
			Date date = new Date();
			// ユーザ・共有ユーザ紐付けテーブル
			TUserFssUser tUserFssUser = new TUserFssUser();
			tUserFssUser.setUserId(mLmsUser.getUserId());
			tUserFssUser.setDeleteFlg(Constants.DB_FLG_TRUE);
			tUserFssUser.setLastModifiedUser(loginUserDto.getLmsUserId());
			tUserFssUser.setLastModifiedDate(date);
			tUserFssUserMapper.updateDeleteFlg(tUserFssUser);
			// コース・ユーザー紐付けテーブル
			TCourseUser tCourseUser = new TCourseUser();
			tCourseUser.setLmsUserId(lmsUserId);
			tCourseUser.setDeleteFlg(Constants.DB_FLG_TRUE);
			tCourseUser.setLastModifiedUser(loginUserDto.getLmsUserId());
			tCourseUser.setLastModifiedDate(date);
			tCourseUserMapper.updateDeleteFlg(tCourseUser);
			// ユーザー・企業紐付けテーブル
			TUserCompany tUserCompany = new TUserCompany();
			tUserCompany.setLmsUserId(lmsUserId);
			tUserCompany.setDeleteFlg(Constants.DB_FLG_TRUE);
			tUserCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
			tUserCompany.setLastModifiedDate(date);
			tUserCompanyMapper.updateDeleteFlg(tUserCompany);
			// ユーザー・会場紐付けテーブル
			TUserPlace tUserPlace = new TUserPlace();
			tUserPlace.setLmsUserId(lmsUserId);
			tUserPlace.setDeleteFlg(Constants.DB_FLG_TRUE);
			tUserPlace.setLastModifiedUser(loginUserDto.getLmsUserId());
			tUserPlace.setLastModifiedDate(date);
			tUserPlaceMapper.updateDeleteFlg(tUserPlace);
			// ユーザーマスタ
			MUser mUser = new MUser();
			mUser.setUserId(mLmsUser.getUserId());
			mUser.setDeleteFlg(Constants.DB_FLG_TRUE);
			mUser.setLastModifiedUser(loginUserDto.getLmsUserId());
			mUser.setLastModifiedDate(date);
			mUserMapper.updateDeleteFlg(mUser);
			// LMSユーザーマスタ
			mLmsUser.setDeleteFlg(Constants.DB_FLG_TRUE);
			mLmsUser.setLastModifiedUser(loginUserDto.getLmsUserId());
			mLmsUser.setLastModifiedDate(date);
			mLmsUserMapper.updateDeleteFlg(mLmsUser);
		}
		return messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE, new String[] { "ユーザー" });
	}

}
