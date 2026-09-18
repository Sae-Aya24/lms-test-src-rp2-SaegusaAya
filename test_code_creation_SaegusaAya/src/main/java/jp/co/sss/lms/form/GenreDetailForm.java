package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.97 ジャンル詳細フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class GenreDetailForm {

	/** ジャンル詳細ID */
	private Integer genreDetailId;
	/** ジャンルID */
	private String genreId; // Task.99
	/** ジャンル詳細名 */
	@NotBlank
	@Size(max = 100)
	private String genreDetailName; // Task.99

}
