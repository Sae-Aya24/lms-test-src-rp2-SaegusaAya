package jp.co.sss.lms.service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jakarta.servlet.http.HttpServletResponse;
import jp.co.sss.lms.dto.CompanyDto;
import jp.co.sss.lms.dto.CourseDto;
import jp.co.sss.lms.dto.DailyReportDetailDto;
import jp.co.sss.lms.dto.DailyReportDownloadDto;
import jp.co.sss.lms.dto.DailyReportDto;
import jp.co.sss.lms.dto.DailyReportFbDto;
import jp.co.sss.lms.dto.IntelligibilityDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.PlaceDto;
import jp.co.sss.lms.dto.UserDailyReportDto;
import jp.co.sss.lms.dto.WorkbookDto;
import jp.co.sss.lms.entity.TDailyReportFb;
import jp.co.sss.lms.entity.TDailyReportSubmit;
import jp.co.sss.lms.entity.TDailyReportSubmitDetail;
import jp.co.sss.lms.entity.TIntelligibility;
import jp.co.sss.lms.form.DailyReportDetailForm;
import jp.co.sss.lms.form.DailyReportDownloadForm;
import jp.co.sss.lms.form.DailyReportSearchForm;
import jp.co.sss.lms.form.DailyReportSubmitForm;
import jp.co.sss.lms.mapper.MDailyReportMapper;
import jp.co.sss.lms.mapper.MLmsUserMapper;
import jp.co.sss.lms.mapper.TCourseDailyReportMapper;
import jp.co.sss.lms.mapper.TDailyReportFbMapper;
import jp.co.sss.lms.mapper.TDailyReportSubmitDetailMapper;
import jp.co.sss.lms.mapper.TDailyReportSubmitMapper;
import jp.co.sss.lms.mapper.TIntelligibilityMapper;
import jp.co.sss.lms.mapper.TSectionDailyReportMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.DateUtil;
import jp.co.sss.lms.util.ExcelUtil;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * レポート情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class ReportService {

	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private DateUtil dateUtil;
	@Autowired
	private TCourseDailyReportMapper tCourseDailyReportMapper;
	@Autowired
	private TSectionDailyReportMapper tSectionDailyReportMapper;
	@Autowired
	private TDailyReportSubmitMapper tDailyReportSubmitMapper;
	@Autowired
	private TDailyReportSubmitDetailMapper tDailyReportSubmitDetailMapper;
	@Autowired
	private TIntelligibilityMapper tIntelligibilityMapper;
	@Autowired
	private TDailyReportFbMapper tDailyReportFbMapper;
	@Autowired
	private MLmsUserMapper mLmsUserMapper;
	@Autowired
	private MDailyReportMapper mDailyReportMapper;
	@Autowired
	@Lazy
	private CourseService courseService;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private CompanyService companyService;

	/**
	 * レポート情報取得
	 * 
	 * @param dailyReportSubmitForm
	 * @return レポートフォーム
	 * @throws ParseException
	 */
	public void getDailyReport(DailyReportSubmitForm dailyReportSubmitForm) throws ParseException {

		DailyReportDto dailyReportDto;
		if (dailyReportSubmitForm.getDailyReportSubmitId() != null) {
			// Task.22 日報提出IDで提出済みのレポートDTOを取得
			dailyReportDto = getDailyReportDto(dailyReportSubmitForm.getDailyReportSubmitId());
		} else {
			// コースに紐づくレポート情報を取得
			dailyReportDto = tCourseDailyReportMapper.getDailyReportDto(
					dailyReportSubmitForm.getDailyReportId(), loginUserDto.getCourseId(),
					loginUserDto.getAccountId(), loginUserDto.getLmsUserId(),
					Constants.DB_FLG_FALSE, dailyReportSubmitForm.getDate());
			// 無かった場合にセクションに紐づく情報を取得
			if (dailyReportDto == null) {
				dailyReportDto = tSectionDailyReportMapper.getDailyReportDto(
						dailyReportSubmitForm.getDailyReportId(),
						dailyReportSubmitForm.getSectionId(), loginUserDto.getAccountId(),
						loginUserDto.getLmsUserId(), Constants.DB_FLG_FALSE,
						dailyReportSubmitForm.getDate());
			}
		}

		// フォームに詰め替え
		dailyReportSubmitForm.setDailyReportId(dailyReportDto.getDailyReportId());
		if (dailyReportSubmitForm.getDate() == null) {
			dailyReportSubmitForm.setDate(dailyReportDto.getDate());
		}
		dailyReportSubmitForm.setReportName(dailyReportDto.getReportName());
		dailyReportSubmitForm.setIntelligibilityFlg(dailyReportDto.getIntelligibilityFlg());
		if (dailyReportDto.getIntelligibilityFlg() != null
				&& dailyReportDto.getIntelligibilityFlg() == 1) {
			dailyReportSubmitForm
					.setIntelligibilityFieldNum(dailyReportDto.getIntelligibilityFieldNum());
			dailyReportSubmitForm.setIntelligibilityNum(dailyReportDto.getIntelligibilityNum());
			Short intelligibilityDtoListSize = dailyReportDto.getIntelligibilityFieldNum();
			String[] intFieldNameArray = new String[intelligibilityDtoListSize];
			Short[] intFieldValueArray = new Short[intelligibilityDtoListSize];
			List<IntelligibilityDto> intelligibilityDtoList = dailyReportDto
					.getIntelligibilityDtoList();
			if (!(intelligibilityDtoList.size() == 1
					&& intelligibilityDtoList.get(0).getIntelligibilityId() == null)) {
				for (int i = 0; i < intelligibilityDtoList.size(); i++) {
					intFieldNameArray[i] = intelligibilityDtoList.get(i).getFieldName();
					intFieldValueArray[i] = intelligibilityDtoList.get(i).getFieldValue();
				}
			}
			dailyReportSubmitForm.setIntFieldNameArray(intFieldNameArray);
			dailyReportSubmitForm.setIntFieldValueArray(intFieldValueArray);
		}
		Integer dailyReportDetailDtoListSize = dailyReportDto.getDailyReportDetailDtoList().size();
		String[] fieldNameArray = new String[dailyReportDetailDtoListSize];
		Short[] requiredFlgArray = new Short[dailyReportDetailDtoListSize];
		Short[] inputTypeArray = new Short[dailyReportDetailDtoListSize];
		Integer[] rangeFromArray = new Integer[dailyReportDetailDtoListSize];
		Integer[] rangeToArray = new Integer[dailyReportDetailDtoListSize];
		String[] contentArray = new String[dailyReportDetailDtoListSize];
		for (int j = 0; j < dailyReportDto.getDailyReportDetailDtoList().size(); j++) {
			DailyReportDetailDto dailyReportDetailDto = dailyReportDto.getDailyReportDetailDtoList()
					.get(j);
			fieldNameArray[j] = dailyReportDetailDto.getFieldName();
			requiredFlgArray[j] = dailyReportDetailDto.getRequiredFlg();
			inputTypeArray[j] = dailyReportDetailDto.getInputType();
			rangeFromArray[j] = dailyReportDetailDto.getRangeFrom();
			rangeToArray[j] = dailyReportDetailDto.getRangeTo();
			contentArray[j] = dailyReportDetailDto.getContent();
		}
		dailyReportSubmitForm.setFieldNameArray(fieldNameArray);
		dailyReportSubmitForm.setRequiredFlgArray(requiredFlgArray);
		dailyReportSubmitForm.setInputTypeArray(inputTypeArray);
		dailyReportSubmitForm.setRangeFromArray(rangeFromArray);
		dailyReportSubmitForm.setRangeToArray(rangeToArray);
		dailyReportSubmitForm.setContentArray(contentArray);
	}

	/**
	 * レポート登録
	 * 
	 * @param form
	 * @throws ParseException
	 */
	public void submit(DailyReportSubmitForm dailyReportSubmitForm) throws ParseException {

		Date today = new Date();
		// 日報提出エンティティを生成
		TDailyReportSubmit tDailyReportSubmit = new TDailyReportSubmit();
		tDailyReportSubmit.setDailyReportId(dailyReportSubmitForm.getDailyReportId());
		tDailyReportSubmit.setDate(dailyReportSubmitForm.getDate());
		Integer lmsUserId = loginUserUtil.isStudent() ? loginUserDto.getLmsUserId()
				: dailyReportSubmitForm.getLmsUserId();
		tDailyReportSubmit.setLmsUserId(lmsUserId);
		tDailyReportSubmit.setAccountId(loginUserDto.getAccountId());
		tDailyReportSubmit.setDeleteFlg(Constants.DB_FLG_FALSE);
		tDailyReportSubmit.setLastModifiedUser(loginUserDto.getLmsUserId());
		tDailyReportSubmit.setLastModifiedDate(today);

		// 日報提出IDがnullの場合
		if (dailyReportSubmitForm.getDailyReportSubmitId() == null) {
			tDailyReportSubmit.setFirstCreateUser(loginUserDto.getLmsUserId());
			tDailyReportSubmit.setFirstCreateDate(today);
			tDailyReportSubmitMapper.insert(tDailyReportSubmit);
		} else {
			tDailyReportSubmit
					.setDailyReportSubmitId(dailyReportSubmitForm.getDailyReportSubmitId());
			tDailyReportSubmitMapper.update(tDailyReportSubmit);
			// 日報提出詳細の削除
			Integer countSubmitDetail = tDailyReportSubmitDetailMapper
					.getCountByDailyReportSubmitId(tDailyReportSubmit.getDailyReportSubmitId());
			if (countSubmitDetail > 0) {
				tDailyReportSubmitDetailMapper.delete(tDailyReportSubmit.getDailyReportSubmitId());
			}
			// 理解度の削除
			Integer countIntelligibility = tIntelligibilityMapper
					.getCountByDailyReportSubmitId(tDailyReportSubmit.getDailyReportSubmitId());
			if (countIntelligibility > 0) {
				tIntelligibilityMapper.delete(tDailyReportSubmit.getDailyReportSubmitId());
			}
		}
		// レポート詳細登録
		for (int i = 0; i < dailyReportSubmitForm.getContentArray().length; i++) {
			TDailyReportSubmitDetail tDailyReportSubmitDetail = new TDailyReportSubmitDetail();
			tDailyReportSubmitDetail
					.setDailyReportSubmitId(tDailyReportSubmit.getDailyReportSubmitId());
			tDailyReportSubmitDetail.setFieldNum(i + 1);
			tDailyReportSubmitDetail.setContent(dailyReportSubmitForm.getContentArray()[i]);
			tDailyReportSubmitDetail.setAccountId(tDailyReportSubmit.getAccountId());
			tDailyReportSubmitDetail.setDeleteFlg(Constants.DB_FLG_FALSE);
			tDailyReportSubmitDetail.setFirstCreateUser(loginUserDto.getLmsUserId());
			tDailyReportSubmitDetail.setFirstCreateDate(today);
			tDailyReportSubmitDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
			tDailyReportSubmitDetail.setLastModifiedDate(today);
			tDailyReportSubmitDetailMapper.insert(tDailyReportSubmitDetail);
		}
		// 理解度登録
		if (dailyReportSubmitForm.getIntFieldNameArray() != null) {
			for (int j = 0; j < dailyReportSubmitForm.getIntFieldNameArray().length; j++) {
				TIntelligibility tIntelligibility = new TIntelligibility();
				tIntelligibility
						.setDailyReportSubmitId(tDailyReportSubmit.getDailyReportSubmitId());
				tIntelligibility.setFieldNum(j + 1);
				tIntelligibility.setFieldName(dailyReportSubmitForm.getIntFieldNameArray()[j]);
				tIntelligibility.setFieldValue(dailyReportSubmitForm.getIntFieldValueArray()[j]);
				tIntelligibility.setAccountId(tDailyReportSubmit.getAccountId());
				tIntelligibility.setDeleteFlg(Constants.DB_FLG_FALSE);
				tIntelligibility.setFirstCreateUser(loginUserDto.getLmsUserId());
				tIntelligibility.setFirstCreateDate(today);
				tIntelligibility.setLastModifiedUser(loginUserDto.getLmsUserId());
				tIntelligibility.setLastModifiedDate(today);
				tIntelligibilityMapper.insert(tIntelligibility);
			}
		}
	}

	/**
	 * レポートダウンロード
	 * 
	 * @param dailyReportId
	 * @param dailyReportSubmitId
	 * @param response
	 * @throws IOException
	 */
	public void download(Integer dailyReportId, Integer dailyReportSubmitId,
			HttpServletResponse response) throws IOException {
		WorkbookDto workbookDto = getWorkbookDto(dailyReportId, dailyReportSubmitId);
		ExcelUtil.downloadBook(workbookDto, response);
	}

	/**
	 * Task.20 レポート提出フォームの入力チェック
	 * 
	 * @param dailyReportSubmitForm
	 * @param result
	 */
	public void reportSubmitInputCheck(DailyReportSubmitForm dailyReportSubmitForm,
			BindingResult result) {

		// 学習理解度
		if (dailyReportSubmitForm.getIntFieldNameArray() != null) {
			for (int i = 0; i < dailyReportSubmitForm.getIntFieldNameArray().length; i++) {
				// 100文字チェック
				if (dailyReportSubmitForm.getIntFieldNameArray()[i].length() > 100) {
					result.addError(
							new FieldError(result.getObjectName(), "intFieldNameArray[" + i + "]",
									"* " + messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
											new String[] { "学習項目", "100" })));
				}
				// 学習項目[n]か理解度[n]どちらか片方のみに値がある場合相関チェック
				if (!StringUtils.isBlank(dailyReportSubmitForm.getIntFieldNameArray()[i])
						&& dailyReportSubmitForm.getIntFieldValueArray()[i] == null) {
					result.addError(
							new FieldError(result.getObjectName(), "intFieldValueArray[" + i + "]",
									"* " + messageUtil.getMessage(Constants.VALID_KEY_REQUIRED_WHEN,
											new String[] { "学習項目", "理解度" })));
				} else if (StringUtils.isBlank(dailyReportSubmitForm.getIntFieldNameArray()[i])
						&& dailyReportSubmitForm.getIntFieldValueArray()[i] != null) {
					result.addError(
							new FieldError(result.getObjectName(), "intFieldNameArray[" + i + "]",
									"* " + messageUtil.getMessage(Constants.VALID_KEY_REQUIRED_WHEN,
											new String[] { "理解度", "学習項目" })));
				}
			}
		}

		// 報告レポート
		for (int j = 0; j < dailyReportSubmitForm.getFieldNameArray().length; j++) {
			// 必須チェック
			if (dailyReportSubmitForm.getRequiredFlgArray()[j] == 1
					&& StringUtils.isBlank(dailyReportSubmitForm.getContentArray()[j])) {
				result.addError(new FieldError(result.getObjectName(), "contentArray[" + j + "]",
						"* " + messageUtil.getMessage(Constants.VALID_KEY_REQUIRED,
								new String[] { dailyReportSubmitForm.getFieldNameArray()[j] })));
			} else if (dailyReportSubmitForm.getInputTypeArray()[j] == 0) {
				// 数値チェック
				if (!StringUtils.isNumeric(dailyReportSubmitForm.getContentArray()[j])) {
					result.addError(new FieldError(result.getObjectName(),
							"contentArray[" + j + "]",
							"* " + messageUtil.getMessage(Constants.VALID_KEY_NOTHALFSIZENUMBER,
									new String[] {
											dailyReportSubmitForm.getFieldNameArray()[j] })));
					// 期間チェック
				} else if (dailyReportSubmitForm.getRangeFromArray()[j] > Integer
						.parseInt(dailyReportSubmitForm.getContentArray()[j])
						|| dailyReportSubmitForm.getRangeToArray()[j] < Integer
								.parseInt(dailyReportSubmitForm.getContentArray()[j])) {
					result.addError(new FieldError(result.getObjectName(),
							"contentArray[" + j + "]",
							"* " + messageUtil.getMessage(Constants.VALID_KEY_OUTRANGENUMBER,
									new String[] { dailyReportSubmitForm.getFieldNameArray()[j],
											dailyReportSubmitForm.getRangeFromArray()[j].toString(),
											dailyReportSubmitForm.getRangeToArray()[j]
													.toString() })));
				}
			} else if (dailyReportSubmitForm.getContentArray()[j].length() > 2000) {
				// 2000文字チェック
				result.addError(new FieldError(result.getObjectName(), "contentArray[" + j + "]",
						"* " + messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH, new String[] {
								dailyReportSubmitForm.getFieldNameArray()[j], "2000" })));
			}
		}
	}

	/**
	 * Task.22 提出済みレポート情報取得
	 * 
	 * @param dailyReportSubmitId
	 * @return レポートDTO
	 */
	public DailyReportDto getDailyReportDto(Integer dailyReportSubmitId) {

		DailyReportDto dailyReportDto = tDailyReportSubmitMapper
				.getDailyReportDto(dailyReportSubmitId, Constants.DB_FLG_FALSE);

		return dailyReportDto;
	}

	/**
	 * Task.22 レポートフィードバック登録
	 * 
	 * @param dailyReportDetailForm
	 */
	public void feedbackRegist(DailyReportDetailForm dailyReportDetailForm) {
		Date now = new Date();
		TDailyReportFb tDailyReportFb = new TDailyReportFb();
		tDailyReportFb.setDailyReportSubmitId(dailyReportDetailForm.getDailyReportSubmitId());
		tDailyReportFb.setLmsUserId(loginUserDto.getLmsUserId());
		tDailyReportFb.setContent(dailyReportDetailForm.getContent());
		tDailyReportFb.setAccountId(loginUserDto.getAccountId());
		tDailyReportFb.setDeleteFlg(Constants.DB_FLG_FALSE);
		tDailyReportFb.setFirstCreateUser(loginUserDto.getLmsUserId());
		tDailyReportFb.setFirstCreateDate(now);
		tDailyReportFb.setLastModifiedUser(loginUserDto.getLmsUserId());
		tDailyReportFb.setLastModifiedDate(now);
		tDailyReportFbMapper.insert(tDailyReportFb);
	}

	/**
	 * Task.22 レポートフィードバック論理削除
	 * 
	 * @param dailyReportDetailForm
	 */
	public void feedbackDelete(DailyReportDetailForm dailyReportDetailForm) {
		Date now = new Date();
		TDailyReportFb tDailyReportFb = new TDailyReportFb();
		tDailyReportFb.setDailyReportFbId(dailyReportDetailForm.getDailyReportFbId());
		tDailyReportFb.setDeleteFlg(Constants.DB_FLG_TRUE);
		tDailyReportFb.setLastModifiedUser(loginUserDto.getLmsUserId());
		tDailyReportFb.setLastModifiedDate(now);
		tDailyReportFbMapper.deleteUpdate(tDailyReportFb);
	}

	/**
	 * Task.23 レポートフィードバック更新
	 * 
	 * @param dailyReportDetailForm
	 */
	public void feedbackUpdate(DailyReportDetailForm dailyReportDetailForm) {
		Date now = new Date();
		TDailyReportFb tDailyReportFb = new TDailyReportFb();
		tDailyReportFb.setDailyReportFbId(dailyReportDetailForm.getDailyReportFbId());
		tDailyReportFb.setContent(dailyReportDetailForm.getContent());
		tDailyReportFb.setDeleteFlg(Constants.DB_FLG_FALSE);
		tDailyReportFb.setLastModifiedUser(loginUserDto.getLmsUserId());
		tDailyReportFb.setLastModifiedDate(now);
		tDailyReportFbMapper.update(tDailyReportFb);
	}

	/**
	 * Task.50 レポート検索フォームの設定
	 * 
	 * @param dailyReportSearchForm
	 */
	public void setDailyReportSearchForm(DailyReportSearchForm dailyReportSearchForm) {
		// コースDTOリスト取得
		List<CourseDto> courseDtoList = courseService.getCourseDtoList();
		dailyReportSearchForm.setCourseDtoList(courseDtoList);

		if (loginUserUtil.isAdmin()) {
			// Task.80 会場DTOリスト取得
			List<PlaceDto> placeDtoList = placeService.getPlaceDtoList(null);
			dailyReportSearchForm.setPlaceDtoList(placeDtoList);
		} else {
			// 会場DTO取得
			PlaceDto placeDto = placeService.getPlaceDto();
			dailyReportSearchForm.setPlaceId(placeDto.getPlaceId());
			dailyReportSearchForm.setPlaceName(placeDto.getPlaceName());
			dailyReportSearchForm.setPlaceNote(placeDto.getPlaceNote());
		}

		// 企業DTOリスト取得
		List<CompanyDto> companyDtoList = companyService.getCompanyDtoList();
		dailyReportSearchForm.setCompanyDtoList(companyDtoList);

		// 経過時間ラベル
		dailyReportSearchForm
				.setPastTimeLabel(messageUtil.getMessage("setting.search.pastTimeLabel"));
	}

	/**
	 * Task.50 レポート検索フォームに当日日付を設定
	 * 
	 * @param dailyReportSearchForm
	 */
	public void setToday(DailyReportSearchForm dailyReportSearchForm) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date date = new Date();
		String today = sdf.format(date);
		dailyReportSearchForm.setDateFrom(today);
		dailyReportSearchForm.setDateTo(today);
	}

	/**
	 * Task.50 レポート検索フォームの入力チェック
	 * 
	 * @param dailyReportSearchForm
	 * @param result
	 * @return チェック結果
	 * @throws ParseException
	 */
	public void searchReportInputCheck(DailyReportSearchForm dailyReportSearchForm,
			BindingResult result) throws ParseException {

		// フォームの入力チェックでエラーがある場合はチェックを行わない
		if (result.hasErrors()) {
			return;
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFrom = sdf.parse(dailyReportSearchForm.getDateFrom());
		Date dateTo = sdf.parse(dailyReportSearchForm.getDateTo());
		if (dateFrom.compareTo(dateTo) == 1) {
			result.addError(new FieldError(result.getObjectName(), "dateTo",
					messageUtil.getMessage(Constants.VALID_KEY_SEARCHPERIODCOMPAREERROR,
							new String[] { "日付(To)", "日付(From)" })));
		}
	}

	/**
	 * Task.50 ユーザーレポート情報DTOリスト取得
	 * 
	 * @param dailyReportSearchForm
	 * @return ユーザーレポート情報DTOリスト
	 * @throws ParseException
	 */
	public List<UserDailyReportDto> getUserDailyReportDtoList(
			DailyReportSearchForm dailyReportSearchForm) throws ParseException {

		Integer placeId = null;
		if (loginUserUtil.isAdmin()) {
			// Task.80
			placeId = dailyReportSearchForm.getPlaceId();
		} else {
			placeId = loginUserDto.getPlaceId();
		}

		// Task.80
		Date pastDate = null;
		if (loginUserUtil.isAdmin() && dailyReportSearchForm.getPastFlg() == null) {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			date = sdf.parse(sdf.format(date));
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);
			Integer pastTime = Integer.parseInt(messageUtil.getMessage("setting.search.pastTime"));
			calendar.add(Calendar.MONTH, -pastTime);
			pastDate = calendar.getTime();
		}

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date dateFrom = sdf.parse(dailyReportSearchForm.getDateFrom());
		Date dateTo = sdf.parse(dailyReportSearchForm.getDateTo());

		return mLmsUserMapper.getUserDailyReportDto(dateFrom, dateTo,
				dailyReportSearchForm.getCourseName(), placeId,
				dailyReportSearchForm.getCompanyName(), Constants.CODE_VAL_ROLL_STUDENT, pastDate,
				Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.50 レポート一括ダウンロード
	 * 
	 * @param dailyReportDownloadForm
	 * @param response
	 * @throws NumberFormatException
	 * @throws IOException
	 */
	public void downloadZip(DailyReportDownloadForm dailyReportDownloadForm,
			HttpServletResponse response) throws NumberFormatException, IOException {

		List<WorkbookDto> workbookDtoList = new ArrayList<>();
		for (int i = 0; i < dailyReportDownloadForm.getDailyReportSubmitIdList().length; i++) {
			WorkbookDto workbookDto = getWorkbookDto(
					Integer.parseInt(dailyReportDownloadForm.getDailyReportIdList()[i]),
					Integer.parseInt(dailyReportDownloadForm.getDailyReportSubmitIdList()[i]));
			workbookDtoList.add(workbookDto);
		}
		ExcelUtil.downloadBookInZip(workbookDtoList, response);

	}

	/**
	 * Task.114 レポートDTOリストの取得
	 * 
	 * @return レポートDTOリスト
	 */
	public List<DailyReportDto> getDailyReportDtoList() {
		return mDailyReportMapper.getDailyReportDtoList(Constants.DB_HIDDEN_FLG_FALSE,
				Constants.DB_FLG_FALSE);
	}

	/**
	 * ワークブックDTO取得
	 * 
	 * @param dailyReportId
	 * @param dailyReportSubmitId
	 * @return ワークブックDTO
	 * @throws IOException
	 */
	private WorkbookDto getWorkbookDto(Integer dailyReportId, Integer dailyReportSubmitId)
			throws IOException {

		// レポートダウンロード情報取得
		DailyReportDownloadDto dailyReportDownloadDto = tDailyReportSubmitMapper
				.getDailyReportDownloadDto(dailyReportSubmitId, dailyReportId,
						Constants.DB_FLG_FALSE);

		// テンプレートファイルのパスを設定
		String commonFileDir = messageUtil.getMessage("setting.file.common.dir");
		String excelFilePath = commonFileDir + "/" + dailyReportDownloadDto.getFileName();

		ExcelUtil excelUtil = new ExcelUtil(excelFilePath);

		// 企業名
		excelUtil.setVal(dailyReportDownloadDto.getSheetName(),
				dailyReportDownloadDto.getRowCompany() - 1,
				dailyReportDownloadDto.getClmCompany() - 1,
				dailyReportDownloadDto.getCompanyName());

		// ユーザー名
		excelUtil.setVal(dailyReportDownloadDto.getSheetName(),
				dailyReportDownloadDto.getRowUser() - 1, dailyReportDownloadDto.getClmUser() - 1,
				dailyReportDownloadDto.getUserName());

		// 日付
		excelUtil.setVal(dailyReportDownloadDto.getSheetName(),
				dailyReportDownloadDto.getRowDate() - 1, dailyReportDownloadDto.getClmDate() - 1,
				dailyReportDownloadDto.getDate());

		// 「日報提出詳細テーブル」と「日報詳細マスタ」のデータを設定する。
		for (DailyReportDetailDto dailyReportDetailDto : dailyReportDownloadDto
				.getDailyReportDetailDtoList()) {
			excelUtil.setVal(dailyReportDownloadDto.getSheetName(),
					dailyReportDetailDto.getRow() - 1, dailyReportDetailDto.getClm() - 1,
					dailyReportDetailDto.getContent());
		}

		if (dailyReportDownloadDto.getIntelligibilityDtoList().size() > 0) {

			Integer rowIntelFld = dailyReportDownloadDto.getRowIntelFld();
			Integer rowIntel = dailyReportDownloadDto.getRowIntel();

			for (IntelligibilityDto intelligibilityDto : dailyReportDownloadDto
					.getIntelligibilityDtoList()) {
				excelUtil.setVal(dailyReportDownloadDto.getSheetName(), rowIntelFld - 1,
						dailyReportDownloadDto.getClmIntelFld() - 1,
						intelligibilityDto.getFieldName());
				excelUtil.setVal(dailyReportDownloadDto.getSheetName(), rowIntel - 1,
						dailyReportDownloadDto.getClmIntel() - 1,
						intelligibilityDto.getFieldValue());
				rowIntelFld++;
				rowIntel++;
			}
		}

		if (dailyReportDownloadDto.getDailyReportFbDtoList() != null) {
			if (dailyReportDownloadDto.getDailyReportFbDtoList().size() > 1) {
				for (int i = 1; i < dailyReportDownloadDto.getDailyReportFbDtoList().size(); i++) {
					excelUtil.sheetCopy("フィードバックコメント", 6, 8, i);
				}
			}
			for (int i = 0; i < dailyReportDownloadDto.getDailyReportFbDtoList().size(); i++) {
				DailyReportFbDto dailyReportFbDto = dailyReportDownloadDto.getDailyReportFbDtoList()
						.get(i);
				// 指定位置にフィードバックしたユーザを設定
				excelUtil.setVal("フィードバックコメント", 6 + (i * 2), 0, dailyReportFbDto.getUserName());
				// 指定位置にコメントを入力した日付を設定
				excelUtil.setVal("フィードバックコメント", 6 + (i * 2), 17, dailyReportFbDto.getDate());
				// 指定位置に入力内容を設定
				excelUtil.setVal("フィードバックコメント", 7 + (i * 2), 10, dailyReportFbDto.getContent());
			}
		}

		WorkbookDto workbookDto = new WorkbookDto();
		workbookDto.setWb(excelUtil.getWb());
		workbookDto.getWb().setForceFormulaRecalculation(true);

		String[] fileName = dailyReportDownloadDto.getFileName().split("\\.");
		String bookName = "";
		for (int i = 0; i < fileName.length - 1; i++) {
			if (i > 0) {
				bookName += ".";
			}
			bookName += fileName[i];
		}

		// 受講生名に全角スペースが使用されている場合は半角スペースに置き換える
		String userName = dailyReportDownloadDto.getUserName().replaceAll("　", " ");
		String companyName = dailyReportDownloadDto.getCompanyName().replaceAll("　", " ");

		workbookDto.setWbName(
				bookName + "_" + dateUtil.toString(dailyReportDownloadDto.getDate(), "YYYYMMdd")
						+ "_" + companyName + "_" + userName + "." + fileName[fileName.length - 1]);

		return workbookDto;
	}

}
