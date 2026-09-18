package jp.co.sss.lms.form;

import java.util.Date;
import java.util.List;

import jp.co.sss.lms.dto.CategoryDto;
import jp.co.sss.lms.dto.CourseServiceCategoryDto;
import lombok.Data;

/**
 * Task.115 コース詳細登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class CourseDetailRegistForm {

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
	/** 研修日リスト */
	private List<String> workingDaysList;
	/** カテゴリDTOリスト */
	private List<CategoryDto> categoryDtoList;
	/** 更新フラグ */
	private Boolean updateFlg;

}
