package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * Task.111 面談スケジュール詳細エンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MMeetingScheduleDetail {

	/** 面談スケジュール詳細ID */
	private Integer meetingScheduleDetailId;
	/** 面談スケジュールID */
	private Integer meetingScheduleId;
	/** 面談日付 */
	private Date meetingDate;
	/** 面談時刻 */
	private String meetingTime;
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

}
