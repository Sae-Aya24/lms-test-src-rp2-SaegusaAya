package jp.co.sss.lms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.CompanyCourseDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.PlaceDto;
import jp.co.sss.lms.dto.TakeOverScheduleDetailDto;
import jp.co.sss.lms.dto.TakeOverScheduleDto;
import jp.co.sss.lms.entity.MMeetingSchedule;
import jp.co.sss.lms.entity.MMeetingScheduleDetail;
import jp.co.sss.lms.entity.TMeetingCompany;
import jp.co.sss.lms.entity.TMeetingPlace;
import jp.co.sss.lms.form.TakeOverCandidateListForm;
import jp.co.sss.lms.form.TakeOverScheduleRegistForm;
import jp.co.sss.lms.form.TakeOverScheduleReserveForm;
import jp.co.sss.lms.form.TakeOverSelectCompanyForm;
import jp.co.sss.lms.form.TakeOverSelectDateForm;
import jp.co.sss.lms.mapper.MCompanyMapper;
import jp.co.sss.lms.mapper.MMeetingScheduleDetailMapper;
import jp.co.sss.lms.mapper.MMeetingScheduleMapper;
import jp.co.sss.lms.mapper.TMeetingCompanyMapper;
import jp.co.sss.lms.mapper.TMeetingPlaceMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.TrainingTime;

/**
 * 引継面談／会場見学情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class TakeOverService {

	@Autowired
	private MMeetingScheduleMapper mMeetingScheduleMapper;
	@Autowired
	private MMeetingScheduleDetailMapper mMeetingScheduleDetailMapper;
	@Autowired
	private TMeetingPlaceMapper tMeetingPlaceMapper;
	@Autowired
	private TMeetingCompanyMapper tMeetingCompanyMapper;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private MCompanyMapper mCompanyMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private TrainingTime trainingTime;

	/**
	 * Task.59 引継面談／会場見学スケジュールDTOリストの取得
	 * 
	 * @return 引継面談／会場見学スケジュールDTOリスト
	 */
	public List<TakeOverScheduleDto> getTakeOverScheduleDtoList() {
		// Task.108 引数に公開フラグを追加
		return mMeetingScheduleMapper.getTakeOverScheduleDtoList(loginUserDto.getPlaceId(),
				Constants.PUBLISHED_FLG_TRUE, Constants.DB_FLG_FALSE);
	}

	/**
	 * Task.73 引継面談／会場見学スケジュール詳細DTOリストの取得
	 * 
	 * @param takeOverScheduleReserveForm
	 */
	public void setTakeOverScheduleDetailDtoList(
			TakeOverScheduleReserveForm takeOverScheduleReserveForm) {
		List<TakeOverScheduleDetailDto> takeOverScheduleDetailDtoList = mMeetingScheduleDetailMapper
				.getTakeOverScheduleDetailDtoList(
						takeOverScheduleReserveForm.getMeetingScheduleId(), Constants.DB_FLG_FALSE);
		takeOverScheduleReserveForm.setTakeOverScheduleDetailDtoList(takeOverScheduleDetailDtoList);
		updateFlgs(takeOverScheduleReserveForm);
	}

	/**
	 * Task.74 引継面談／会場見学スケジュール登録フォームの入力チェック
	 * 
	 * @param takeOverScheduleReserveForm
	 * @param result
	 */
	public void takeOverScheduleReserveFormInputCheck(
			TakeOverScheduleReserveForm takeOverScheduleReserveForm, BindingResult result) {
		updateFlgs(takeOverScheduleReserveForm);
		if (takeOverScheduleReserveForm.getReservedIndex() == null) {
			result.addError(new FieldError(result.getObjectName(), "reservedIndex",
					messageUtil.getMessage(Constants.VALID_KEY_NOTSELECTED)));
			return;
		}
		TakeOverScheduleDetailDto takeOverScheduleDetailDto = takeOverScheduleReserveForm
				.getTakeOverScheduleDetailDtoList()
				.get(takeOverScheduleReserveForm.getReservedIndex());
		if (takeOverScheduleDetailDto.getJoinAmount() == null) {
			result.addError(new FieldError(result.getObjectName(),
					"takeOverScheduleDetailDtoList["
							+ takeOverScheduleReserveForm.getReservedIndex() + "].joinAmount",
					messageUtil.getMessage(Constants.VALID_KEY_REQUIRED_JOINAMOUNT)));
		}
	}

	/**
	 * Task.74 引継面談／会場見学 時間予約情報の変更
	 * 
	 * @param takeOverScheduleReserveForm
	 * @return 完了メッセージ
	 */
	public String updateTakeOverScheduleDetailDto(
			TakeOverScheduleReserveForm takeOverScheduleReserveForm) {
		TakeOverScheduleDetailDto takeOverScheduleDetailDto = takeOverScheduleReserveForm
				.getTakeOverScheduleDetailDtoList()
				.get(takeOverScheduleReserveForm.getReservedIndex());
		Date today = new Date();
		TMeetingCompany tMeetingCompany = new TMeetingCompany();
		BeanUtils.copyProperties(takeOverScheduleDetailDto, tMeetingCompany);
		tMeetingCompany.setMeetingPlaceId(takeOverScheduleReserveForm.getMeetingPlaceId());
		tMeetingCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
		tMeetingCompany.setLastModifiedDate(today);
		tMeetingCompanyMapper.update(tMeetingCompany);
		return messageUtil.getMessage(Constants.PROP_KEY_ANYITEMS_COMPLETE,
				new String[] { "引継面談／会場見学", "予約" });
	}

	/**
	 * Task.74 引継面談／会場見学 時間予約情報の削除
	 * 
	 * @param takeOverScheduleReserveForm
	 * @return 完了メッセージ
	 */
	public String deleteTakeOverScheduleDetailDto(
			TakeOverScheduleReserveForm takeOverScheduleReserveForm) {
		TakeOverScheduleDetailDto takeOverScheduleDetailDto = takeOverScheduleReserveForm
				.getTakeOverScheduleDetailDtoList()
				.get(takeOverScheduleReserveForm.getReservedIndex());
		Date today = new Date();
		TMeetingCompany tMeetingCompany = new TMeetingCompany();
		BeanUtils.copyProperties(takeOverScheduleDetailDto, tMeetingCompany);
		tMeetingCompany.setMeetingPlaceId(takeOverScheduleReserveForm.getMeetingPlaceId());
		tMeetingCompany.setMeetingScheduleDetailId(null);
		tMeetingCompany.setJoinAmount(null);
		tMeetingCompany.setCompanyRequest(null);
		tMeetingCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
		tMeetingCompany.setLastModifiedDate(today);
		tMeetingCompanyMapper.update(tMeetingCompany);
		return messageUtil.getMessage(Constants.PROP_KEY_ANYITEMS_COMPLETE,
				new String[] { "予約", "取り消し" });
	}

	/**
	 * Task.108 引継面談／会場見学スケジュールDTOリストの取得（全件）
	 * 
	 * @param takeOverCandidateListForm
	 * @return 引継面談／会場見学スケジュールDTOリスト
	 */
	@SuppressWarnings("null")
	public void getTakeOverScheduleDtoListAll(TakeOverCandidateListForm takeOverCandidateListForm) {
		List<TakeOverScheduleDto> takeOverScheduleDtoList = mMeetingScheduleMapper
				.getTakeOverScheduleDtoList(null, (Short) null, Constants.DB_FLG_FALSE);
		takeOverCandidateListForm.setTakeOverScheduleDtoList(takeOverScheduleDtoList);
	}

	/**
	 * Task.108 引継面談／会場見学スケジュール編集前の入力チェック
	 * 
	 * @param takeOverCandidateListForm
	 * @return チェック結果
	 */
	public Boolean beforeSelectCompanyInputCheck(
			TakeOverCandidateListForm takeOverCandidateListForm) {
		// 編集不可チェック
		if (takeOverCandidateListForm.getPublishedFlg() != null
				&& takeOverCandidateListForm.getPublishedFlg() == 1) {
			String errorMessage = messageUtil.getMessage(Constants.VALID_KEY_PUBLISHED_EDIT);
			takeOverCandidateListForm.setErrorMessage(errorMessage);
			return true;
		}
		return false;
	}

	/**
	 * 
	 * Task.108 引継面談／会場見学スケジュールの削除
	 * 
	 * @param takeOverCandidateListForm
	 */
	public void deleteSchedule(TakeOverCandidateListForm takeOverCandidateListForm) {
		// 削除不可チェック
		if (takeOverCandidateListForm.getPublishedFlg() != null
				&& takeOverCandidateListForm.getPublishedFlg() == 1) {
			String errorMessage = messageUtil.getMessage(Constants.VALID_KEY_PUBLISHED_DELETE);
			takeOverCandidateListForm.setErrorMessage(errorMessage);
			return;
		}
		// 削除
		Date today = new Date();
		MMeetingSchedule mMeetingSchedule = new MMeetingSchedule();
		mMeetingSchedule.setMeetingScheduleId(takeOverCandidateListForm.getMeetingScheduleId());
		mMeetingSchedule.setDeleteFlg(Constants.DB_FLG_TRUE);
		mMeetingSchedule.setLastModifiedUser(loginUserDto.getLmsUserId());
		mMeetingSchedule.setLastModifiedDate(today);
		mMeetingScheduleMapper.updateDeleteFlg(mMeetingSchedule);
		mMeetingScheduleDetailMapper.delete(takeOverCandidateListForm.getMeetingScheduleId());
		tMeetingPlaceMapper.delete(takeOverCandidateListForm.getMeetingScheduleId());
		tMeetingCompanyMapper.delete(takeOverCandidateListForm.getMeetingPlaceId());
		// 完了メッセージ登録
		String successMessage = messageUtil.getMessage(Constants.PROP_KEY_DELETE_COMPLETE,
				new String[] { "引継面談スケジュール" });
		takeOverCandidateListForm.setSuccessMessage(successMessage);
	}

	/**
	 * Task.108 引継面談／会場見学スケジュールの公開
	 * 
	 * @param takeOverCandidateListForm
	 */
	public void publishSchedule(TakeOverCandidateListForm takeOverCandidateListForm) {
		// 公開不可チェック
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(takeOverCandidateListForm.getEditLimit());
		calendar.add(Calendar.DATE, 1);
		Date editLimit = calendar.getTime();
		boolean pastEditLimit = editLimit.before(new Date());
		if (pastEditLimit) {
			String errorMessage = messageUtil.getMessage(Constants.VALID_KEY_HASPASSED,
					new String[] { "予約編集期限", "公開" });
			takeOverCandidateListForm.setErrorMessage(errorMessage);
			return;
		}
		int meetingPlaceCount = tMeetingPlaceMapper.getMeetingPlaceCount(
				takeOverCandidateListForm.getMeetingScheduleId(), Constants.DB_FLG_FALSE);
		int meetingCompanyCount = tMeetingCompanyMapper.getMeetingCompanyCount(
				takeOverCandidateListForm.getMeetingPlaceId(), Constants.DB_FLG_FALSE);
		if (meetingPlaceCount == 0 || meetingCompanyCount == 0) {
			String errorMessage = messageUtil.getMessage(Constants.VALID_KEY_REQUIRED_SELECTITEM,
					new String[] { "面談対象の会場／企業", "公開" });
			takeOverCandidateListForm.setErrorMessage(errorMessage);
			return;
		}
		int meetingScheduleDetailCount = mMeetingScheduleDetailMapper.getMeetingScheduleDetailCount(
				takeOverCandidateListForm.getMeetingScheduleId(), Constants.DB_FLG_FALSE);
		if (meetingScheduleDetailCount == 0) {
			String errorMessage = messageUtil.getMessage(Constants.VALID_KEY_REQUIRED_REGISTITEM,
					new String[] { "面談の日程", "公開" });
			takeOverCandidateListForm.setErrorMessage(errorMessage);
			return;
		}
		// 公開フラグ更新
		Date today = new Date();
		TMeetingPlace tMeetingPlace = new TMeetingPlace();
		tMeetingPlace.setMeetingPlaceId(takeOverCandidateListForm.getMeetingPlaceId());
		tMeetingPlace.setPublishedFlg(Constants.PUBLISHED_FLG_TRUE);
		tMeetingPlace.setLastModifiedUser(loginUserDto.getLmsUserId());
		tMeetingPlace.setLastModifiedDate(today);
		tMeetingPlaceMapper.updatePublishedFlg(tMeetingPlace);
		// 完了メッセージ登録
		String successMessage = messageUtil.getMessage(Constants.PROP_KEY_PUBLISH_COMPLETE,
				new String[] { "引継面談スケジュール" });
		takeOverCandidateListForm.setSuccessMessage(successMessage);
	}

	/**
	 * Task.108 引継面談／会場見学スケジュールの公開取り消し
	 * 
	 * @param takeOverCandidateListForm
	 */
	public void cancelPublish(TakeOverCandidateListForm takeOverCandidateListForm) {
		// 取消不可チェック
		if (takeOverCandidateListForm.getReservedFlg() != null
				&& takeOverCandidateListForm.getReservedFlg() == 1) {
			String errorMessage = messageUtil.getMessage(Constants.VALID_KEY_ALREADYRESERVE,
					new String[] { "取消" });
			takeOverCandidateListForm.setErrorMessage(errorMessage);
			return;
		}
		// 公開フラグ更新
		Date today = new Date();
		TMeetingPlace tMeetingPlace = new TMeetingPlace();
		tMeetingPlace.setMeetingPlaceId(takeOverCandidateListForm.getMeetingPlaceId());
		tMeetingPlace.setPublishedFlg(Constants.PUBLISHED_FLG_FALSE);
		tMeetingPlace.setLastModifiedUser(loginUserDto.getLmsUserId());
		tMeetingPlace.setLastModifiedDate(today);
		tMeetingPlaceMapper.updatePublishedFlg(tMeetingPlace);
		// 完了メッセージ登録
		String successMessage = messageUtil.getMessage(Constants.PROP_KEY_ANYITEMS_COMPLETE,
				new String[] { "引継面談スケジュール", "公開取消" });
		takeOverCandidateListForm.setSuccessMessage(successMessage);
	}

	/**
	 * Task.109 引継面談／会場見学スケジュール登録フォームの入力チェック
	 * 
	 * @param takeOverScheduleRegistForm
	 * @param result
	 * @throws ParseException
	 */
	public void takeOverScheduleRegistFormInputCheck(
			TakeOverScheduleRegistForm takeOverScheduleRegistForm, BindingResult result)
			throws ParseException {
		// フォームバリデーションでエラーがある場合はチェックを行わない
		if (result.hasErrors()) {
			return;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		Date meetingOpenDate = sdf.parse(takeOverScheduleRegistForm.getMeetingOpenDate());
		Date meetingCloseDate = sdf.parse(takeOverScheduleRegistForm.getMeetingCloseDate());
		if (meetingOpenDate.compareTo(meetingCloseDate) == 1) {
			result.addError(new FieldError(result.getObjectName(), "meetingOpenDate", messageUtil
					.getMessage(Constants.VALID_KEY_BEFORECLOSE, new String[] { "開始日", "終了日" })));
			return;
		}
		Date editLimit = sdf.parse(takeOverScheduleRegistForm.getEditLimit());
		if (editLimit.compareTo(meetingOpenDate) == 1) {
			result.addError(
					new FieldError(result.getObjectName(), "editLimit", messageUtil.getMessage(
							Constants.VALID_KEY_BEFORECLOSE, new String[] { "予約編集期限", "開始日" })));
			return;
		}
	}

	/**
	 * Task.109 引継面談／会場見学スケジュールの登録
	 * 
	 * @param takeOverScheduleRegistForm
	 * @return 面談スケジュールID
	 * @throws ParseException
	 */
	public Integer registSchedule(TakeOverScheduleRegistForm takeOverScheduleRegistForm)
			throws ParseException {
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		MMeetingSchedule mMeetingSchedule = new MMeetingSchedule();
		mMeetingSchedule.setPurpose(takeOverScheduleRegistForm.getPurpose());
		mMeetingSchedule
				.setMeetingOpenDate(sdf.parse(takeOverScheduleRegistForm.getMeetingOpenDate()));
		mMeetingSchedule
				.setMeetingCloseDate(sdf.parse(takeOverScheduleRegistForm.getMeetingCloseDate()));
		mMeetingSchedule.setEditLimit(sdf.parse(takeOverScheduleRegistForm.getEditLimit()));
		mMeetingSchedule.setDeleteFlg(Constants.DB_FLG_FALSE);
		mMeetingSchedule.setFirstCreateUser(loginUserDto.getLmsUserId());
		mMeetingSchedule.setFirstCreateDate(today);
		mMeetingSchedule.setLastModifiedUser(loginUserDto.getLmsUserId());
		mMeetingSchedule.setLastModifiedDate(today);
		mMeetingScheduleMapper.insert(mMeetingSchedule);
		return mMeetingSchedule.getMeetingScheduleId();
	}

	/**
	 * Task.110 引継面談／会場見学スケジュール企業選択フォームの設定
	 * 
	 * @param takeOverSelectCompanyForm
	 */
	public void setSelectCompanyForm(TakeOverSelectCompanyForm takeOverSelectCompanyForm) {
		// 面談スケジュールの取得
		TakeOverScheduleDto takeOverScheduleDto = mMeetingScheduleMapper.getTakeOverScheduleDto(
				takeOverSelectCompanyForm.getMeetingScheduleId(), Constants.DB_FLG_FALSE);
		BeanUtils.copyProperties(takeOverScheduleDto, takeOverSelectCompanyForm);
		// 会場リスト（全件）の取得
		List<PlaceDto> placeDtoList = placeService.getPlaceDtoList(null);
		takeOverSelectCompanyForm.setPlaceDtoList(placeDtoList);
		// 企業コースリスト（全件）の取得
		List<CompanyCourseDto> companyCourseDtoList = mCompanyMapper
				.getCompanyCourseDtoList(Constants.DB_FLG_FALSE);
		takeOverSelectCompanyForm.setCompanyCourseDtoList(companyCourseDtoList);
	}

	/**
	 * Task.110 引継面談／会場見学スケジュール企業選択フォームの入力チェック
	 * 
	 * @param takeOverScheduleRegistForm
	 * @param result
	 */
	public void selectCompanyFormInputCheck(TakeOverSelectCompanyForm takeOverSelectCompanyForm,
			BindingResult result) {
		if (takeOverSelectCompanyForm.getSelectedCompanyIdList() == null) {
			result.addError(new FieldError(result.getObjectName(), "selectedCompanyIdList",
					messageUtil.getMessage(Constants.VALID_KEY_REQUIRED_SELECT,
							new String[] { "企業" })));
		}
	}

	/**
	 * Task.110 引継面談／会場見学スケジュール企業選択情報の登録
	 * 
	 * @param takeOverSelectCompanyForm
	 */
	public void registPlaceAndCompany(TakeOverSelectCompanyForm takeOverSelectCompanyForm) {
		int meetingScheduleId = takeOverSelectCompanyForm.getMeetingScheduleId();
		// 既存情報の削除
		int meetingPlaceCount = tMeetingPlaceMapper.getMeetingPlaceCount(meetingScheduleId,
				Constants.DB_FLG_FALSE);
		if (meetingPlaceCount > 0) {
			Integer meetingPlaceId = tMeetingPlaceMapper.getMeetingPlaceId(meetingScheduleId,
					Constants.DB_FLG_FALSE);
			tMeetingCompanyMapper.delete(meetingPlaceId);
			tMeetingPlaceMapper.delete(meetingScheduleId);
		}
		Date today = new Date();
		// 面談対象会場の登録
		TMeetingPlace tMeetingPlace = new TMeetingPlace();
		tMeetingPlace.setMeetingScheduleId(meetingScheduleId);
		tMeetingPlace.setPlaceId(takeOverSelectCompanyForm.getSelectedPlaceId());
		tMeetingPlace.setDeleteFlg(Constants.DB_FLG_FALSE);
		tMeetingPlace.setFirstCreateUser(loginUserDto.getLmsUserId());
		tMeetingPlace.setFirstCreateDate(today);
		tMeetingPlace.setLastModifiedUser(loginUserDto.getLmsUserId());
		tMeetingPlace.setLastModifiedDate(today);
		tMeetingPlace.setPublishedFlg(Constants.PUBLISHED_FLG_FALSE);
		tMeetingPlaceMapper.insert(tMeetingPlace);
		// 面談対象企業の登録
		for (Integer selectedCompanyId : takeOverSelectCompanyForm.getSelectedCompanyIdList()) {
			if (selectedCompanyId != null) {
				TMeetingCompany tMeetingCompany = new TMeetingCompany();
				tMeetingCompany.setMeetingPlaceId(tMeetingPlace.getMeetingPlaceId());
				tMeetingCompany.setCompanyId(selectedCompanyId);
				tMeetingCompany.setDeleteFlg(Constants.DB_FLG_FALSE);
				tMeetingCompany.setFirstCreateUser(loginUserDto.getLmsUserId());
				tMeetingCompany.setFirstCreateDate(today);
				tMeetingCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
				tMeetingCompany.setLastModifiedDate(today);
				tMeetingCompanyMapper.insert(tMeetingCompany);
			}
		}
	}

	/**
	 * Task.111 引継面談／会場見学スケジュール日程選択フォームの設定
	 * 
	 * @param takeOverSelectDateForm
	 */
	public void setSelectDateForm(TakeOverSelectDateForm takeOverSelectDateForm) {
		// 面談スケジュールの取得
		TakeOverScheduleDto takeOverScheduleDto = mMeetingScheduleMapper.getTakeOverScheduleDto(
				takeOverSelectDateForm.getMeetingScheduleId(), Constants.DB_FLG_FALSE);
		BeanUtils.copyProperties(takeOverScheduleDto, takeOverSelectDateForm);
		// 面談スケジュール詳細リストの取得
		List<TakeOverScheduleDetailDto> takeOverScheduleDetailDtoList = mMeetingScheduleDetailMapper
				.getTakeOverScheduleDetailDtoList(takeOverSelectDateForm.getMeetingScheduleId(),
						Constants.DB_FLG_FALSE);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		List<String> meetingDateList = new ArrayList<>();
		List<String> meetingTimeList = new ArrayList<>();
		for (TakeOverScheduleDetailDto takeOverScheduleDetailDto : takeOverScheduleDetailDtoList) {
			meetingDateList.add(sdf.format(takeOverScheduleDetailDto.getMeetingDate()));
			meetingTimeList.add(takeOverScheduleDetailDto.getMeetingTime());
		}
		takeOverSelectDateForm.setMeetingDateList(meetingDateList);
		takeOverSelectDateForm.setMeetingTimeList(meetingTimeList);
	}

	/**
	 * Task.111 引継面談／会場見学スケジュール日程選択フォームの入力チェック
	 * 
	 * @param takeOverSelectDateForm
	 * @param result
	 * @throws ParseException
	 */
	public void selectDateFormInputCheck(TakeOverSelectDateForm takeOverSelectDateForm,
			BindingResult result) throws ParseException {
		List<String> meetingDateList = takeOverSelectDateForm.getMeetingDateList();
		List<String> meetingTimeList = takeOverSelectDateForm.getMeetingTimeList();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		for (int i = 0; i < meetingDateList.size(); i++) {
			String meetingDateStr = meetingDateList.get(i);
			String meetingTime = meetingTimeList.get(i);
			if (meetingDateStr == "") {
				result.addError(new FieldError(result.getObjectName(), "meetingDateList[" + i + "]",
						messageUtil.getMessage(Constants.VALID_KEY_REQUIRED,
								new String[] { "日付" })));
			} else {
				Date meetingDate = sdf.parse(meetingDateStr);
				if (meetingDate.compareTo(takeOverSelectDateForm.getMeetingOpenDate()) == -1
						|| meetingDate
								.compareTo(takeOverSelectDateForm.getMeetingCloseDate()) == 1) {
					result.addError(new FieldError(result.getObjectName(),
							"meetingDateList[" + i + "]", messageUtil.getMessage(
									Constants.VALID_KEY_OUTOFEVENTPERIOD, new String[] { "日付" })));
				}
			}
			if (meetingTime == "") {
				result.addError(new FieldError(result.getObjectName(), "meetingTimeList[" + i + "]",
						messageUtil.getMessage(Constants.VALID_KEY_REQUIRED,
								new String[] { "時間" })));
			} else if (!trainingTime.isValidTrainingTime(meetingTime)) {
				result.addError(new FieldError(result.getObjectName(), "meetingTimeList[" + i + "]",
						messageUtil.getMessage(Constants.VALID_KEY_TRAININGTIME,
								new String[] { "時間" })));
			}
		}
	}

	/**
	 * Task.111 引継面談／会場見学スケジュール日程選択情報の登録
	 * 
	 * @param takeOverSelectDateForm
	 * @throws ParseException
	 * @return 引継面談／会場見学 実施日一覧フォーム
	 */
	public TakeOverCandidateListForm selectDate(TakeOverSelectDateForm takeOverSelectDateForm)
			throws ParseException {
		int meetingScheduleId = takeOverSelectDateForm.getMeetingScheduleId();
		// 既存情報の削除
		int meetingScheduleDetailCount = mMeetingScheduleDetailMapper
				.getMeetingScheduleDetailCount(meetingScheduleId, Constants.DB_FLG_FALSE);
		if (meetingScheduleDetailCount > 0) {
			mMeetingScheduleDetailMapper.delete(meetingScheduleId);
		}
		Date today = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		// 面談スケジュール詳細の登録
		List<String> meetingDateList = takeOverSelectDateForm.getMeetingDateList();
		List<String> meetingTimeList = takeOverSelectDateForm.getMeetingTimeList();
		for (int i = 0; i < meetingDateList.size(); i++) {
			Date meetingDate = sdf.parse(meetingDateList.get(i));
			String meetingTime = new TrainingTime(meetingTimeList.get(i)).getFormattedString();
			MMeetingScheduleDetail mMeetingScheduleDetail = new MMeetingScheduleDetail();
			mMeetingScheduleDetail.setMeetingScheduleId(meetingScheduleId);
			mMeetingScheduleDetail.setMeetingDate(meetingDate);
			mMeetingScheduleDetail.setMeetingTime(meetingTime);
			mMeetingScheduleDetail.setDeleteFlg(Constants.DB_FLG_FALSE);
			mMeetingScheduleDetail.setFirstCreateUser(loginUserDto.getLmsUserId());
			mMeetingScheduleDetail.setFirstCreateDate(today);
			mMeetingScheduleDetail.setLastModifiedUser(loginUserDto.getLmsUserId());
			mMeetingScheduleDetail.setLastModifiedDate(today);
			mMeetingScheduleDetailMapper.insert(mMeetingScheduleDetail);
		}
		// 完了メッセージ登録
		TakeOverCandidateListForm takeOverCandidateListForm = new TakeOverCandidateListForm();
		String successMessage = messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE,
				new String[] { "引継面談スケジュール" });
		takeOverCandidateListForm.setSuccessMessage(successMessage);
		return takeOverCandidateListForm;
	}

	/**
	 * Task.73 予約関連フラグの更新
	 * 
	 * @param takeOverScheduleReserveForm
	 */
	private void updateFlgs(TakeOverScheduleReserveForm takeOverScheduleReserveForm) {
		// 予約編集期限チェック
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(takeOverScheduleReserveForm.getEditLimit());
		calendar.add(Calendar.DATE, 1);
		Date editLimit = calendar.getTime();
		boolean pastEditLimit = editLimit.before(new Date());
		// 満席チェック・更新可能チェック
		boolean isFullyBooked = true;
		takeOverScheduleReserveForm.setUpdatableFlg(false);
		for (int i = 0; i < takeOverScheduleReserveForm.getTakeOverScheduleDetailDtoList()
				.size(); i++) {
			TakeOverScheduleDetailDto takeOverScheduleDetailDto = takeOverScheduleReserveForm
					.getTakeOverScheduleDetailDtoList().get(i);
			// 面談対象会場IDがnullのDTOがあれば満席フラグfalse
			if (takeOverScheduleDetailDto.getMeetingCompanyId() == null) {
				isFullyBooked = false;
			}
			// Task.74 - 予約済みのDTOであれば予約済みインデックスをセット
			if (takeOverScheduleDetailDto.getMeetingCompanyId() != null
					&& takeOverScheduleReserveForm.getReservedIndex() == null
					&& takeOverScheduleDetailDto.getCompanyId() == loginUserDto.getCompanyId()) {
				takeOverScheduleReserveForm.setReservedIndex(i);
			}
			// Task.74 - 画面でチェックされた日時のDTOに企業IDをセット
			if (takeOverScheduleReserveForm.getReservedIndex() != null
					&& takeOverScheduleReserveForm.getReservedIndex() == i) {
				takeOverScheduleDetailDto.setCompanyId(loginUserDto.getCompanyId());
				// 企業IDと人数の両方がセットされていたら更新可能フラグtrue
				if (takeOverScheduleDetailDto.getJoinAmount() != null) {
					takeOverScheduleReserveForm.setUpdatableFlg(true);
				}
			}
		}
		// ３つの条件がすべてfalseなら登録可能フラグtrue
		takeOverScheduleReserveForm.setRegisterableFlg(
				!pastEditLimit && !isFullyBooked && !takeOverScheduleReserveForm.getUpdatableFlg());
	}

}
