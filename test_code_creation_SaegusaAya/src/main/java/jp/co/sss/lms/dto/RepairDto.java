package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.87 改修履歴DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class RepairDto {

	/** 改修履歴ID */
	private Integer historyId;
	/** 内容 */
	private String content;
	/** 初回作成日時 */
	private Date firstCreateDate;

}
