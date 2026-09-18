package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.114 コースレポートDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class CourseReportDto {

	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** 概要 */
	private String courseDescription;
	/** 開講日 */
	private Date openTime;
	/** 閉講日 */
	private Date closeTime;
	/** 日報ID */
	private Integer dailyReportId;
	/** 日報名 */
	private String dailyReportName;
	/** 評価レポートID */
	private Integer evReportId;
	/** 評価レポート名 */
	private String evReportName;
	/** 面談ファイルID */
	private Integer meetingFileId;
	/** 面談ファイル名 */
	private String meetingFileName;

}
