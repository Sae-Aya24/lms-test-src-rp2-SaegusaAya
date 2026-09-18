package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * セクションサービス成果物DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class SectionServiceDeliverablesDto {

	/** 成果物ID */
	private Integer deliverablesId;
	/** 成果物名 */
	private String deliverablesName;

}
