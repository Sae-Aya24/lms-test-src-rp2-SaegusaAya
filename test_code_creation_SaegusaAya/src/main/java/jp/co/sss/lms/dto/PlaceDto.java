package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * 会場DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class PlaceDto {

	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 会場詳細 */
	private String placeDescription;
	/** サポートセンター表示 */
	private Short supportAvailable;
	/** 収容人数 */
	private Integer seatingCapacity;
	/** 備考 */
	private String placeNote;
	/** 非表示フラグ */
	private Short hiddenFlg;

}
