package jp.co.sss.lms.dto;

import java.io.Serializable;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

import lombok.Data;

/**
 * ログイン情報DTO
 * 
 * @author 東京ITスクール
 */
@Component
@SessionScope
@Data
public class LoginUserDto implements Serializable {

	/** シリアルバージョンUID */
	private static final long serialVersionUID = 1L;

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
	/** パスワードNG回数 */
	private Integer passwordNgCount;
	/** パスワード変更日付 */
	private Date passwordChangeDate;
	/** パスワードNG日付 */
	private String passwordNgDate;
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
