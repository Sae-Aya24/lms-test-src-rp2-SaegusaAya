package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * 動画カテゴリDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MovieCategoryDto {

	/** 動画カテゴリID */
	private Integer movieCategoryId;
	/** 動画カテゴリ名 */
	private String movieCategoryName;
	/** 動画リスト */
	private List<MovieDto> movieDtoList;

}
