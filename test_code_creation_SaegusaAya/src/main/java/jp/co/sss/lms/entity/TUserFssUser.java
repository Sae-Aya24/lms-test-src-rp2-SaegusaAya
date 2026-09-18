package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * ユーザ・共有ユーザ紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TUserFssUser {

	/** ユーザ・共有ユーザID */
	private Integer userFssUserId;
	/** ユーザID */
	private Integer userId;
	/** 共有ユーザID */
	private Integer fssUserId;
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
