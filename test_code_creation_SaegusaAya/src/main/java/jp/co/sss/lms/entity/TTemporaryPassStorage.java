package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * パスワード変更テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TTemporaryPassStorage {

	/** 変更情報ID */
	private Integer temporaryPassStorageId;
	/** ユーザーID */
	private Integer userId;
	/** 変更キー */
	private String changeKey;
	/** 期限 */
	private Date timeLimit;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/*** 初回作成日 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日 */
	private Date lastModifiedDate;

}
