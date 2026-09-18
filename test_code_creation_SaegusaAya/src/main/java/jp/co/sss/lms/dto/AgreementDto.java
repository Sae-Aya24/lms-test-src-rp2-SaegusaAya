package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.64 契約内容DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class AgreementDto {

	/** 契約同意ID */
	private Integer agreementConsentId; // Task.65
	/** 契約内容ID */
	private Integer agreementId;
	/** 契約内容 */
	private String agreementContent;
	/** 個人情報利用目的 */
	private String personalInformationPurpose;
	/** 契約日 */
	private String contractDate;
	/** 企業名 */
	private String companyName;
	/** 事業所所在地 */
	private String companyAddress;
	/** 代表者役職名 */
	private String representativePost;
	/** 代表者氏名 */
	private String representativeName;
	/** コース名 */
	private String courseName;
	/** 契約開始日 */
	private String contractStartDate;
	/** 契約終了日 */
	private String contractEndDate;

}
