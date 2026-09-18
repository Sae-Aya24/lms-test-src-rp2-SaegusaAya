package jp.co.sss.lms.form;

import lombok.Data;

/**
 * Task.103 評価レポートフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class EvReportForm {

	/** 評価レポートID */
	private Integer evReportId;
	/** 評価レポート名 */
	private String reportName;

}
