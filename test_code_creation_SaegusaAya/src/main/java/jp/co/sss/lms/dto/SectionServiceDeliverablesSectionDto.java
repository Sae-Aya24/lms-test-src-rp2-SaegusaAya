package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * セクションサービス成果物セクションDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class SectionServiceDeliverablesSectionDto {

	/** 成果物・セクション紐付けID */
	public Integer deliverablesSectionId;
	/** 提出期限 */
	public String submissionDeadline;
	/** セクションサービス成果物DTO */
	public SectionServiceDeliverablesDto deliverablesDto;

}
