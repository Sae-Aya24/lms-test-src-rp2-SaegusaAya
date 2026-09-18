package jp.co.sss.lms.form;

import lombok.Data;

/**
 * レポートダウンロードフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyReportDownloadForm {

	/** 日報ID */
	private Integer dailyReportId;
	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** 日報IDリスト */
	private String[] dailyReportIdList;
	/** 日報提出IDリスト */
	private String[] dailyReportSubmitIdList;

}
