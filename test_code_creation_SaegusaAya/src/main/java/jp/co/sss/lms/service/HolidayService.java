package jp.co.sss.lms.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.MHoliday;
import jp.co.sss.lms.form.HolidayForm;
import jp.co.sss.lms.mapper.MHolidayMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.MessageUtil;

/**
 * Task.86 休日情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class HolidayService {

	@Autowired
	private MHolidayMapper mHolidayMapper;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * Task.86 休日情報取得
	 * 
	 * @param holidayForm
	 * @throws ParseException
	 */
	public void setHolidayForm(HolidayForm holidayForm) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");

		// 休日リストの取得
		if (holidayForm.getStartDate() == null) {
			Date today = new Date();
			String startDate = sdf.format(today);
			holidayForm.setStartDate(startDate);
		}
		List<MHoliday> mHolidayList = mHolidayMapper.findByHolidayDate(holidayForm.getStartDate(),
				Constants.DB_FLG_FALSE);

		// 配列の初期化
		Calendar calendar = Calendar.getInstance();
		Date date = sdf.parse(holidayForm.getStartDate());
		calendar.setTime(date);
		Integer lastDayOfMonth = calendar.getActualMaximum(Calendar.DATE);
		holidayForm.setHolidayId(new Integer[lastDayOfMonth]);
		holidayForm.setDay(new String[lastDayOfMonth]);
		holidayForm.setHolidayName(new String[lastDayOfMonth]);
		holidayForm.setCancelFlg(new Short[lastDayOfMonth]);
		holidayForm.setHolidayFlg(new Short[lastDayOfMonth]);

		// 休日リストの設定
		SimpleDateFormat sdfYMD = new SimpleDateFormat("yyyy年M月d日");
		for (int i = 0; i < lastDayOfMonth; i++) {
			calendar.set(Calendar.DATE, i + 1);
			holidayForm.getDay()[i] = sdfYMD.format(calendar.getTime());
			holidayForm.getHolidayFlg()[i] = 0;
			holidayForm.getCancelFlg()[i] = 0;
			for (MHoliday mHoliday : mHolidayList) {
				if (calendar.getTime().compareTo(mHoliday.getHolidayDate()) == 0) {
					holidayForm.getHolidayId()[i] = mHoliday.getHolidayId();
					holidayForm.getHolidayName()[i] = mHoliday.getHolidayName();
					holidayForm.getHolidayFlg()[i] = 1;
					break;
				}
			}
		}
	}

	/**
	 * Task.86 登録時の入力チェック
	 * 
	 * @param holidayForm
	 * @param result
	 */
	public void registInputCheck(HolidayForm holidayForm, BindingResult result) {
		for (int i = 0; i < holidayForm.getHolidayName().length; i++) {
			if (!StringUtils.isBlank(holidayForm.getHolidayName()[i])
					&& holidayForm.getHolidayName()[i].length() > 20) {
				result.addError(new FieldError(result.getObjectName(), "holidayName[" + i + "]",
						"* " + messageUtil.getMessage(Constants.VALID_KEY_MAXLENGTH,
								new String[] { "休日名", "20" })));
			}
		}
	}

	/**
	 * Task.86 休日情報登録
	 * 
	 * @param holidayForm
	 * @throws ParseException
	 */
	public String regist(HolidayForm holidayForm) throws ParseException {

		SimpleDateFormat sdf = new SimpleDateFormat("yyyy年M月d日");
		Date now = new Date();
		for (int i = 0; i < holidayForm.getHolidayId().length; i++) {
			if (holidayForm.getHolidayFlg()[i] == 1) {
				if (holidayForm.getCancelFlg()[i] == 1) {
					mHolidayMapper.delete(holidayForm.getHolidayId()[i]);
				}
			} else {
				if (!StringUtils.isBlank(holidayForm.getHolidayName()[i])) {
					MHoliday mHoliday = new MHoliday();
					mHoliday.setHolidayName(holidayForm.getHolidayName()[i]);
					mHoliday.setHolidayDate(sdf.parse(holidayForm.getDay()[i]));
					mHoliday.setAccountId(loginUserDto.getAccountId());
					mHoliday.setDeleteFlg(Constants.DB_FLG_FALSE);
					mHoliday.setFirstCreateUser(loginUserDto.getLmsUserId());
					mHoliday.setFirstCreateDate(now);
					mHoliday.setLastModifiedUser(loginUserDto.getLmsUserId());
					mHoliday.setLastModifiedDate(now);
					mHolidayMapper.insert(mHoliday);
				}
			}
		}
		return messageUtil.getMessage(Constants.PROP_KEY_REGIST_COMPLETE, new String[] { "休日" });
	}

}
