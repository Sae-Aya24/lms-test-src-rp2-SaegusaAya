package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * ユーザーマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MUser {

	/** ユーザID */
	private Integer userId;
	/** ログインID */
	private String loginId;
	/** パスワード */
	private String password;
	/** ユーザー名 */
	private String userName;
	/** 権限 */
	private String authority;
	/** 企業アカウントID */
	private Integer accountId;
	/** セキュリティ同意フラグ */
	private Short securityAgreeFlg;
	/** パスワード変更日付 */
	private Date passwordChangeDate;
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
	/** よみがな */
	private String kana;
	/** メールアドレス */
	private String mailAddress;
	/** 助成金カテゴリID */
	private Integer subsidyCategoryId;
	/** 途中退校フラグ */
	private Short leaveFlg;
	/** 途中退校日 */
	private Date leaveDate;

}
