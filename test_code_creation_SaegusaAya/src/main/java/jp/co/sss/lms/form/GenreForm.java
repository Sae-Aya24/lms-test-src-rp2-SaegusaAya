package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.97 ジャンルフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class GenreForm {

	/** ジャンルID */
	private Integer genreId;
	/** ジャンル名 */
	@NotBlank // Task.98
	@Size(max = 100) // Task.98
	private String genreName;

}
