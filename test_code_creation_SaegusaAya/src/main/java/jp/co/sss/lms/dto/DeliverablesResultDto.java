package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * 成果物詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class DeliverablesResultDto {

	/** 成果物・セクション紐付けID */
	private Integer deliverablesResultId;
	/** LMSユーザーID */
	private Integer lmsUserId;
	/** 採点 */
	private Integer score;
	/** フィードバック */
	private String feedback;
	/** ファイルパス */
	private String filePath;
	/** ファイルサイズ */
	private Long fileSize;
	/** 提出時間 */
	private Date submissionTime;
	/** 日付 */
	private Date date;

}
