package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * お知らせDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class InfoDto {

	/** お知らせID */
	private Integer infoId;
	/** 内容 */
	private String content;
	/** 更新日時 */
	private Date lastModifiedDate;
	/** newフラグ */
	private Boolean newFlg; // Task.02

}
