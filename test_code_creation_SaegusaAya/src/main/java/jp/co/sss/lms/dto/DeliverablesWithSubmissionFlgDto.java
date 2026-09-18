package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * 成果物サービスDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class DeliverablesWithSubmissionFlgDto {

	/** 成果物ID */
	private Integer deliverablesId;
	/** 成果物・セクション紐づけID */
	private Integer deliverablesSectionId;
	/** 成果物名 */
	private String deliverablesName;
	/** 備考 */
	private String note;
	/** 提出期限 */
	private String submissionDeadline;
	/** 提出フラグ */
	private Short submissionFlg;

}
