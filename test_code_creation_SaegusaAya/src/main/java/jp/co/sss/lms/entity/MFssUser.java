package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 共有ユーザマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MFssUser {

	/** 共有ユーザID */
	private Integer fssUserId;
	/** ユーザ名 */
	private String nickname;
	/** 最大合計ファイルサイズ */
	private Integer maxFileAmount;
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
