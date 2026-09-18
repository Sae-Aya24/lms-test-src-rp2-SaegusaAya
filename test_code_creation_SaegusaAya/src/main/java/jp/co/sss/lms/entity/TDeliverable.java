package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 成果物テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TDeliverable {

	/** 成果物ID */
	private Integer deliverableId;
	/** 成果物名 */
	private String deliverableName;
	/** 課題ID */
	private Integer ticketId;
	/** lLMSユーザID */
	private Integer lmsUserId;
	/** 削除フラグ */
	private Short status;
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

}
