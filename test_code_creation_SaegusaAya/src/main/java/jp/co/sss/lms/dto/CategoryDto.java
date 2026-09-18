package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.115 カテゴリDTO
 * 
 * @author 東京ITスクール
 *
 */
@Data
public class CategoryDto {

	/** カテゴリID */
	private Integer categoryId;
	/** カテゴリ名 */
	private String categoryName;
	/** カテゴリ概要 */
	private String categoryDescription;
	/** コースID */
	private Integer courseId;

}
