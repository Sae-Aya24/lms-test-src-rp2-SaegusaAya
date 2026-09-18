package jp.co.sss.lms.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * コース情報サービス コースDTO
 * 
 * @author 東京ITスクール
 *
 */
@Data
public class CourseServiceCourseDto {

	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** 開講日 */
	private Date openTime;
	/** 閉講日 */
	private Date closeTime;
	/** コース情報サービス カテゴリDTOリスト */
	private List<CourseServiceCategoryDto> courseServiceCategoryDtoList;

}
