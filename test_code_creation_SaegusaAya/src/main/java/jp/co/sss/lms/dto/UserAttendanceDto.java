package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * ユーザー勤怠情報DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class UserAttendanceDto {

	/** 会場ID */
	private Integer placeId;
	/** LMSユーザID */
	private Integer lmsUserId;
	/** ユーザー名 */
	private String userName;
	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** 企業入力勤怠情報ID */
	private Integer companyAttendanceId;
	/** 受講生入力勤怠情報ID */
	private Integer studentAttendanceId;
	/** 日付 */
	private Date trainingDate;
	/** 出勤時間 */
	private String trainingStartTime;
	/** 退勤時間 */
	private String trainingEndTime;
	/** 中抜け時間 */
	private Integer blankTime;
	/** 勤怠状態 */
	private Short status;
	/** 備考 */
	private String note;

}
