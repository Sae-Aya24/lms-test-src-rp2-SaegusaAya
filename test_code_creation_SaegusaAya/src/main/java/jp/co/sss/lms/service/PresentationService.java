package jp.co.sss.lms.service;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.PresentationDetailDto;
import jp.co.sss.lms.dto.PresentationDto;
import jp.co.sss.lms.entity.TPresentationCompany;
import jp.co.sss.lms.form.PresentationForm;
import jp.co.sss.lms.mapper.MPresentationScheduleMapper;
import jp.co.sss.lms.mapper.TPresentationCompanyMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.LoginUserUtil;

/**
 * 成果報告サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class PresentationService {

	@Autowired
	private MPresentationScheduleMapper mPresentationScheduleMapper;
	@Autowired
	private TPresentationCompanyMapper tPresentationCompanyMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private LoginUserUtil loginUserUtil;

	/**
	 * Task.60 成果報告会一覧情報取得
	 * 
	 * @return 成果報告会DTOリスト
	 */
	public List<PresentationDto> getPresentationList() {

		List<PresentationDto> presentationDtoList = mPresentationScheduleMapper
				.getPresentationDtoList(Constants.DB_FLG_FALSE, Constants.PUBLISHED_FLG_TRUE,
						loginUserDto.getPlaceId(), loginUserDto.getCompanyId());

		if (loginUserUtil.isCompany()) {
			for (PresentationDto presentationDto : presentationDtoList) {
				for (PresentationDetailDto presentationDetailDto : presentationDto
						.getPresentationDetailDtoList()) {
					// 画面予約状況フラグの設定
					if (Constants.JOIN_ABLE_FLG.equals(presentationDetailDto.getJoinAbleFlg())) {
						presentationDto.setReservationStatusFlg(true);
					} else {
						presentationDto.setReservationStatusFlg(false);
					}
				}
			}
		}
		return presentationDtoList;
	}

	/**
	 * Task.61 成果報告会予約状況詳細情報取得
	 * 
	 * @param presentationScheduleId
	 * @return 成果報告会DTO
	 */
	public PresentationDto getReserveStatusDetail(Integer presentationScheduleId) {
		// 成果報告会予約状況取得
		PresentationDto presentationDto = mPresentationScheduleMapper.getReserveDetail(
				Constants.DB_FLG_FALSE, presentationScheduleId, loginUserDto.getCompanyId());
		for (PresentationDetailDto presentationDetailDto : presentationDto
				.getPresentationDetailDtoList()) {
			// 画面予約状況フラグの設定
			if (Constants.JOIN_ABLE_FLG.equals(presentationDetailDto.getJoinAbleFlg())) {
				presentationDetailDto.setReservationStatusFlg(true);
			} else {
				presentationDetailDto.setReservationStatusFlg(false);
			}
		}
		return presentationDto;
	}

	/**
	 * Task.63 成果報告会チーム詳細情報取得
	 * 
	 * @param presentationScheduleId
	 * @param presentationTeamId
	 * @return 成果報告会チーム詳細DTOリスト
	 */
	public PresentationDto getPresentationTeamDetailDtoList(
			Integer presentationScheduleId, Integer presentationTeamId) {

		PresentationDto presentationDto = mPresentationScheduleMapper
				.getPresentationTeamDetailDtoList(presentationScheduleId, presentationTeamId, Constants.DB_FLG_FALSE);

		return presentationDto;
	}

	/**
	 * Task.75 成果報告会一覧の予約期限前日確認
	 * 
	 * @return 予約期限フラグ
	 */
	public boolean notReserveCheck() {
		// 現在日付
		Date today = new Date();
		// 翌日
		Calendar now = Calendar.getInstance();
		now.add(Calendar.DAY_OF_MONTH, 1);
		Date nextDay = now.getTime();
		Integer notEnterCount = mPresentationScheduleMapper.notReserveCheck(Constants.DB_FLG_FALSE,
				Constants.PUBLISHED_FLG_TRUE, loginUserDto.getPlaceId(),
				loginUserDto.getCompanyId(), today, nextDay, Constants.JOIN_UNABLE_FLG);
		return notEnterCount > 0 ? true : false;
	}

	/**
	 * Task.76 成果報告会予約状況詳細情報取得
	 * 
	 * @param presentationScheduleId
	 * @return 成果報告会DTO
	 */
	public PresentationDto getReserveDetail(Integer presentationScheduleId) {
		// 成果報告会予約状況取得
		PresentationDto presentationDto = mPresentationScheduleMapper.getReserveDetail(
				Constants.DB_FLG_FALSE, presentationScheduleId, loginUserDto.getCompanyId());
		return presentationDto;
	}

	/**
	 * Task.76 成果報告 成果報告参加予約登録
	 * 
	 * @param presentationForm
	 */
	public void regist(PresentationForm presentationForm) {
		Date now = new Date();
		TPresentationCompany tPresentationCompany = new TPresentationCompany();
		tPresentationCompany.setPresentationCompanyId(presentationForm.getPresentationCompanyId());
		tPresentationCompany.setJoinAbleFlg(presentationForm.getJoinAbleFlg());
		tPresentationCompany.setJoinAmount(presentationForm.getJoinAmount());
		tPresentationCompany.setJoinName(presentationForm.getJoinName());
		tPresentationCompany.setDeleteFlg(Constants.DB_FLG_FALSE);
		tPresentationCompany.setLastModifiedUser(loginUserDto.getLmsUserId());
		tPresentationCompany.setLastModifiedDate(now);
		tPresentationCompanyMapper.update(tPresentationCompany);
	}

}
