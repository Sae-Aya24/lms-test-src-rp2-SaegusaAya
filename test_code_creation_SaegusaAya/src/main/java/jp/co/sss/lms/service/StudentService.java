package jp.co.sss.lms.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.AgreementConsentDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.PlaceAssignDto;
import jp.co.sss.lms.dto.StudentDto;
import jp.co.sss.lms.dto.SubsidyCategoryDto;
import jp.co.sss.lms.dto.UserDetailDto;
import jp.co.sss.lms.entity.MLmsUser;
import jp.co.sss.lms.entity.MUser;
import jp.co.sss.lms.entity.TCourseUser;
import jp.co.sss.lms.entity.TUserCompany;
import jp.co.sss.lms.entity.TUserPlace;
import jp.co.sss.lms.form.StudentRegistForm;
import jp.co.sss.lms.form.StudentUpdateForm;
import jp.co.sss.lms.mapper.MLmsUserMapper;
import jp.co.sss.lms.mapper.MUserMapper;
import jp.co.sss.lms.mapper.TAgreementConsentMapper;
import jp.co.sss.lms.mapper.TCourseUserMapper;
import jp.co.sss.lms.mapper.TPlaceAssignMapper;
import jp.co.sss.lms.mapper.TUserCompanyMapper;
import jp.co.sss.lms.mapper.TUserPlaceMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.PasswordUtil;

/**
 * 受講生情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class StudentService {

	@Autowired
	private SubsidyService subsidyService;
	@Autowired
	private TAgreementConsentMapper tAgreementConsentMapper;
	@Autowired
	private TPlaceAssignMapper tPlaceAssignMapper;
	@Autowired
	private MLmsUserMapper mLmsUserMapper;
	@Autowired
	private MUserMapper mUserMapper;
	@Autowired
	private TCourseUserMapper tCourseUserMapper;
	@Autowired
	private TUserCompanyMapper tUserCompanyMapper;
	@Autowired
	private TUserPlaceMapper tUserPlaceMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private PasswordUtil passwordUtil;

	/**
	 * Task.67 受講生登録フォームの設定
	 * 
	 * @param studentRegistForm
	 */
	public void setStudentRegistForm(StudentRegistForm studentRegistForm) {

		// 契約同意情報の取得
		AgreementConsentDto agreementConsentDto = tAgreementConsentMapper
				.getAgreementConsentDtoList(loginUserDto.getCompanyId(), new Date(),
						Constants.CODE_VAL_CONTRACT_AGREE, Constants.DB_FLG_FALSE)
				.get(0);

		studentRegistForm.setCompanyId(agreementConsentDto.getCompanyId());
		studentRegistForm.setCompanyCourseId(agreementConsentDto.getCompanyCourseId());
		studentRegistForm.setCourseId(agreementConsentDto.getCourseId());
		studentRegistForm.setCourseName(agreementConsentDto.getCourseName());

		// 助成金カテゴリ情報の取得
		List<SubsidyCategoryDto> subsidyCategoryDtoList = subsidyService.getSubsidyCategoryList();
		studentRegistForm.setSubsidyCategoryDtoList(subsidyCategoryDtoList);

		// 会場アサイン情報の取得
		List<PlaceAssignDto> placeAssignDtoList = tPlaceAssignMapper.getPlaceAssignDtoList(
				agreementConsentDto.getCompanyCourseId(), Constants.DB_FLG_FALSE);

		studentRegistForm.setStudentDtoList(new ArrayList<>());
		for (PlaceAssignDto placeAssignDto : placeAssignDtoList) {
			for (int i = 0; i < placeAssignDto.getApplicantAmount(); i++) {
				StudentDto studentDto = new StudentDto();
				studentDto.setPlaceId(placeAssignDto.getPlaceId());
				studentDto.setPlaceName(placeAssignDto.getPlaceName());
				studentRegistForm.getStudentDtoList().add(studentDto);
			}
		}
	}

	/**
	 * Task.67 受講生登録フォームの入力チェック
	 * 
	 * @param studentRegistForm
	 * @param result
	 */
	public void studentRegistInputCheck(StudentRegistForm studentRegistForm, BindingResult result) {
		for (int i = 0; i < studentRegistForm.getStudentDtoList().size(); i++) {
			StudentDto studentDto = studentRegistForm.getStudentDtoList().get(i);
			if (StringUtils.isBlank(studentDto.getUserName())) {
				result.addError(new FieldError(result.getObjectName(),
						"studentDtoList[" + i + "].userName", messageUtil.getMessage(
								Constants.VALID_KEY_REQUIRED, new String[] { "受講生名" })));
			}
			if (StringUtils.isBlank(studentDto.getKana())) {
				result.addError(new FieldError(result.getObjectName(),
						"studentDtoList[" + i + "].kana", messageUtil.getMessage(
								Constants.VALID_KEY_REQUIRED, new String[] { "よみがな" })));
			}
		}
	}

	/**
	 * Task.67 受講生の登録
	 * 
	 * @param studentRegistForm
	 * @return 完了メッセージ
	 */
	public String registStudent(StudentRegistForm studentRegistForm) {

		String loginIdPrefix = "companyCourse" + studentRegistForm.getCompanyCourseId();
		Integer studentSequence = 0;
		Date now = new Date();

		for (StudentDto studentDto : studentRegistForm.getStudentDtoList()) {
			MUser mUser = new MUser();
			String loginId = loginIdPrefix + "user" + (++studentSequence);
			String password = loginId;
			mUser.setLoginId(loginId);
			mUser.setPassword(passwordUtil.getSaltedAndStrechedPassword(password, loginId));
			mUser.setUserName(studentDto.getUserName());
			mUser.setAuthority(Constants.CODE_VAL_ROLL_STUDENT);
			mUser.setAccountId(1);
			mUser.setSecurityAgreeFlg(Constants.CODE_VAL_SECURITY_DISAGREE);
			mUser.setDeleteFlg(Constants.DB_FLG_FALSE);
			mUser.setFirstCreateUser(loginUserDto.getLmsUserId());
			mUser.setFirstCreateDate(now);
			mUser.setLastModifiedUser(loginUserDto.getLmsUserId());
			mUser.setLastModifiedDate(now);
			mUser.setKana(studentDto.getKana());
			mUser.setSubsidyCategoryId(studentDto.getSubsidyCategoryId());
			mUserMapper.insert(mUser);

			MLmsUser mLmsUser = new MLmsUser();
			mLmsUser.setUserId(mUser.getUserId());
			mLmsUser.setRole(Constants.CODE_VAL_ROLL_STUDENT);
			mLmsUser.setAdminFlg(Constants.ADMIN_FLG_FALSE);
			mLmsUser.setAccountId(1);
			mLmsUser.setDeleteFlg(Constants.DB_FLG_FALSE);
			mLmsUser.setFirstCreateUser(loginUserDto.getLmsUserId());
			mLmsUser.setFirstCreateDate(now);
			mLmsUser.setLastModifiedUser(loginUserDto.getLmsUserId());
			mLmsUser.setLastModifiedDate(now);
			mLmsUser.setHopeViaTraning(studentDto.getHopeViaTraning());
			mLmsUser.setProgrammingExperience(studentDto.getProgrammingExperience());
			mLmsUserMapper.insert(mLmsUser);

			TCourseUser tCourseUser = new TCourseUser();
			tCourseUser.setCourseId(studentRegistForm.getCourseId());
			tCourseUser.setLmsUserId(mLmsUser.getLmsUserId());
			tCourseUser.setAccountId(1);
			tCourseUser.setDeleteFlg(Constants.DB_FLG_FALSE);
			tCourseUser.setFirstCreateUser(loginUserDto.getLmsUserId());
			tCourseUser.setFirstCreateDate(now);
			tCourseUser.setLastModifiedUser(loginUserDto.getLmsUserId());
			tCourseUser.setLastModifiedDate(now);
			tCourseUserMapper.insert(tCourseUser);

			TUserCompany tUserCompany = new TUserCompany();
			tUserCompany.setLmsUserId(mLmsUser.getLmsUserId());
			tUserCompany.setCompanyId(studentRegistForm.getCompanyId());
			tUserCompany.setAccountId(1);
			tUserCompany.setDeleteFlg(Constants.DB_FLG_FALSE);
			tUserCompany.setFirstCreateUser(loginUserDto.getLmsUserId());
			tUserCompany.setFirstCreateDate(now);
			tUserCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
			tUserCompany.setLastModifiedDate(now);
			tUserCompanyMapper.insert(tUserCompany);

			TUserPlace tUserPlace = new TUserPlace();
			tUserPlace.setLmsUserId(mLmsUser.getLmsUserId());
			tUserPlace.setPlaceId(studentDto.getPlaceId());
			tUserPlace.setAccountId(1);
			tUserPlace.setDeleteFlg(Constants.DB_FLG_FALSE);
			tUserPlace.setFirstCreateUser(loginUserDto.getLmsUserId());
			tUserPlace.setFirstCreateDate(now);
			tUserPlace.setLastModifiedUser(loginUserDto.getLmsUserId());
			tUserPlace.setLastModifiedDate(now);
			tUserPlaceMapper.insert(tUserPlace);
		}

		return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE, new String[] { "受講生" });

	}

	/**
	 * Task.69 ユーザー詳細取得
	 * 
	 * @param studentUpdateForm
	 */
	public void getStudentUpdateForm(StudentUpdateForm studentUpdateForm) {

		UserDetailDto userDetailDto = mLmsUserMapper.getUserDetail(studentUpdateForm.getLmsUserId(),
				Constants.DB_FLG_FALSE);

		studentUpdateForm.setCompanyName(userDetailDto.getCompanyName());
		studentUpdateForm.setCourseId(userDetailDto.getCourseId());
		studentUpdateForm.setCourseName(userDetailDto.getCourseName());
		studentUpdateForm.setHopeViaTraning(userDetailDto.getHopeViaTraning());
		studentUpdateForm.setKana(userDetailDto.getKana());
		studentUpdateForm.setLmsUserId(userDetailDto.getLmsUserId());
		studentUpdateForm.setLoginId(userDetailDto.getLoginId());
		studentUpdateForm.setPlaceName(userDetailDto.getPlaceName());
		studentUpdateForm.setProgrammingExperience(userDetailDto.getProgrammingExperience());
		studentUpdateForm.setSubsidyCategoryId(userDetailDto.getSubsidyCategoryId());
		studentUpdateForm.setUserId(userDetailDto.getUserId());
		studentUpdateForm.setUserName(userDetailDto.getUserName());

	}

	/**
	 * Task.69 受講生の更新
	 * 
	 * @param studentUpdateForm
	 * @return 完了メッセージ
	 */
	public String updateStudent(StudentUpdateForm studentUpdateForm) {
		Date now = new Date();

		// ユーザーマスタの更新
		MUser mUser = new MUser();
		mUser.setUserId(studentUpdateForm.getUserId());
		mUser.setUserName(studentUpdateForm.getUserName());
		mUser.setLastModifiedUser(loginUserDto.getLmsUserId());
		mUser.setLastModifiedDate(now);
		mUser.setKana(studentUpdateForm.getKana());
		mUser.setSubsidyCategoryId(studentUpdateForm.getSubsidyCategoryId());
		mUserMapper.update(mUser);

		// LMSユーザーマスタの更新
		MLmsUser mLmsUser = new MLmsUser();
		mLmsUser.setLmsUserId(studentUpdateForm.getLmsUserId());
		mLmsUser.setLastModifiedUser(loginUserDto.getLmsUserId());
		mLmsUser.setLastModifiedDate(now);
		mLmsUser.setHopeViaTraning(studentUpdateForm.getHopeViaTraning());
		mLmsUser.setProgrammingExperience(studentUpdateForm.getProgrammingExperience());
		mLmsUserMapper.update(mLmsUser);

		return messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE, new String[] { "受講生" });
	}

}
