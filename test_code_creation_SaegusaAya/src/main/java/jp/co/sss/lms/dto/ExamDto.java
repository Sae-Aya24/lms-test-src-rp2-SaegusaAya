package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.51 試験DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class ExamDto {

	/** 試験ID */
	private Integer examId;
	/** 試験名 */
	private String examName;
	/** 概要 */
	private String examDescription;
	/** 制限時間 */
	private Short limitTime;
	/** ジャンルID */
	private Integer genreId;
	/** 企業アカウントID */
	private Integer accountId;
	/** 非表示フラグ */
	private Short hiddenFlg;

}
