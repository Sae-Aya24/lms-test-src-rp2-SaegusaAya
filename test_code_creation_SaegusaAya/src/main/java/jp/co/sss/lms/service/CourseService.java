package jp.co.sss.lms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.CourseDto;
import jp.co.sss.lms.dto.CourseReportDto;
import jp.co.sss.lms.dto.CourseServiceCategoryDto;
import jp.co.sss.lms.dto.CourseServiceCourseDto;
import jp.co.sss.lms.dto.CourseServiceSectionDto;
import jp.co.sss.lms.dto.CourseWithTeachingMaterialCountDto;
import jp.co.sss.lms.dto.EvReportDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.MCourse;
import jp.co.sss.lms.entity.MHoliday;
import jp.co.sss.lms.entity.MSection;
import jp.co.sss.lms.entity.TCourseDailyReport;
import jp.co.sss.lms.entity.TCourseMeeting;
import jp.co.sss.lms.entity.TEvCourse;
import jp.co.sss.lms.form.CourseDetailRegistForm;
import jp.co.sss.lms.form.CourseRegistForm;
import jp.co.sss.lms.mapper.MCategoryMapper;
import jp.co.sss.lms.mapper.MCourseMapper;
import jp.co.sss.lms.mapper.MHolidayMapper;
import jp.co.sss.lms.mapper.MSectionMapper;
import jp.co.sss.lms.mapper.TCourseDailyReportMapper;
import jp.co.sss.lms.mapper.TCourseMeetingMapper;
import jp.co.sss.lms.mapper.TEvCourseMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.LoggingUtil;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.PasswordUtil;

/**
 * コース情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class CourseService {

	@Autowired
	private MCourseMapper mCourseMapper;
	@Autowired
	private TCourseDailyReportMapper tCourseDailyReportMapper;
	@Autowired
	private TEvCourseMapper tEvCourseMapper;
	@Autowired
	private TCourseMeetingMapper tCourseMeetingMapper;
	@Autowired
	private MHolidayMapper mHolidayMapper;
	@Autowired
	private MCategoryMapper mCategoryMapper;
	@Autowired
	private MSectionMapper mSectionMapper;
	@Autowired
	private ReportService reportService;
	@Autowired
	private EvReportService evReportService;
	@Autowired
	private MeetingService meetingService;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private PasswordUtil passwordUtil;
	@Autowired
	private LoggingUtil loggingUtil;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * パラメータチェック
	 * 
	 * @param courseId
	 * @return エラーメッセージ
	 */
	public String checkCourseId(Integer courseId) {
		Integer courseCount = mCourseMapper.getCourseCount(courseId);
		if (courseCount == 0) {
			String message = messageUtil.getMessage(Constants.VALID_KEY_ALREADYDELETE,
					new String[] { "コースID " + courseId });
			StringBuffer sb = new StringBuffer(message);
			loggingUtil.appendLog(sb);
			logger.info(sb.toString());
			return message;
		}
		return "";
	}

	/**
	 * コース情報サービス コースDTOの取得
	 * 
	 * @param courseId
	 * @return courseServiceCourseDto
	 * @throws ParseException
	 */
	public CourseServiceCourseDto getCourseDetail(Integer courseId) throws ParseException {

		// Task.15 引数にLmsUserIdを追加
		CourseServiceCourseDto courseServiceCourseDto = mCourseMapper.getCourseDetail(courseId,
				loginUserDto.getLmsUserId(), Constants.DB_FLG_FALSE);

		// Task.14 ①当日の強調表示
		Date now = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Date nowFormat = sdf.parse(sdf.format(now));
		for (CourseServiceCategoryDto courseServiceCategoryDto : courseServiceCourseDto
				.getCourseServiceCategoryDtoList()) {
			for (CourseServiceSectionDto courseServiceSectionDto : courseServiceCategoryDto
					.getCourseServiceSectionDtoList()) {
				if (nowFormat.compareTo(courseServiceSectionDto.getDate()) == 0) {
					courseServiceSectionDto.setTodayFlg(true);
				} else {
					courseServiceSectionDto.setTodayFlg(false);
				}
			}
		}

		return courseServiceCourseDto;

	}

	/**
	 * Task.42 コース一覧情報取得
	 * 
	 * @return コース一覧情報
	 */
	public List<CourseWithTeachingMaterialCountDto> getCourseList() {

		List<CourseWithTeachingMaterialCountDto> courseWithTeachingMaterialCountDtoList = mCourseMapper
				.getCourseWithTeachingMaterialCountDtoList(loginUserDto.getAccountId(),
						Constants.DB_FLG_FALSE, Constants.DB_HIDDEN_FLG_FALSE);

		for (CourseWithTeachingMaterialCountDto courseWithTeachingMaterialCountDto : courseWithTeachingMaterialCountDtoList) {
			if (courseWithTeachingMaterialCountDto.getOpenTime() != null
					&& courseWithTeachingMaterialCountDto.getCloseTime() != null) {
				Calendar now = Calendar.getInstance();
				// 時分秒ミリ秒に0をセット
				now.set(Calendar.HOUR_OF_DAY, 0);
				now.set(Calendar.MINUTE, 0);
				now.set(Calendar.SECOND, 0);
				now.set(Calendar.MILLISECOND, 0);

				Calendar openDate = Calendar.getInstance();
				openDate.setTime(courseWithTeachingMaterialCountDto.getOpenTime());

				Calendar closeDate = Calendar.getInstance();
				closeDate.setTime(courseWithTeachingMaterialCountDto.getCloseTime());

				boolean isAlreadyStart = now.after(openDate) || now.equals(openDate);
				boolean isNotOver = now.before(closeDate) || now.equals(closeDate);

				courseWithTeachingMaterialCountDto.setOpenCourseFlg(isAlreadyStart && isNotOver);
			}
		}

		return courseWithTeachingMaterialCountDtoList;
	}

	/**
	 * Task.43 コースDTOリストの取得
	 * 
	 * @return コースDTOリスト
	 */
	// 講師・管理者画面で汎用的に使われる
	public List<CourseDto> getCourseDtoList() {
		return mCourseMapper.getCourseDtoList(Constants.DB_HIDDEN_FLG_FALSE,
				Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.114 コース登録フォームの初期化
	 * 
	 * @param courseRegistForm
	 */
	public void setCourseRegistForm(CourseRegistForm courseRegistForm) {
		// セレクトボックスリスト取得
		courseRegistForm.setDailyReportDtoList(reportService.getDailyReportDtoList());
		courseRegistForm.setEvReportDtoList(evReportService.getEvReportDtoList(null));
		courseRegistForm.setMeetingFileDtoList(meetingService.getMeetingFileDtoList(null, null));
		// 評価レポートID配列の初期化
		List<EvReportDto> evReportDtoList = courseRegistForm.getEvReportDtoList();
		courseRegistForm.setEvReportIdArray(new Integer[evReportDtoList.size()]);
		for (int i = 0; i < courseRegistForm.getEvReportIdArray().length; i++) {
			courseRegistForm.getEvReportIdArray()[i] = 0;
		}
	}

	/**
	 * Task.114 コースレポート情報の取得
	 * 
	 * @param courseRegistForm
	 */
	public void getCourseReportDto(CourseRegistForm courseRegistForm) {
		List<CourseReportDto> courseReportDtoList = mCourseMapper.getCourseReportDtoList(
				courseRegistForm.getCourseId(), Constants.DB_HIDDEN_FLG_FALSE,
				Constants.DB_FLG_FALSE);
		// フォーム内の評価レポートID以外の値は先頭の1件からコピー
		BeanUtils.copyProperties(courseReportDtoList.get(0), courseRegistForm);
		// テキストボックス表示のため日付フォーマット変更
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		courseRegistForm.setOpenTime(sdf.format(courseReportDtoList.get(0).getOpenTime()));
		courseRegistForm.setCloseTime(sdf.format(courseReportDtoList.get(0).getCloseTime()));
		// 評価レポートID配列を準備
		List<EvReportDto> evReportDtoList = courseRegistForm.getEvReportDtoList();
		for (int i = 0; i < evReportDtoList.size(); i++) {
			EvReportDto evReportDto = evReportDtoList.get(i);
			for (CourseReportDto courseReportDto : courseReportDtoList) {
				if (courseReportDto.getEvReportId() == evReportDto.getEvReportId()) {
					courseRegistForm.getEvReportIdArray()[i] = evReportDto.getEvReportId();
				}
			}
		}
	}

	/**
	 * Task.114 コースレポート登録フォームの入力チェック
	 * 
	 * @param courseRegistForm
	 * @param result
	 * @throws ParseException
	 */
	public void courseRegistFormInputCheck(CourseRegistForm courseRegistForm, BindingResult result)
			throws ParseException {
		// フォームバリデーションでエラーがある場合はチェックを行わない
		if (result.hasErrors()) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date openTime = sdf.parse(courseRegistForm.getOpenTime());
		Date closeTime = sdf.parse(courseRegistForm.getCloseTime());
		if (openTime.compareTo(closeTime) == 1) {
			result.addError(new FieldError(result.getObjectName(), "openTime", messageUtil
					.getMessage(Constants.VALID_KEY_BEFORECLOSE, new String[] { "開始日", "終了日" })));
		}
	}

	/**
	 * Task.114 コースレポート情報の登録
	 * 
	 * @param courseRegistForm
	 * @throws ParseException
	 */
	public Integer registCourseReport(CourseRegistForm courseRegistForm) throws ParseException {
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		MCourse mCourse = new MCourse();
		// コースマスタ
		if (courseRegistForm.getCourseId() == null) {
			// 登録
			BeanUtils.copyProperties(courseRegistForm, mCourse, "courseId");
			mCourse.setOpenTime(sdf.parse(courseRegistForm.getOpenTime()));
			mCourse.setCloseTime(sdf.parse(courseRegistForm.getCloseTime()));
			mCourse.setCourseType(Constants.CODE_VAL_COURSE_TYPE_NORMAL);
			mCourse.setAccountId(loginUserDto.getAccountId());
			mCourse.setDeleteFlg(Constants.DB_FLG_FALSE);
			mCourse.setFirstCreateUser(loginUserDto.getLmsUserId());
			mCourse.setFirstCreateDate(today);
			mCourse.setLastModifiedUser(loginUserDto.getLmsUserId());
			mCourse.setLastModifiedDate(today);
			mCourse.setPassword(passwordUtil.generatePassword());
			mCourse.setHiddenFlg(Constants.DB_HIDDEN_FLG_FALSE);
			mCourseMapper.insert(mCourse);
		} else {
			// 変更
			BeanUtils.copyProperties(courseRegistForm, mCourse);
			mCourse.setOpenTime(sdf.parse(courseRegistForm.getOpenTime()));
			mCourse.setCloseTime(sdf.parse(courseRegistForm.getCloseTime()));
			mCourse.setLastModifiedUser(loginUserDto.getLmsUserId());
			mCourse.setLastModifiedDate(today);
			mCourseMapper.update(mCourse);
			// トランザクション表は一旦削除
			tCourseDailyReportMapper.delete(courseRegistForm.getCourseId());
			tEvCourseMapper.delete(courseRegistForm.getCourseId());
			tCourseMeetingMapper.delete(courseRegistForm.getCourseId());
		}
		// コース・日報紐付け
		if (courseRegistForm.getDailyReportId() != null) {
			TCourseDailyReport tCourseDailyReport = new TCourseDailyReport();
			tCourseDailyReport.setCourseId(mCourse.getCourseId());
			tCourseDailyReport.setDailyReportId(courseRegistForm.getDailyReportId());
			tCourseDailyReport.setAccountId(loginUserDto.getAccountId());
			tCourseDailyReport.setDeleteFlg(Constants.DB_FLG_FALSE);
			tCourseDailyReport.setFirstCreateUser(loginUserDto.getLmsUserId());
			tCourseDailyReport.setFirstCreateDate(today);
			tCourseDailyReport.setLastModifiedUser(loginUserDto.getLmsUserId());
			tCourseDailyReport.setLastModifiedDate(today);
			tCourseDailyReportMapper.insert(tCourseDailyReport);
		}
		// 評価レポート・コース紐付け
		if (courseRegistForm.getEvReportIdArray() != null) {
			for (Integer evReportId : courseRegistForm.getEvReportIdArray()) {
				TEvCourse tEvCourse = new TEvCourse();
				tEvCourse.setEvReportId(evReportId);
				tEvCourse.setCourseId(mCourse.getCourseId());
				tEvCourse.setAccountId(loginUserDto.getAccountId());
				tEvCourse.setDeleteFlg(Constants.DB_FLG_FALSE);
				tEvCourse.setFirstCreateUser(loginUserDto.getLmsUserId());
				tEvCourse.setFirstCreateDate(today);
				tEvCourse.setLastModifiedUser(loginUserDto.getLmsUserId());
				tEvCourse.setLastModifiedDate(today);
				tEvCourseMapper.insert(tEvCourse);
			}
		}
		// コース・面談ファイル付け
		if (courseRegistForm.getMeetingFileId() != null) {
			TCourseMeeting tCourseMeeting = new TCourseMeeting();
			tCourseMeeting.setCourseId(mCourse.getCourseId());
			tCourseMeeting.setMeetingFileId(courseRegistForm.getMeetingFileId());
			tCourseMeeting.setAccountId(loginUserDto.getAccountId());
			tCourseMeeting.setDeleteFlg(Constants.DB_FLG_FALSE);
			tCourseMeeting.setFirstCreateUser(loginUserDto.getLmsUserId());
			tCourseMeeting.setFirstCreateDate(today);
			tCourseMeeting.setLastModifiedUser(loginUserDto.getLmsUserId());
			tCourseMeeting.setLastModifiedDate(today);
			tCourseMeetingMapper.insert(tCourseMeeting);
		}
		return mCourse.getCourseId();
	}

	/**
	 * Task.115 コース詳細登録情報の取得
	 * 
	 * @param courseDetailRegistForm
	 */
	public void setCourseDetailRegistForm(CourseDetailRegistForm courseDetailRegistForm) {
		// コース詳細情報の取得
		CourseServiceCourseDto courseServiceCourseDto = mCourseMapper.getCourseDetail(
				courseDetailRegistForm.getCourseId(), null, Constants.DB_FLG_FALSE);
		BeanUtils.copyProperties(courseServiceCourseDto, courseDetailRegistForm);
		// 研修日リスト設定
		List<MHoliday> mHolidayList = mHolidayMapper.findByHolidayStartAndEndDate(
				courseServiceCourseDto.getOpenTime(), courseServiceCourseDto.getCloseTime(),
				Constants.DB_FLG_FALSE);
		courseDetailRegistForm.setWorkingDaysList(new ArrayList<>());
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(courseServiceCourseDto.getOpenTime());
		while (true) {
			boolean workingDay = true;
			for (MHoliday mHoliday : mHolidayList) {
				if (calendar.getTime().compareTo(mHoliday.getHolidayDate()) == 0) {
					workingDay = false;
					break;
				}
			}
			if (workingDay) {
				courseDetailRegistForm.getWorkingDaysList().add(sdf.format(calendar.getTime()));
			}
			if (calendar.getTime().compareTo(courseServiceCourseDto.getCloseTime()) == 0) {
				break;
			}
			calendar.add(Calendar.DATE, 1);
		}
		// カテゴリDTOリスト設定
		courseDetailRegistForm
				.setCategoryDtoList(mCategoryMapper.getCategoryDtoList(Constants.DB_FLG_FALSE));
		// 更新フラグ設定
		if (courseServiceCourseDto.getCourseServiceCategoryDtoList().get(0)
				.getCategoryId() != null) {
			courseDetailRegistForm.setUpdateFlg(true);
		}

	}

	/**
	 * Task.115 コース詳細情報の登録
	 * 
	 * @param courseDetailRegistForm
	 */
	public String registCourseDetail(CourseDetailRegistForm courseDetailRegistForm) {
		// 既存セクションの削除
		mSectionMapper.delete(courseDetailRegistForm.getCourseId());
		// エンティティリストの作成
		Date today = new Date();
		List<MSection> mSectionList = new ArrayList<>();
		for (CourseServiceCategoryDto courseServiceCategoryDto : courseDetailRegistForm
				.getCourseServiceCategoryDtoList()) {
			// カテゴリーIDがない、または削除フラグの立っているDTOは除外
			if (courseServiceCategoryDto.getCategoryId() == null
					|| courseServiceCategoryDto.getCategoryDelFlg() == 1) {
				continue;
			}
			for (CourseServiceSectionDto courseServiceSectionDto : courseServiceCategoryDto
					.getCourseServiceSectionDtoList()) {
				// セクション名がない、または削除フラグの立っているDTOは除外
				if (courseServiceSectionDto.getSectionName() == null
						|| courseServiceSectionDto.getSectionDelFlg() == 1) {
					continue;
				}
				MSection mSection = new MSection();
				mSection.setSectionName(courseServiceSectionDto.getSectionName());
				mSection.setCourseId(courseDetailRegistForm.getCourseId());
				mSection.setCategoryId(courseServiceCategoryDto.getCategoryId());
				mSection.setDate(courseServiceSectionDto.getDate());
				mSection.setAccountId(loginUserDto.getAccountId());
				mSection.setDeleteFlg(Constants.DB_FLG_FALSE);
				mSection.setFirstCreateUser(loginUserDto.getLmsUserId());
				mSection.setFirstCreateDate(today);
				mSection.setLastModifiedUser(loginUserDto.getLmsUserId());
				mSection.setLastModifiedDate(today);
				mSectionList.add(mSection);
			}
		}
		// 日付順で並べ替え
		Collections.sort(mSectionList);
		// セクションの登録
		for (MSection mSection : mSectionList) {
			mSectionMapper.insert(mSection);
		}
		String course = messageUtil.getMessage("course");
		return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE, new String[] { course });
	}

}
