package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 企業・共有グループ紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TCompanyFssGroup {

	/** 企業・共有グループID */
	private Integer companyFssGroupId;
	/** 企業ID */
	private Integer companyId;
	/** 共有グループID */
	private Integer fssGroupId;
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
