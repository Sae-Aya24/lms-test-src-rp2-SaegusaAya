package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * オペレーターDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class OperatorDto {

	/** オペレーターID */
	private Integer operatorId;
	/** オペレーター名 */
	private String operatorName;
	/** 連携用URL */
	private String url;
	/** オペレーター種別 */
	private Short operatorType;

}
