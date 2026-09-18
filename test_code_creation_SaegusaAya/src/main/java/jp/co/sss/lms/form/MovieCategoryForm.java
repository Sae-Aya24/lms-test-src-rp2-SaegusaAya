package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.100 動画カテゴリフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class MovieCategoryForm {

	/** 動画カテゴリID */
	private Integer movieCategoryId;
	/** 動画カテゴリ名 */
	@NotBlank
	@Size(max = 100)
	private String movieCategoryName;

}
