package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.97 ジャンル詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class GenreDetailDto {

	/** ジャンル詳細ID */
	private Integer genreDetailId;
	/** ジャンル詳細 */
	private String genreDetailName;

}
