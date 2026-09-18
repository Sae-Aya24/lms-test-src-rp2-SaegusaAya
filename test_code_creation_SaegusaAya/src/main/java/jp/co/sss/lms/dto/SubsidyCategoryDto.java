package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.67 助成金カテゴリDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class SubsidyCategoryDto {

	/** 助成金カテゴリID */
	private Integer subsidyCategoryId;
	/** 助成金カテゴリ名 */
	private String subsidyCategoryName;

}
