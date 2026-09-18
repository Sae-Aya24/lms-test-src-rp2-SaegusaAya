package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * 契約同意DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class AgreementConsentDto {

	/** 契約同意ID */
	private Integer agreementConsentId;
	/** 契約日 */
	private Date contractDate;
	/** 企業ID */
	private Integer companyId; // Task.67
	/** 企業・コース紐付けID */
	private Integer companyCourseId; // Task.67
	/** コースID */
	private Integer courseId; // Task.67
	/** コース名 */
	private String courseName;
	/** 契約開始日 */
	private Date contractStartDate;
	/** 契約終了日 */
	private Date contractEndDate;

}
