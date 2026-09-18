package jp.co.sss.lms.util;

import java.text.ParseException;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.sss.lms.enums.AttendanceStatusEnum;
import jp.co.sss.lms.mapper.MSectionMapper;

/**
 * 勤怠管理のユーティリティクラス
 * 
 * @author 東京ITスクール
 */
@Component
public class AttendanceUtil {

	@Autowired
	private DateUtil dateUtil;
	@Autowired
	private TrainingTime trainingTime;
	@Autowired
	private MSectionMapper mSectionMapper;

	/**
	 * SSS定時・出退勤時間を元に、遅刻早退を判定をする
	 * 
	 * @param trainingStartTime 開始時刻
	 * @param trainingEndTime   終了時刻
	 * @return 遅刻早退を判定メソッド
	 */
	public AttendanceStatusEnum getStatus(TrainingTime trainingStartTime,
			TrainingTime trainingEndTime) {
		return getStatus(trainingStartTime, trainingEndTime, Constants.SSS_WORK_START_TIME,
				Constants.SSS_WORK_END_TIME);
	}

	/**
	 * 与えられた定時・出退勤時間を元に、遅刻早退を判定する
	 * 
	 * @param trainingStartTime 開始時刻
	 * @param trainingEndTime   終了時刻
	 * @param workStartTime     定時開始時刻
	 * @param workEndTime       定時終了時刻
	 * @return 判定結果
	 */
	private AttendanceStatusEnum getStatus(TrainingTime trainingStartTime,
			TrainingTime trainingEndTime, TrainingTime workStartTime, TrainingTime workEndTime) {
		// 定時が不明な場合、NONEを返却する
		if (workStartTime == null || workStartTime.isBlank() || workEndTime == null
				|| workEndTime.isBlank()) {
			return AttendanceStatusEnum.NONE;
		}
		boolean isLate = false, isEarly = false;
		// 定時より1分以上遅く出社していたら遅刻(＝はセーフ)
		if (trainingStartTime != null && trainingStartTime.isNotBlank()) {
			isLate = (trainingStartTime.compareTo(workStartTime) > 0);
		}
		// 定時より1分以上早く退社していたら早退(＝はセーフ)
		if (trainingEndTime != null && trainingEndTime.isNotBlank()) {
			isEarly = (trainingEndTime.compareTo(workEndTime) < 0);
		}
		if (isLate && isEarly) {
			return AttendanceStatusEnum.TARDY_AND_LEAVING_EARLY;
		}
		if (isLate) {
			return AttendanceStatusEnum.TARDY;
		}
		if (isEarly) {
			return AttendanceStatusEnum.LEAVING_EARLY;
		}
		return AttendanceStatusEnum.NONE;
	}

	/**
	 * 中抜け時間を時(hour)と分(minute)に変換
	 *
	 * @param min 中抜け時間
	 * @return 時(hour)と分(minute)に変換したクラス
	 */
	public TrainingTime calcBlankTime(int min) {
		int hour = min / 60;
		int minute = min % 60;
		TrainingTime total = new TrainingTime(hour, minute);
		return total;
	}

	/**
	 * 時刻分を丸めた本日日付を取得
	 * 
	 * @return "yyyy/M/d"形式の日付
	 */
	public Date getTrainingDate() {
		Date trainingDate;
		try {
			trainingDate = dateUtil.parse(dateUtil.toString(new Date()));
		} catch (ParseException e) {
			// DateUtil#toStringとparseは同様のフォーマットを使用しているため、起こりえないエラー
			throw new IllegalStateException();
		}
		return trainingDate;
	}

	/**
	 * 休憩時間取得
	 * 
	 * @return 休憩時間
	 */
	public LinkedHashMap<Integer, String> setBlankTime() {
		LinkedHashMap<Integer, String> map = new LinkedHashMap<>();
		map.put(null, "");
		for (int i = 15; i < 480;) {
			int hour = i / 60;
			int minute = i % 60;
			String time;

			if (hour == 0) {
				time = minute + "分";

			} else if (minute == 0) {
				time = hour + "時間";
			} else {
				time = hour + "時" + minute + "分";
			}

			map.put(i, time);

			i = i + 15;

		}
		return map;
	}

	/**
	 * Task.26 時間のプルダウンマップを生成
	 * 
	 * @return 時間のプルダウンマップ
	 */
	public LinkedHashMap<Integer, String> getHourMap() {
		LinkedHashMap<Integer, String> hourMap = new LinkedHashMap<>();
		hourMap.put(null, "");

		for (int i = 0; i < 24; i++) {
			hourMap.put(i, String.format("%02d", i));
		}

		return hourMap;
	}

	/**
	 * Task.26 分のプルダウンマップを生成
	 * 
	 * @return 分のプルダウンマップ
	 */
	public LinkedHashMap<Integer, String> getMinuteMap() {
		LinkedHashMap<Integer, String> minuteMap = new LinkedHashMap<>();
		minuteMap.put(null, "");

		for (int i = 0; i < 60; i++) {
			minuteMap.put(i, String.format("%02d", i));
		}

		return minuteMap;
	}

	/**
	 * Task.26 時間(時)の切り出し
	 * 
	 * @param trainingTime 開始時刻or終了時刻
	 * @return 切り出された時間(時)
	 */
	public Integer getHour(String trainingTime) {
		if (StringUtils.isBlank(trainingTime)) {
			return null;
		}
		return Integer.parseInt(trainingTime.substring(0, 2));
	}

	/**
	 * Task.26 時間(分)の切り出し
	 * 
	 * @param trainingTime 開始時刻or終了時刻
	 * @return 切り出された時間(分)
	 */
	public Integer getMinute(String trainingTime) {
		if (StringUtils.isBlank(trainingTime)) {
			return null;
		}
		return Integer.parseInt(trainingTime.substring(trainingTime.length() - 2));
	}

	/**
	 * Task.26 受講時間数を算出する
	 * 
	 * @param trainingStartTime
	 * @param trainingEndTime
	 * @return 受講合計時間数
	 */
	public TrainingTime calcJukoTime(TrainingTime trainingStartTime, TrainingTime trainingEndTime) {
		// 就業時間とSSS定時のうち遅い方を、受講開始時間とする
		TrainingTime startTime = trainingTime.max(trainingStartTime, Constants.SSS_WORK_START_TIME);
		// 就業時間とSSS定時のうち早い方を、受講終了時間とする
		TrainingTime endTime = trainingTime.min(trainingEndTime, Constants.SSS_WORK_END_TIME);
		// SSS休憩開始と就業時間のうち早いのを、午前の終了時間とする
		TrainingTime firstHalfEndTime = trainingTime.min(trainingEndTime,
				Constants.SSS_REST_START_TIME);
		// SSS休憩終了と就業時間のうち遅いのを、午後の開始時間とする
		TrainingTime lastHalfStartTime = trainingTime.max(trainingStartTime,
				Constants.SSS_REST_END_TIME);

		// 前半と、後半の有効時間を足した結果を取得
		TrainingTime total = calcAvailableTime(startTime, endTime, firstHalfEndTime,
				lastHalfStartTime);
		return total;
	}

	/**
	 * Task.26 中抜け時間(文字列)を数字に変換
	 *
	 * @param min
	 * @return brankTime
	 */
	public Integer reverseBlankTime(String time) {
		TrainingTime trainingTime = new TrainingTime(time);

		int hour = trainingTime.getHour() * 60;
		int minute = trainingTime.getMinute();

		int brankTime = hour + minute;

		return brankTime;
	}

	/**
	 * 研修日の判定
	 * 
	 * @param courseId
	 * @param trainingDate
	 * @return 判定結果
	 */
	public boolean isWorkDay(Integer courseId, Date trainingDate) {
		Integer count = mSectionMapper.getSectionCountByCourseId(courseId, trainingDate);
		if (count > 0) {
			return true;
		}
		return false;
	}

	/**
	 * 午前午後の合計勤務時間を算出
	 *
	 * @param startTime
	 * @param endTime
	 * @param firstHalfEndTime
	 * @param lastHalfStartTime
	 * @return total
	 */
	private TrainingTime calcAvailableTime(TrainingTime startTime, TrainingTime endTime,
			TrainingTime firstHalfEndTime, TrainingTime lastHalfStartTime) {
		TrainingTime firstHalf = new TrainingTime(0, 0);
		TrainingTime lastHalf = new TrainingTime(0, 0);

		// 賃金対象開始時間が午前終了時間より前の場合、午前の勤務分を算出
		if (firstHalfEndTime.compareTo(startTime) > 0) {
			// 午前の勤務時間 = 午前終了時間 - 賃金対象開始時間
			firstHalf = firstHalfEndTime.subtract(startTime);
		}
		// 賃金対象終了時間が午後開始時間より後の場合、午後の勤務分を算出
		if (lastHalfStartTime.compareTo(endTime) < 0) {
			// 午後の勤務時間 = 賃金対象終了時間 - 午後開始時間
			lastHalf = endTime.subtract(lastHalfStartTime);
		}
		TrainingTime total = firstHalf.add(lastHalf);
		return total;
	}

}
