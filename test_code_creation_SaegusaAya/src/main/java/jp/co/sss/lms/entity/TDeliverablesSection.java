package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 成果物・セクション紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TDeliverablesSection {

	/** 成果物セクションID */
	private Integer deliverablesSectionId;
	/** 提出期限 */
	private String submissionDeadline;
	/** 企業アカウントID */
	private Integer accountId;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回作成日 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日 */
	private Date lastModifiedDate;

}
