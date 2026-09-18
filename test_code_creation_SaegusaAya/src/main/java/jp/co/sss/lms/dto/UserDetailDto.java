package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * ユーザー基本情報DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class UserDetailDto {
	/** LMSユーザーID */
	private Integer lmsUserId;
	/** ユーザーID */
	private Integer userId;
	/** ユーザー名 */
	private String userName;
	/** よみがな */
	private String kana;
	/** ロール */
	private String role;
	/** ログインID */
	private String loginId;
	/** 研修を通じてどのようになってほしいか */
	private String hopeViaTraning;
	/** プログラム経験 */
	private Short programmingExperience;
	/** 企業名 */
	private String companyName;
	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** 会場名 */
	private String placeName;
	/** 備考 */
	private String placeNote;
	/** 会場詳細 */
	private String placeDescription;
	/** 企業アカウントID */
	private Integer accountId;
	/** 途中退校フラグ */
	private Short leaveFlg;
	/** 途中退校日 */
	private Date leaveDate;
	/** 助成金カテゴリID */
	private Integer subsidyCategoryId;

}
