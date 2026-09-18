package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 勤怠管理(受講生)テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TStudentAttendance {

	/** 受講生入力勤怠情報ID */
	private Integer studentAttendanceId;
	/** LMSユーザID */
	private Integer lmsUserId;
	/** 日付 */
	private Date trainingDate;
	/** 出勤時間 */
	private String trainingStartTime;
	/** 退勤時間 */
	private String trainingEndTime;
	/** 勤怠状態 */
	private Short status;
	/** 備考 */
	private String note;
	/** 企業アカウントID */
	private Integer accountId;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回作成日時 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日時 */
	private Date lastModifiedDate;
	/** 中抜け時間 */
	private Integer blankTime;

}
