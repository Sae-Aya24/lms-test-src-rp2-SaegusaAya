package jp.co.sss.lms.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.StringTokenizer;

import org.springframework.stereotype.Component;

@Component
public class DateUtil {
	/**
	 * Date型日付を"yyyy/M/d"形式にフォーマット
	 * 
	 * @param date
	 * @return "yyyy/M/d"形式日付
	 */
	public String toString(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
		return sdf.format(date);
	}

	/**
	 * Date型日付を任意の日付形式にフォーマット
	 * 
	 * @param date
	 * @param fmt
	 * @return 任意の形式の日付
	 */
	public String toString(Date date, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.format(date);
	}

	/**
	 * String型日付を"yyyy/M/d"形式にフォーマット
	 * 
	 * @param date
	 * @return "yyyy/M/d"形式の日付
	 */
	public Date parse(String date) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/M/d");
		return sdf.parse(date);
	}

	/**
	 * String型日付を任意の日付形式にフォーマット
	 * 
	 * @param date
	 * @param fmt
	 * @return 任意の形式の日付
	 */
	public Date parse(String date, String fmt) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		return sdf.parse(date);
	}

	/**
	 * 誕生日と現在日付から年齢取得
	 * 
	 * @param birthday
	 * @return 年齢
	 */
	public int calcAge(Date birthday) {
		return calcAge(birthday, new Date());
	}

	/**
	 * 誕生日と任意の日付から年齢取得
	 * 
	 * @param birthday
	 * @param now
	 * @return 年齢
	 */
	public int calcAge(Date birthday, Date now) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
		return (Integer.parseInt(sdf.format(now)) - Integer.parseInt(sdf.format(birthday))) / 10000;
	}

	/**
	 * 第一引数の日付を第二引数の値に変更する<br>
	 * ex. time1=2001/12/01 16:25:31<br>
	 * time2=2015/05/24 12:00:00<br>
	 * の場合 戻り値は2015/02/24 16:25:31
	 * 
	 * @param t1
	 * @param t2
	 * @return
	 */
	public Timestamp replaceDate(Timestamp t1, Timestamp t2) {
		if (t1 == null || t2 == null) {
			return null;
		}

		Calendar cal1 = Calendar.getInstance();
		cal1.setTimeInMillis(t1.getTime());

		Calendar cal2 = Calendar.getInstance();
		cal2.setTimeInMillis(t2.getTime());

		cal1.set(Calendar.YEAR, cal2.get(Calendar.YEAR));
		cal1.set(Calendar.MONTH, cal2.get(Calendar.MONTH));
		cal1.set(Calendar.DATE, cal2.get(Calendar.DATE));

		return new Timestamp(cal1.getTimeInMillis());
	}

	/**
	 * 2つの日付の差を求めます。<br>
	 * 日付文字列 strDate1 - strDate2 が何日かを返します。
	 *
	 * @param strDate1 日付文字列 yyyy/M/d
	 * @param strDate2 日付文字列 yyyy/M/d
	 * @return 2つの日付の差
	 * @throws ParseException 日付フォーマットが不正な場合
	 */
	public int differenceDays(String strDate1, String strDate2) throws ParseException {
		return differenceDays(strDate1, strDate2, "yyyy/M/d");
	}

	/**
	 * 2つの日付の差を求めます。<br>
	 * 日付文字列 strDate1 - strDate2 が何日かを返します。
	 *
	 * @param strDate1 日付文字列 yyyy/M/d
	 * @param strDate2 日付文字列 yyyy/M/d
	 * @param fmt
	 * @return 2つの日付の差
	 * @throws ParseException 日付フォーマットが不正な場合
	 */
	public int differenceDays(String strDate1, String strDate2, String fmt) throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		Date date1 = sdf.parse(strDate1);
		Date date2 = sdf.parse(strDate2);
		return differenceDays(date1, date2);
	}

	/**
	 * 2つの日付の差を求めます。<br>
	 * java.util.Date 型の日付 date1 - date2 が何日かを返します。
	 *
	 * 計算方法は以下となります。<br>
	 * 1.最初に2つの日付を long 値に変換します。<br>
	 * ※この long 値は 1970 年 1 月 1 日 00:00:00 GMT からの経過ミリ秒数となります。 <br>
	 * 2.次にその差を求めます。<br>
	 * 3.上記の計算で出た数量を 1 日の時間で割ることで 日付の差を求めることができます。<br>
	 * ※1 日 ( 24 時間) は、86,400,000 ミリ秒です。
	 *
	 * @param date1 日付 java.util.Date
	 * @param date2 日付 java.util.Date
	 * @return 2つの日付の差
	 */
	public int differenceDays(Date date1, Date date2) {
		long datetime1 = date1.getTime();
		long datetime2 = date2.getTime();
		long one_date_time = 1000 * 60 * 60 * 24;
		long diffDays = (datetime1 - datetime2) / one_date_time;
		return (int) diffDays;
	}

	/**
	 * String型をCalendar型に変換
	 *
	 * @param arg 変換前の日付
	 * @param key 区切り文字
	 * @return
	 */
	public Calendar getCalendar(String arg, String key) {
		String[] strDate = arg.split(key);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(strDate[0]), Integer.parseInt(strDate[1]) - 1,
				Integer.parseInt(strDate[2]), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	/**
	 * String型をCalendar型に変換
	 *
	 * @param arg 変換前の日付
	 * @param key 区切り文字
	 * @param day 日
	 * @return
	 */
	public static Calendar getCalendar(String arg, String key, String day) {
		String[] strDate = arg.split(key);

		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(strDate[0]), Integer.parseInt(strDate[1]) - 1,
				Integer.parseInt(day), 0, 0, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar;
	}

	/**
	 * 現在日時取得
	 * 
	 * @return
	 */
	public String getCurrentDateString() {
		Date date = new Date();
		return dateToString(date);
	}

	/**
	 * 現在日時取得
	 * 
	 * @return
	 */
	public String getCurrentDateString(String fmt) {
		Date date = new Date();
		return dateToString(date, fmt);
	}

	/**
	 * Date型、Timestamp型をStringに変換
	 * 
	 * @param time
	 * @param format 日付のフォーマット(yyyy/MM/dd 等)
	 * @return
	 */
	public String dateToString(Date time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(time);
	}

	/**
	 * Date型、Timestamp型をStringに変換
	 * 
	 * @param time
	 * @param format 日付のフォーマット(yyyy/MM/dd 等)
	 * @return
	 */
	public String dateToStringJ(Date time, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format, Locale.JAPANESE);
		return sdf.format(time);
	}

	/**
	 * Date型、Timestamp型をStringに変換
	 * 
	 * @param time
	 * @return
	 */
	public String dateToString(Date time) {
		return dateToString(time, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * Timestamp型をCalendar型に変換
	 * 
	 * @param time
	 * @return
	 */
	public Calendar timestampToCalender(Timestamp time) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(time.getTime());
		return cal;
	}

	/**
	 * Calendar型をTimestamp型に変換
	 * 
	 * @param time
	 * @return
	 */
	public Timestamp calenderToTimestamp(Calendar cal) {
		Timestamp time = new Timestamp(cal.getTime().getTime());
		return time;
	}

	/**
	 * String型をTimestamp型（yyyy/MM/dd HH:mm:ss形式）に変換
	 * 
	 * @param time
	 * @return stringToTimestamp()
	 */
	public Timestamp stringToTimestamp(String time) {
		return stringToTimestamp(time, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * String型をTimestamp型（指定した形式）に変換
	 * 
	 * @param time
	 * @param fmt
	 * @return stringToTimestamp()
	 */
	public Timestamp stringToTimestamp(String time, String fmt) {
		Timestamp dateTime = null;
		try {
			Long dateTimeLong = new SimpleDateFormat(fmt).parse(time).getTime();
			dateTime = new Timestamp(dateTimeLong);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return dateTime;
	}

	/**
	 * String型をjava.sql.Date型（yyyy-MM-dd形式）に変換
	 * 
	 * @param date
	 * @return stringToTimestamp()
	 */
	public java.sql.Date stringToSqlDate(String date) {
		return stringToSqlDate(date, Constants.DEFAULT_DATE_FORMAT);
	}

	/**
	 * 文字列をDateに変換する。変換不可能な場合はnullを返却する。
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public java.sql.Date stringToSqlDate(String date, String format) {
		try {
			Date d = new SimpleDateFormat(format).parse(date);
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			cal.set(Calendar.HOUR_OF_DAY, 0);
			cal.set(Calendar.MINUTE, 0);
			cal.set(Calendar.SECOND, 0);
			cal.set(Calendar.MILLISECOND, 0);
			return new java.sql.Date(cal.getTimeInMillis());
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 文字列をDateに変換する。変換不可能な場合はnullを返却する。
	 * 
	 * @param date
	 * @param format
	 * @return
	 */
	public Date stringToDate(String date, String format) {
		try {
			Date d = new SimpleDateFormat(format).parse(date);
			return d;
		} catch (ParseException e) {
			return null;
		}
	}

	/**
	 * 指定された日付・時刻文字列を、可能であれば Calendarクラスに変換します。<br>
	 * 以下の形式の日付文字列を変換できます。
	 *
	 * ●変換可能な形式は以下となります。<br>
	 * yyyy/MM/dd yy/MM/dd yyyy-MM-dd yy-MM-dd yyyyMMdd
	 *
	 * 上記に以下の時間フィールドが組み合わされた状態 でも有効です。<br>
	 * HH:mm HH:mm:ss HH:mm:ss.SSS
	 *
	 * @param strDate 日付・時刻文字列。
	 * @return 変換後のCalendarクラス。
	 * @throws IllegalArgumentException 日付文字列が変換不可能な場合 または、矛盾している場合（例：2000/99/99）。
	 */
	public Calendar toCalendar(String strDate) {
		strDate = format(strDate);
		Calendar cal = Calendar.getInstance();
		cal.setLenient(false);

		int yyyy = Integer.parseInt(strDate.substring(0, 4));
		int MM = Integer.parseInt(strDate.substring(5, 7));
		int dd = Integer.parseInt(strDate.substring(8, 10));
		int HH = cal.get(Calendar.HOUR_OF_DAY);
		int mm = cal.get(Calendar.MINUTE);
		int ss = cal.get(Calendar.SECOND);
		int SSS = cal.get(Calendar.MILLISECOND);
		cal.clear();
		cal.set(yyyy, MM - 1, dd);
		int len = strDate.length();
		switch (len) {
		case 10:
			break;
		case 16: // yyyy/MM/dd HH:mm
			HH = Integer.parseInt(strDate.substring(11, 13));
			mm = Integer.parseInt(strDate.substring(14, 16));
			cal.set(Calendar.HOUR_OF_DAY, HH);
			cal.set(Calendar.MINUTE, mm);
			break;
		case 19: // yyyy/MM/dd HH:mm:ss
			HH = Integer.parseInt(strDate.substring(11, 13));
			mm = Integer.parseInt(strDate.substring(14, 16));
			ss = Integer.parseInt(strDate.substring(17, 19));
			cal.set(Calendar.HOUR_OF_DAY, HH);
			cal.set(Calendar.MINUTE, mm);
			cal.set(Calendar.SECOND, ss);
			break;
		case 23: // yyyy/MM/dd HH:mm:ss.SSS
			HH = Integer.parseInt(strDate.substring(11, 13));
			mm = Integer.parseInt(strDate.substring(14, 16));
			ss = Integer.parseInt(strDate.substring(17, 19));
			SSS = Integer.parseInt(strDate.substring(20, 23));
			cal.set(Calendar.HOUR_OF_DAY, HH);
			cal.set(Calendar.MINUTE, mm);
			cal.set(Calendar.SECOND, ss);
			cal.set(Calendar.MILLISECOND, SSS);
			break;
		default:
			throw new IllegalArgumentException("引数の文字列[" + strDate + "]は日付文字列に変換できません");
		}
		return cal;
	}

	/**
	 * 様々な日付、時刻文字列をデフォルトの日付・時刻フォーマット へ変換します。
	 *
	 * ●デフォルトの日付フォーマットは以下になります。 <br>
	 * 日付だけの場合：yyyy/MM/dd <br>
	 * 日付+時刻の場合：yyyy/MM/dd HH:mm:ss.SSS
	 *
	 * @param str 変換対象の文字列
	 * @return デフォルトの日付・時刻フォーマット
	 * @throws IllegalArgumentException 日付文字列が変換不可能な場合
	 */
	private String format(String str) {
		if (str == null || str.trim().length() < 8) {
			throw new IllegalArgumentException("引数の文字列[" + str + "]は日付文字列に変換できません");
		}

		str = str.trim();
		String yyyy = null;
		String MM = null;
		String dd = null;
		String HH = null;
		String mm = null;
		String ss = null;
		String SSS = null;

		// "-" or "/" が無い場合
		if (str.indexOf("/") == -1 && str.indexOf("-") == -1) {
			if (str.length() == 8) {
				yyyy = str.substring(0, 4);
				MM = str.substring(4, 6);
				dd = str.substring(6, 8);
				return yyyy + "/" + MM + "/" + dd;
			}
			yyyy = str.substring(0, 4);
			MM = str.substring(4, 6);
			dd = str.substring(6, 8);
			HH = str.substring(9, 11);
			mm = str.substring(12, 14);
			ss = str.substring(15, 17);
			return yyyy + "/" + MM + "/" + dd + " " + HH + ":" + mm + ":" + ss;
		}

		StringTokenizer token = new StringTokenizer(str, "_/-:. ");
		StringBuffer result = new StringBuffer();
		for (int i = 0; token.hasMoreTokens(); i++) {
			String temp = token.nextToken();
			switch (i) {
			case 0:// 年の部分
				yyyy = fillString(str, temp, "L", "20", 4);
				result.append(yyyy);
				break;
			case 1:// 月の部分
				MM = fillString(str, temp, "L", "0", 2);
				result.append("/" + MM);
				break;
			case 2:// 日の部分
				dd = fillString(str, temp, "L", "0", 2);
				result.append("/" + dd);
				break;
			case 3:// 時間の部分
				HH = fillString(str, temp, "L", "0", 2);
				result.append(" " + HH);
				break;
			case 4:// 分の部分
				mm = fillString(str, temp, "L", "0", 2);
				result.append(":" + mm);
				break;
			case 5:// 秒の部分
				ss = fillString(str, temp, "L", "0", 2);
				result.append(":" + ss);
				break;
			case 6:// ミリ秒の部分
				SSS = fillString(str, temp, "R", "0", 3);
				result.append("." + SSS);
				break;
			}
		}
		return result.toString();
	}

	private String fillString(String strDate, String str, String position, String addStr, int len) {
		if (str.length() > len) {
			throw new IllegalArgumentException("引数の文字列[" + strDate + "]は日付文字列に変換できません");
		}
		return fillString(str, position, len, addStr);
	}

	/**
	 * 文字列[str]に対して、補充する文字列[addStr]を [position]の位置に[len]に満たすまで挿入します。
	 *
	 * ※[str]がnullや空リテラルの場合でも[addStr]を [len]に満たすまで挿入した結果を返します。
	 * 
	 * @param str      対象文字列
	 * @param position 前に挿入 ⇒ L or l 後に挿入 ⇒ R or r
	 * @param len      補充するまでの桁数
	 * @param addStr   挿入する文字列
	 * @return 変換後の文字列。
	 */
	private String fillString(String str, String position, int len, String addStr) {
		if (addStr == null || addStr.length() == 0) {
			throw new IllegalArgumentException("挿入する文字列の値が不正です。addStr=" + addStr);
		}

		if (str == null) {
			str = "";
		}

		StringBuffer buffer = new StringBuffer(str);
		while (len > buffer.length()) {
			if (position.equalsIgnoreCase("l")) {
				int sum = buffer.length() + addStr.length();
				if (sum > len) {
					addStr = addStr.substring(0, addStr.length() - (sum - len));
					buffer.insert(0, addStr);
				} else {
					buffer.insert(0, addStr);
				}
			} else {
				buffer.append(addStr);
			}
		}

		if (buffer.length() == len) {
			return buffer.toString();
		}

		return buffer.toString().substring(0, len);
	}

	/**
	 * 日付部分のみを取得する。
	 * 
	 * @param date
	 * @return
	 */
	public Date getDateWithoutTime(Date date) {

		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);

		return new Date(cal.getTimeInMillis());
	}

	/**
	 * 時間を追加する。
	 * 
	 * @param date
	 * @param hour
	 * @return
	 */
	public Date addHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR_OF_DAY, hour);

		return new Date(cal.getTimeInMillis());

	}

	/**
	 * 月を追加する。
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public Date addMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);

		return new Date(cal.getTimeInMillis());

	}

	/**
	 * 月を追加する。
	 * 
	 * @param date
	 * @param month
	 * @return
	 */
	public Timestamp addMonthTimeStamp(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);

		return new Timestamp(cal.getTimeInMillis());
	}

	/**
	 * 第一引数～第二引数の間に跨いだ月の数を取得する。
	 * 
	 * @param before
	 * @param after
	 * @return
	 */
	public int getAcrossMonth(Timestamp before, Timestamp after) {
		Calendar openCal = timestampToCalender(before);
		openCal.set(Calendar.DATE, 1);
		Calendar closeCal = timestampToCalender(after);
		closeCal.set(Calendar.DATE, 1);

		int ret = 0;
		while (openCal.compareTo(closeCal) <= 0) {
			ret++;
			openCal.add(Calendar.MONTH, 1);
		}

		return ret;
	}

	/**
	 * 日付形式であるかチェックを行う。
	 * 
	 * @param dateStr
	 * @param fmt
	 * @return
	 */
	public boolean isDate(String dateStr) {
		return isDate(dateStr, "yyyy/MM/dd HH:mm:ss");
	}

	/**
	 * 日付形式であるかチェックを行う。
	 * 
	 * @param dateStr
	 * @param fmt
	 * @return
	 */
	public static boolean isDate(String dateStr, String fmt) {
		SimpleDateFormat sdf = new SimpleDateFormat(fmt);
		try {
			sdf.parse(dateStr);
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	/**
	 * 引数として渡された日付の月最終日を取得する。
	 * 
	 * @param date
	 * @return
	 */
	public Date getLastDayOfMonth(Date date) {
		Calendar cal = Calendar.getInstance();
		cal.setTimeInMillis(date.getTime());
		cal.set(Calendar.DATE, cal.getActualMaximum(Calendar.DATE));

		return new Date(cal.getTimeInMillis());

	}

	/**
	 * 日本語の曜日を取得する。
	 * 
	 * @param date
	 * @return
	 */
	public String getDayOfTheWeekShort(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);
		switch (dayOfWeek) {
		case Calendar.SUNDAY:
			return "日";
		case Calendar.MONDAY:
			return "月";
		case Calendar.TUESDAY:
			return "火";
		case Calendar.WEDNESDAY:
			return "水";
		case Calendar.THURSDAY:
			return "木";
		case Calendar.FRIDAY:
			return "金";
		case Calendar.SATURDAY:
			return "土";
		}
		throw new IllegalStateException();
	}

	/**
	 * Task.34 試験の解答時間の表示<br>
	 * ミリ秒から文字列の時間に変換（h時間m分s）
	 * 
	 * @param time 時間(ミリ秒)
	 * @return 時間
	 */
	public String getTimeFormat(long time) {
		long h = 1000 * 60 * 60;
		long m = 1000 * 60;
		long s = 1000;
		String timeFormat = "";
		if (time >= h) {
			long hour = time / h % 24;
			long minute = time / m % 60;
			long second = time / s % 60;
			timeFormat = String.format("%01d時間%01d分%01d秒", hour, minute, second);
		} else if (time >= m) {
			long minute = time / m % 60;
			long second = time / s % 60;
			timeFormat = String.format("%01d分%01d秒", minute, second);
		} else {
			long second = time / s % 60;
			timeFormat = String.format("%01d秒", second);
		}
		return timeFormat;
	}

}
