package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * コース情報サービス カテゴリDTO
 * 
 * @author 東京ITスクール
 *
 */
@Data
public class CourseServiceCategoryDto {

	/** カテゴリID */
	private Integer categoryId; // Task.115
	/** カテゴリ名 */
	private String categoryName;
	/** カテゴリ概要 */
	private String categoryDescription; // Task.18
	/** コース情報サービス セクションDTOリスト */
	private List<CourseServiceSectionDto> courseServiceSectionDtoList;
	/** カテゴリ削除フラグ */
	private Integer categoryDelFlg; // Task.115

}
