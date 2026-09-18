package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * ログイン情報エンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class LoginUser {

	/** 企業アカウントID */
	private Integer accountId;
	/** ユーザーID */
	private Integer userId;
	/** LMSユーザーID */
	private Integer lmsUserId;
	/** ユーザー名 */
	private String userName;
	/** ロール */
	private String role;
	/** 企業ID */
	private Integer companyId;
	/** 企業名 */
	private String companyName;
	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 会場非表示フラグ */
	private Short hiddenFlg;
	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** セキュリティ同意フラグ */
	private Short securityAgreeFlg;
	/** パスワード変更日付 */
	private Date passwordChangeDate;
	/** サポート表示 */
	private Short supportAvailable;
	/** メールアドレス */
	private String mailAddress;
	/** 途中退校フラグ */
	private Integer leaveFlg;
	/** 途中退校日 */
	private Date leaveDate;
	/** ファイル共有フラグ */
	private Short fileShareFlg;

}
