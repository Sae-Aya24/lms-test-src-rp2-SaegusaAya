package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Task.69 受講生更新フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class StudentUpdateForm {

	/** LMSユーザーID */
	private Integer lmsUserId;
	/** ユーザーID */
	private Integer userId;
	/** ユーザー名 */
	@NotBlank
	private String userName;
	/** よみがな */
	@NotBlank
	private String kana;
	/** 助成金カテゴリID */
	private Integer subsidyCategoryId;
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

}
