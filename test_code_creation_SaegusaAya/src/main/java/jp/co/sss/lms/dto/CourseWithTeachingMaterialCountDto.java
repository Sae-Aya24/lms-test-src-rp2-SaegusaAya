package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.42 コース・紐付き教材件数DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class CourseWithTeachingMaterialCountDto {

	/** 開講フラグ */
	private boolean openCourseFlg;
	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** コース説明 */
	private String courseDescription;
	/** 開講日 */
	private Date openTime;
	/** 閉講日 */
	private Date closeTime;
	/** コース種別 */
	private Short courseType;
	/** コース固定ユーザーパスワード */
	private String password;
	/** コース教材数 */
	private long teachingMaterialCount;

}
