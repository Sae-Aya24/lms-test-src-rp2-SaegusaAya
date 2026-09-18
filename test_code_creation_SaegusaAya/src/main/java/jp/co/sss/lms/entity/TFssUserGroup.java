package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 共有ユーザ・グループ紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TFssUserGroup {

	/** 共有ユーザ・グループID */
	private Integer fssUserGroupId;
	/** 共有ユーザID */
	private Integer fssUserId;
	/** 共有グループID */
	private Integer fssGroupId;
	/** 権限 */
	private Short auth;
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
