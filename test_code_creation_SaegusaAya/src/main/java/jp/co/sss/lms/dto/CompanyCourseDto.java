package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.110 企業コースDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class CompanyCourseDto {

	/** 企業ID */
	private Integer companyId;
	/** 企業名 */
	private String companyName;
	/** 企業所在地 */
	private String companyAddress;
	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** 開講日 */
	private Date openTime;
	/** 閉講日 */
	private Date closeTime;
	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 会場所在地 */
	private String placeAddress;

}
