package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.63 成果報告会チーム詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class PresentationTeamDetailDto {

	/** 成果報告会チームID */
	private Integer presentationTeamId;
	/** チーム名 */
	private String presentationTeamName;
	/** 企業ID */
	private Integer companyId;
	/** 企業名 */
	private String companyName;
	/** 所在地 */
	private String address;
	/** ユーザーID */
	private Integer userId;
	/** 受講生名 */
	private String userName;

}