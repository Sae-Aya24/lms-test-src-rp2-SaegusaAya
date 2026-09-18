package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.100 動画フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class MovieForm {

	/** 動画ID */
	private Integer movieId;
	/** 動画カテゴリID */
	private Integer movieCategoryId;
	/** 動画名 */
	@NotBlank
	@Size(max = 50)
	private String movieName;
	/** URL */
	@NotBlank
	@Size(max = 150)
	private String url;
	/** 動画カテゴリ名 */
	private String movieCategoryName;

}
