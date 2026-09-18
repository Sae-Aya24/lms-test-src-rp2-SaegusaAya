package jp.co.sss.lms.form;

import lombok.Data;

/**
 * レポート詳細フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyReportDetailForm {

	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** 日報フィードバックコメントID */
	private Integer dailyReportFbId;
	/** コメント */
	private String content;

}
