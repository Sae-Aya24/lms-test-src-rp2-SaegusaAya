package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * Task.108 面談スケジュールエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MMeetingSchedule {

	/** 面談スケジュールID */
	private Integer meetingScheduleId;
	/** 面談開始日 */
	private Date meetingOpenDate;
	/** 面談終了日 */
	private Date meetingCloseDate;
	/** 予約編集期限 */
	private Date editLimit;
	/** 用途 */
	private String purpose;
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
