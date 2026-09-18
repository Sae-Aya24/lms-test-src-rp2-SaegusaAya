package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * コースDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class CourseDto {

	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** 概要 */
	private String courseDescription;
	/** 開講日 */
	private Date openTime;
	/** 閉講日 */
	private Date closeTime;
	/** コースタイプ */
	private Short courseType;
	/** 非表示フラグ */
	private Short hiddenFlg;

}
