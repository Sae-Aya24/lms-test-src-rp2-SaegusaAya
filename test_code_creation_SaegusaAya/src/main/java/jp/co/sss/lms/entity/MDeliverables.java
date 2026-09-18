package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 成果物マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MDeliverables {

	/** 成果物ID */
	private Integer deliverablesId;
	/** 成果物名 */
	private String deliverablesName;
	/** 採点フラグ */
	private Short scoreFlg;
	/** フィードバックフラグ */
	private Short feedbackFlg;
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
	/** 非表示フラグ */
	private Short hiddenFlg;

}
