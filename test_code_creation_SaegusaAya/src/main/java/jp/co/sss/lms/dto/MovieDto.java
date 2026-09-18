package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * 動画情報DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MovieDto {

	/** 動画名 */
	private String movieName;
	/** 動画URL */
	private String url;
	/** 動画カテゴリID */
	private Integer movieCategoryId;
	/** 動画ID */
	private Integer movieId;
	/** 動画カテゴリ名 */
	private String movieCategoryName;

}
