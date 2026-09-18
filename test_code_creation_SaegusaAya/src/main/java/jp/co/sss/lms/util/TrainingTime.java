package jp.co.sss.lms.util;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import lombok.Getter;
import lombok.Setter;

/**
 * 研修時刻ユーティリティ
 * 
 * @author 東京ITスクール
 */
@Component
@Getter
@Setter
public class TrainingTime implements Comparable<TrainingTime>, Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

	/** 時間 */
	private Integer hour;
	/** 分 */
	private Integer minute;

	/**
	 * 現在時刻でインスタンスを作成する
	 */
	public TrainingTime() {
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
		String timeStr = sdf.format(new Date());
		setTimeStr(timeStr);
	}

	/**
	 * 指定時刻でインスタンスを作成
	 * 
	 * @param hour
	 * @param minute
	 */
	public TrainingTime(Integer hour, Integer minute) {
		if (!isValidTrainingTime(hour, minute)) {
			throw new IllegalArgumentException();
		}
		this.hour = hour;
		this.minute = minute;
	}

	/**
	 * インスタンス可能か返却する
	 * 
	 * @param timeStr
	 * @return boolean
	 */
	public boolean isValidTrainingTime(String timeStr) {
		if (StringUtils.isEmpty(timeStr)) {
			return true;
		}
		Integer hour, minute;
		if (timeStr.indexOf(":") >= 0) {
			String[] times = timeStr.split(":");
			final String numRegex = "[0-9]+";
			if (times.length == 2 && times[0].matches(numRegex) && times[1].matches(numRegex)) {
				hour = Integer.parseInt(times[0]);
				minute = Integer.parseInt(times[1]);
				return isValidTrainingTime(hour, minute);
			} else {
				return false;
			}
		} else if (timeStr.matches("[0-9]{4}")) {
			// 数値4桁の場合
			String hourStr = timeStr.substring(0, 2);
			String minuteStr = timeStr.substring(2, 4);
			hour = Integer.parseInt(hourStr);
			minute = Integer.parseInt(minuteStr);
			return isValidTrainingTime(hour, minute);
		} else {
			return false;
		}
	}

	/**
	 * インスタンス可能か返却する
	 * 
	 * @param hour
	 * @param minute
	 * @return boolean
	 */
	public boolean isValidTrainingTime(Integer hour, Integer minute) {
		if (hour == null || minute == null) {
			return false;
		}
		if (minute > 59 || minute < 0) {
			return false;
		}
		return true;
	}

	/**
	 * 文字列からインスタンス化する。 有効フォーマット："9:00", "18:00", "0900", "1800"
	 *
	 * @param timeStr
	 */
	public TrainingTime(String timeStr) {
		if (StringUtils.isEmpty(timeStr)) {
			return;
		}
		setTimeStr(timeStr);
	}

	/**
	 * 時刻を設定
	 *
	 * @param timeStr
	 */
	private void setTimeStr(String timeStr) {
		// 有効フォーマットかどうかチェックを行う
		if (!isValidTrainingTime(timeStr)) {
			throw new IllegalArgumentException(timeStr + " is Illegal.");
		}
		Integer hour, minute;
		if (timeStr.indexOf(":") >= 0) {
			String[] times = timeStr.split(":");
			// isValidTrainingTimeでチェック済のためそのまま変換する
			hour = Integer.parseInt(times[0]);
			minute = Integer.parseInt(times[1]);
		} else if (timeStr.matches("[0-9]{4}")) {
			// 数値4桁の場合
			String hourStr = timeStr.substring(0, 2);
			String minuteStr = timeStr.substring(2, 4);
			hour = Integer.parseInt(hourStr);
			minute = Integer.parseInt(minuteStr);
		} else {
			throw new IllegalArgumentException();
		}
		// 数値的なチェックを行う
		if (!isValidTrainingTime(hour, minute)) {
			throw new IllegalArgumentException();
		}
		this.hour = hour;
		this.minute = minute;
	}

	/**
	 * 値が (this + augend) の TrainingTime を返します。
	 * 
	 * @param augend - この TrainingTime に加算する値
	 * @return this + augend
	 */
	public TrainingTime add(TrainingTime augend) {
		Integer retHour = this.hour;
		Integer retMinute = this.minute;
		retHour += augend.hour;
		retMinute += augend.minute;
		while (retMinute >= 60) {
			retHour += 1;
			retMinute -= 60;
		}
		return new TrainingTime(retHour, retMinute);
	}

	/**
	 * 値が (this - augend) の TrainingTime を返します。
	 * 
	 * @param augend - この TrainingTime から減産する値
	 * @return this - augend
	 */
	public TrainingTime subtract(TrainingTime subtrahend) {
		if (this.compareTo(subtrahend) > 0) {
			Integer retHour = this.hour;
			Integer retMinute = this.minute;
			retHour -= subtrahend.hour;
			retMinute -= subtrahend.minute;
			while (retHour > 0 && retMinute < 0) {
				retHour -= 1;
				retMinute += 60;
			}
			return new TrainingTime(retHour, retMinute);
		} else if (this.equals(subtrahend)) {
			return new TrainingTime(0, 0);
		} else {
			throw new UnsupportedOperationException("未実装");
		}
	}

	/**
	 * 与えられた引数のうち、最大を返却します
	 * 
	 * @param times
	 * @return
	 * @see #compareTo(TrainingTime)
	 */
	public TrainingTime max(TrainingTime... times) {
		if (times.length <= 0) {
			return null;
		}
		TrainingTime maxTime = times[0];
		for (int i = 1; i < times.length; i++) {
			if (maxTime.compareTo(times[i]) < 0) {
				maxTime = times[i];
			}
		}
		return maxTime;
	}

	/**
	 * 与えられた引数のうち、最小を返却します
	 * 
	 * @param times
	 * @return
	 * @see #compareTo(TrainingTime)
	 */
	public TrainingTime min(TrainingTime... times) {
		if (times.length <= 0) {
			return null;
		}
		TrainingTime minTime = times[0];
		for (int i = 1; i < times.length; i++) {
			if (minTime.compareTo(times[i]) > 0) {
				minTime = times[i];
			}
		}
		return minTime;
	}

	/**
	 * zero paddingして、HH:mm形式で返却
	 */
	@Override
	public String toString() {
		return this.getFormattedString();
	}

	/**
	 * 指定した研修時間インスタンスが一致しているか判定
	 * 
	 * @param anObject
	 * @return boolean
	 */
	@Override
	public boolean equals(Object anObject) {
		if (anObject instanceof TrainingTime) {
			TrainingTime target = (TrainingTime) anObject;
			if (target.hour == this.hour && target.minute == this.minute) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * 指定した研修時間インスタンスの比較
	 * 
	 * @param anotherTrainingTime
	 * @return 比較結果
	 */
	@Override
	public int compareTo(TrainingTime anotherTrainingTime) {
		if (this.isBlank() && anotherTrainingTime != null && !anotherTrainingTime.isBlank()) {
			return -1;
		}
		if (!this.isBlank() && (anotherTrainingTime == null || anotherTrainingTime.isBlank())) {
			return 1;
		}
		if (this.isBlank() && (anotherTrainingTime == null || anotherTrainingTime.isBlank())) {
			return 0;
		}
		if (this.equals(anotherTrainingTime)) {
			return 0;
		}
		if (this.hour != anotherTrainingTime.hour) {
			return this.hour.compareTo(anotherTrainingTime.hour);
		}
		return this.minute.compareTo(anotherTrainingTime.minute);
	}

	/**
	 * フィールド変数に値が設定されているかどうか
	 * 
	 * @return boolean
	 */
	public boolean isBlank() {
		return (this.hour == null || this.minute == null);
	}

	/**
	 * フィールド変数に値が設定されていないかどうか
	 * 
	 * @return boolean
	 */
	public boolean isNotBlank() {
		return !isBlank();
	}

	/**
	 * 時刻をHH:mm形式で返却
	 * 
	 * @return HH:mm
	 */
	public String getFormattedString() {
		if (hour == null || minute == null) {
			return "";
		} else {
			return String.format("%1$02d:%2$02d", hour, minute);
		}
	}

	/**
	 * 分を15分刻みで切り上げる。出社に仕様
	 * 
	 * @return
	 */
	public TrainingTime roundUp() {
		if (between(this.minute, 46, 59)) {
			this.hour++;
			this.minute = 0;
		} else if (between(this.minute, 1, 15)) {
			this.minute = 15;
		} else if (between(this.minute, 16, 30)) {
			this.minute = 30;
		} else if (between(this.minute, 31, 45)) {
			this.minute = 45;
		}
		return this;
	}

	/**
	 * 分を15分刻みで切り捨てる。退社に仕様
	 * 
	 * @return
	 */
	public TrainingTime roundDown() {
		if (between(this.minute, 45, 59)) {
			this.minute = 45;
		} else if (between(this.minute, 0, 14)) {
			this.minute = 0;
		} else if (between(this.minute, 15, 29)) {
			this.minute = 15;
		} else if (between(this.minute, 30, 44)) {
			this.minute = 30;
		}
		return this;
	}

	/**
	 * 指定された数値間か判定
	 * 
	 * @param minute
	 * @param a
	 * @param b
	 * 
	 * @return boolean
	 */
	private boolean between(int minute, int a, int b) {
		if (a > b) {
			int tmp = b;
			b = a;
			a = tmp;
		}
		return (minute >= a && minute <= b);
	}

	/**
	 * 空か判定
	 * 
	 * @return boolean
	 */
	public boolean isEmpty() {
		return hour == null || minute == null;
	}

	/**
	 * 企業の休憩取得時間を考慮
	 * 
	 * @return
	 */
	public String restTimeZone(TrainingTime total, TrainingTime companyRestStartTime,
			TrainingTime companyRestEndTime) {
		int startTime = Integer.parseInt(companyRestStartTime.toString().replace(":", ""));
		int endTime = Integer.parseInt(companyRestEndTime.toString().replace(":", ""));

		int startTimeMin = startTime / 100 * 60 + startTime % 100;
		int endTimeMin = endTime / 100 * 60 + endTime % 100;
		int diffMin = endTimeMin - startTimeMin;
		String chinginTime = total.toString();

		// 企業規定の休憩取得時間が1時間を超える場合、totalから超過分を差し引く
		if (diffMin > 60) {
			int totalMin = Integer.parseInt(total.toString().replace(":", ""));
			;
			totalMin = totalMin / 100 * 60 + totalMin % 100;
			diffMin -= 60;
			totalMin -= diffMin;
			totalMin = totalMin / 60 * 100 + totalMin % 60;

			// 元のフォーマットに戻す
			String strTotalMin = String.valueOf(totalMin);
			if (strTotalMin.length() < 4) {
				StringBuffer sb = new StringBuffer();
				sb.append("0");
				sb.append(strTotalMin);
				chinginTime = sb.toString();
			}
		}
		return chinginTime;
	}

}
