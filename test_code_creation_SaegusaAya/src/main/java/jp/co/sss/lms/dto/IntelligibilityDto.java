package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * 理解度DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class IntelligibilityDto {

	/** 理解度ID */
	private Integer intelligibilityId;
	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** 項目番号 */
	private Integer fieldNum;
	/** 項目名 */
	private String fieldName;
	/** 値 */
	private Short fieldValue;

}
