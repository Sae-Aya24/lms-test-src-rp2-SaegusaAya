package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.48 マイアカウントDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MyAccountDto {

	/** ユーザー名 */
	private String userName;
	/** ロール */
	private String role;
	/** ロール名 */
	private String roleName;
	/** コース名 */
	private String courseName; // Task.49
	/** 会場名 */
	private String placeName;
	/** 備考 */
	private String placeNote;

}
