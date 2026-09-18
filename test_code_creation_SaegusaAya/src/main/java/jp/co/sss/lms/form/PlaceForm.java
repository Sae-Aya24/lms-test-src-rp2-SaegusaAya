package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

/**
 * Task.95 会場フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class PlaceForm {

	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	@NotBlank // Task.96
	@Size(max = 20) // Task.96
	private String placeName;
	/** 会場詳細 */
	@NotBlank
	@Size(max = 150)
	private String placeDescription; // Task.96
	/** 収容人数 */
	@NotNull
	@Range(min = 0, max = 9999)
	private Integer seatingCapacity; // Task.96
	/** 備考 */
	@NotBlank
	@Size(max = 150)
	private String placeNote; // Task.96
	/** サポートセンター表示 */
	private Short supportAvailable; // Task.96

}
