package jp.co.sss.lms.form;

import lombok.Data;

/**
 * 日次の勤怠フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyAttendanceForm {

	/** 受講生勤怠ID */
	private Integer studentAttendanceId;
	/** 退校日 */
	private String leaveDate;
	/** 日付 */
	private String trainingDate;
	/** 開講日 */
	private String trainingStartTime;
	/** 閉講日 */
	private String trainingEndTime;
	/** 出勤時間 */
	private Integer trainingStartTimeHour;
	/** 出勤分 */
	private Integer trainingStartTimeMinute;
	/** 退勤時間 */
	private Integer trainingEndTimeHour;
	/** 退勤分 */
	private Integer trainingEndTimeMinute;
	/** 中抜け時間 */
	private Integer blankTime;
	/** 中抜け時間（値） */
	private String blankTimeValue;
	/** ステータス */
	private String status;
	/** 備考 */
	private String note;
	/** 欠席フラグ */
	private Boolean isAbsent;
	/** 出勤時間（入力値） */
	private String trainingStartTimeRaw;
	/** 退勤時間（入力値） */
	private String trainingEndTimeRaw;
	/** 出勤時間（調整後） */
	private String trainingStartTimeRounded;
	/** 退勤時間（調整後） */
	private String trainingEndTimeRounded;
	/** 備考（入力値） */
	private String noteRaw;
	/** セクション名 */
	private String sectionName;
	/** 当日フラグ */
	private Boolean isToday;
	/** エラーフラグ */
	private Boolean isError;
	/** 日付（表示用） */
	private String dispTrainingDate;
	/** ステータス（表示用） */
	private String statusDispName;
	/** LMSユーザーID */
	private String lmsUserId;
	/** ユーザー名 */
	private String userName;
	/** コース名 */
	private String courseName;
	/** インデックス */
	private String index;
	/** 企業勤怠ID */
	private Integer companyAttendanceId;

}
