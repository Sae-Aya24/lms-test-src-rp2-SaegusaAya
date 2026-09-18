package jp.co.sss.lms.service;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.DailyReportDto;
import jp.co.sss.lms.dto.ExamDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.SectionServiceDailyReportDto;
import jp.co.sss.lms.dto.SectionServiceFileDto;
import jp.co.sss.lms.dto.SectionServiceSectionDto;
import jp.co.sss.lms.entity.MSection;
import jp.co.sss.lms.entity.TExamSection;
import jp.co.sss.lms.entity.TSectionDailyReport;
import jp.co.sss.lms.form.SectionDetailForm;
import jp.co.sss.lms.mapper.MDailyReportMapper;
import jp.co.sss.lms.mapper.MExamMapper;
import jp.co.sss.lms.mapper.MSectionMapper;
import jp.co.sss.lms.mapper.TCourseDailyReportMapper;
import jp.co.sss.lms.mapper.TExamSectionMapper;
import jp.co.sss.lms.mapper.TSectionDailyReportMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.LoggingUtil;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.PasswordUtil;
import jp.co.sss.lms.util.TrainingTime;

/**
 * セクション情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class SectionService {

	@Autowired
	private MSectionMapper mSectionMapper;
	@Autowired
	private TCourseDailyReportMapper tCourseDailyReportMapper;
	@Autowired
	private TSectionDailyReportMapper tSectionDailyReportMapper;
	@Autowired
	private TExamSectionMapper tExamSectionMapper;
	@Autowired
	private MExamMapper mExamMapper;
	@Autowired
	private MDailyReportMapper mDailyReportMapper;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private PasswordUtil passwordUtil;
	@Autowired
	private TrainingTime trainingTime;
	@Autowired
	private LoggingUtil loggingUtil;
	@Autowired
	private LoginUserUtil loginUserUtil;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * パラメータチェック
	 * 
	 * @param sectionId
	 * @return エラーメッセージ
	 */
	public String checkSectionId(Integer sectionId) {
		Integer count = mSectionMapper.getSectionCount(sectionId);
		if (count == 0) {
			String message = messageUtil.getMessage(Constants.VALID_KEY_ALREADYDELETE,
					new String[] { "セクションID " + sectionId });
			StringBuffer sb = new StringBuffer(message);
			loggingUtil.appendLog(sb);
			logger.info(sb.toString());
			return message;
		}
		return "";
	}

	/**
	 * セクション詳細情報の取得
	 * 
	 * @param sectionDetailForm
	 */
	public void getSectionDetail(SectionDetailForm sectionDetailForm) {
		// セクションサービスDTOの取得
		SectionServiceSectionDto sectionServiceSectionDto = mSectionMapper.getSectionDetail(
				sectionDetailForm.getSectionId(), loginUserDto.getAccountId(),
				loginUserDto.getLmsUserId(), Constants.DB_FLG_FALSE);
		BeanUtils.copyProperties(sectionServiceSectionDto, sectionDetailForm);
		// セクション・日報紐付け情報の取得
		List<SectionServiceDailyReportDto> sectionServiceDailyReportDtoList = tSectionDailyReportMapper
				.getSectionServiceDailyReportDto(sectionDetailForm.getSectionId(),
						loginUserDto.getLmsUserId(), Constants.DB_FLG_FALSE);
		for (SectionServiceDailyReportDto sectionServiceDailyReportDto : sectionServiceDailyReportDtoList) {
			sectionServiceSectionDto.getReportDtoList().add(sectionServiceDailyReportDto);
		}
		// ファイル情報の設定
		for (SectionServiceFileDto fileDto : sectionServiceSectionDto.getFileDtoList()) {
			String hashFileId = passwordUtil.getSaltedAndStrechedPassword(fileDto.getFileId(),
					loginUserDto.getUserId().toString());
			fileDto.setFileId(hashFileId);
		}
		sectionServiceSectionDto.setMaxFileSize(Constants.DELIVERABLES_UPLOAD_MAX_SIZE);

		// Task.116
		if (loginUserUtil.isAdmin()) {
			List<ExamDto> examList = mExamMapper.getExamList(Constants.DB_FLG_FALSE);
			sectionDetailForm.setExamList(examList);
			List<DailyReportDto> dailyReportList = mDailyReportMapper
					.getDailyReportDtoList(Constants.DB_HIDDEN_FLG_FALSE, Constants.DB_FLG_FALSE);
			sectionDetailForm.setDailyReportList(dailyReportList);
		}
	}

	/**
	 * Task.116 セクション詳細の入力チェック
	 * 
	 * @param sectionDetailForm
	 */
	public void sectionDescriptionInputCheck(SectionDetailForm sectionDetailForm,
			BindingResult result) {
		if (sectionDetailForm.getSectionDescription().length() > 300) {
			result.addError(new FieldError(result.getObjectName(), "sectionDescription",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
							new String[] { "セクション詳細", "300" })));
		}
	}

	/**
	 * Task.116 セクション詳細の更新
	 * 
	 * @param sectionDetailForm
	 */
	public void sectionDescriptionUpdate(SectionDetailForm sectionDetailForm) {
		Date today = new Date();
		MSection mSection = new MSection();
		BeanUtils.copyProperties(sectionDetailForm, mSection);
		mSection.setDeleteFlg(Constants.DB_FLG_FALSE);
		mSection.setLastModifiedUser(loginUserDto.getLmsUserId());
		mSection.setLastModifiedDate(today);
		mSectionMapper.update(mSection);
		String message = messageUtil.getMessage(Constants.PROP_KEY_UPDATE_COMPLETE,
				new String[] { "セクション詳細" });
		sectionDetailForm.setMessage(message);
	}

	/**
	 * Task.116 セクション試験情報の入力チェック
	 * 
	 * @param sectionDetailForm
	 */
	public void sectionExamInputCheck(SectionDetailForm sectionDetailForm, BindingResult result) {
		if (sectionDetailForm.getExamId() == null) {
			result.addError(new FieldError(result.getObjectName(), "examId", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED_SELECT, new String[] { "試験名" })));
		}
		if (sectionDetailForm.getPublicDate() == "") {
			result.addError(new FieldError(result.getObjectName(), "publicDate", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_REQUIRED, new String[] { "公開時刻" })));
		} else if (!trainingTime.isValidTrainingTime(sectionDetailForm.getPublicDate())) {
			result.addError(new FieldError(result.getObjectName(), "publicDate", "* " + messageUtil
					.getMessage(Constants.VALID_KEY_TRAININGTIME, new String[] { "公開時刻" })));
		}
	}

	/**
	 * Task.116 セクション試験情報の登録
	 * 
	 * @param sectionDetailForm
	 * @throws ParseException
	 */
	public void sectionExamRegist(SectionDetailForm sectionDetailForm) throws ParseException {
		// 公開時刻を日付に変換
		TrainingTime trainingTime = new TrainingTime(sectionDetailForm.getPublicDate());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(sectionDetailForm.getDate());
		calendar.add(Calendar.HOUR, trainingTime.getHour());
		calendar.add(Calendar.MINUTE, trainingTime.getMinute());
		Date publicDate = calendar.getTime();
		// エンティティ準備
		TExamSection tExamSection = new TExamSection();
		Date today = new Date();
		tExamSection.setExamId(sectionDetailForm.getExamId());
		tExamSection.setSectionId(sectionDetailForm.getSectionId());
		tExamSection.setPublicDate(publicDate);
		tExamSection.setAccountId(loginUserDto.getAccountId());
		tExamSection.setDeleteFlg(Constants.DB_FLG_FALSE);
		tExamSection.setFirstCreateUser(loginUserDto.getLmsUserId());
		tExamSection.setFirstCreateDate(today);
		tExamSection.setLastModifiedUser(loginUserDto.getLmsUserId());
		tExamSection.setLastModifiedDate(today);
		// 登録済みか確認
		Integer sectionExamCount = tExamSectionMapper.getSectionExamCount(
				sectionDetailForm.getSectionId(), sectionDetailForm.getExamId(),
				Constants.DB_FLG_FALSE);
		if (sectionExamCount == 0) {
			// 登録
			tExamSectionMapper.insert(tExamSection);
		} else {
			// 更新
			tExamSectionMapper.update(tExamSection);
		}
		// フォームの値をクリア
		sectionDetailForm.setExamId(null);
		sectionDetailForm.setPublicDate(null);
		// 完了メッセージ
		String message = messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
				new String[] { "試験" });
		sectionDetailForm.setMessage(message);
	}

	/**
	 * Task.116 セクション試験情報の削除
	 * 
	 * @param sectionDetailForm
	 */
	public void sectionExamDelete(SectionDetailForm sectionDetailForm) {
		TExamSection tExamSection = new TExamSection();
		tExamSection.setExamId(sectionDetailForm.getExamId());
		tExamSection.setSectionId(sectionDetailForm.getSectionId());
		tExamSectionMapper.delete(tExamSection);
		sectionDetailForm.setExamId(null);
		String message = messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { "試験" });
		sectionDetailForm.setMessage(message);
	}

	/**
	 * Task.116 セクションレポート情報の入力チェック
	 * 
	 * @param sectionDetailForm
	 */
	public void sectionReportInputCheck(SectionDetailForm sectionDetailForm, BindingResult result) {
		if (sectionDetailForm.getDailyReportId() == null) {
			result.addError(new FieldError(result.getObjectName(), "dailyReportId",
					"* " + messageUtil.getMessage(Constants.VALID_KEY_REQUIRED_SELECT,
							new String[] { "レポート名" })));
		}
	}

	/**
	 * Task.116 セクションレポート情報の登録
	 * 
	 * @param sectionDetailForm
	 * @throws ParseException
	 */
	public void sectionReportRegist(SectionDetailForm sectionDetailForm) {
		// コースに登録済みの件数
		Integer courseReportCount = tCourseDailyReportMapper.getCourseReportCount(
				sectionDetailForm.getCourseId(), sectionDetailForm.getDailyReportId(),
				Constants.DB_FLG_FALSE);
		// セクションに登録済みの件数
		Integer sectionReportCount = tSectionDailyReportMapper.getSectionReportCount(
				sectionDetailForm.getSectionId(), sectionDetailForm.getDailyReportId(),
				Constants.DB_FLG_FALSE);
		if (courseReportCount == 0 && sectionReportCount == 0) {
			// 登録
			TSectionDailyReport tSectionDailyReport = new TSectionDailyReport();
			Date today = new Date();
			tSectionDailyReport.setSectionId(sectionDetailForm.getSectionId());
			tSectionDailyReport.setDailyReportId(sectionDetailForm.getDailyReportId());
			tSectionDailyReport.setAccountId(loginUserDto.getAccountId());
			tSectionDailyReport.setDeleteFlg(Constants.DB_FLG_FALSE);
			tSectionDailyReport.setFirstCreateUser(loginUserDto.getLmsUserId());
			tSectionDailyReport.setFirstCreateDate(today);
			tSectionDailyReport.setLastModifiedUser(loginUserDto.getLmsUserId());
			tSectionDailyReport.setLastModifiedDate(today);
			tSectionDailyReportMapper.insert(tSectionDailyReport);
		}
		// フォームの値をクリア
		sectionDetailForm.setDailyReportId(null);
		// 完了メッセージ
		String message = messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
				new String[] { "レポート" });
		sectionDetailForm.setMessage(message);
	}

	/**
	 * Task.116 セクションレポート情報の削除
	 * 
	 * @param sectionDetailForm
	 */
	public void sectionReportDelete(SectionDetailForm sectionDetailForm) {
		TSectionDailyReport tSectionDailyReport = new TSectionDailyReport();
		tSectionDailyReport.setSectionId(sectionDetailForm.getSectionId());
		tSectionDailyReport.setDailyReportId(sectionDetailForm.getDailyReportId());
		tSectionDailyReportMapper.delete(tSectionDailyReport);
		sectionDetailForm.setDailyReportId(null);
		String message = messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { "レポート" });
		sectionDetailForm.setMessage(message);
	}

}
