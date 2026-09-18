package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.67 受講生DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class StudentDto {

	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** ユーザー名 */
	private String userName;
	/** よみがな */
	private String kana;
	/** プログラム経験 */
	private Short programmingExperience;
	/** 研修を通じてどのようになってほしいか */
	private String hopeViaTraning;
	/** 助成金カテゴリID */
	private Integer subsidyCategoryId;

}
