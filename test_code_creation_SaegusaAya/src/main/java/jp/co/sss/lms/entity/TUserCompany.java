package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * ユーザー・企業紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TUserCompany {

	/** ユーザー・企業紐付けID */
	private Integer userCompanyId;
	/** LMSユーザID */
	private Integer lmsUserId;
	/** 企業ID */
	private Integer companyId;
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
