package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 契約同意エンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TAgreementConsent {

	/** 契約同意ID */
	private Integer agreementConsentId;
	/** 契約内容ID */
	private Integer agreementId;
	/** 企業・コース紐付けID */
	private Integer companyCourseId;
	/** 同意フラグ */
	private Short consentFlg;
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
