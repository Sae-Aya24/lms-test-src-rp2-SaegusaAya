package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * Task.97 ジャンルDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class GenreDto {

	/** ジャンルID */
	private Integer genreId;
	/** ジャンル */
	private String genreName;
	/** ジャンル詳細DTOリスト */
	private List<GenreDetailDto> genreDetailDtoList;

}
