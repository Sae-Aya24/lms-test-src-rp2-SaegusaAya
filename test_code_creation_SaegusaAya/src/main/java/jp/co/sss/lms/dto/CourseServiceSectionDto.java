package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * コース情報サービス セクションDTO
 * 
 * @author 東京ITスクール
 *
 */
@Data
public class CourseServiceSectionDto {

	/** セクションID */
	private Integer sectionId;
	/** セクション名 */
	private String sectionName;
	/** 日付 */
	private Date date;
	/** 当日フラグ */
	private Boolean todayFlg; // Task.14
	/** レポート提出フラグ */
	private Boolean reportFlg; // Task.15
	/** 試験有無フラグ */
	private Boolean examFlg; // Task.16
	/** セクション削除フラグ */
	private Integer sectionDelFlg; // Task.115

}
