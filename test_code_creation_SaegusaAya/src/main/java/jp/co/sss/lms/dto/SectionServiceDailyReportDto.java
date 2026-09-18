package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * セクションサービスレポートDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class SectionServiceDailyReportDto {

	/** レポートID */
	private Integer dailyReportId;
	/** レポート提出ID */
	private Integer dailyReportSubmitId;
	/** レポート名 */
	private String reportName;
	/** 日付 */
	private Date date;
	/** セクションID */
	private Integer sectionId;

}
