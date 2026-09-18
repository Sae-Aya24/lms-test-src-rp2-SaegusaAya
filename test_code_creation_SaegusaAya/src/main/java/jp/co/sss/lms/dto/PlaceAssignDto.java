package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.67 会場アサイン情報DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class PlaceAssignDto {

	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 人数 */
	private Integer applicantAmount;

}
