package jp.co.sss.lms.form;

import java.util.Date;

import lombok.Data;

/**
 * レポート登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyReportSubmitForm {

	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** LMSユーザーID */
	private Integer lmsUserId;
	/** 日付 */
	private Date date;
	/** コースID */
	private Integer courseId;
	/** セクションID */
	private Integer sectionId;
	/** 日報ID */
	private Integer dailyReportId;
	/** レポート名 */
	private String reportName;
	/** 理解度フラグ */
	private Short intelligibilityFlg;
	/** 理解度項目数 */
	private Short intelligibilityFieldNum;
	/** 理解度段階 */
	private Short intelligibilityNum;
	/** 学習項目配列 */
	private String[] intFieldNameArray;
	/** 理解度配列 */
	private Short[] intFieldValueArray;
	/** 報告レポート名配列 */
	private String[] fieldNameArray;
	/** 報告レポート内容配列 */
	private String[] contentArray;
	/** 必須フラグ配列 */
	private Short[] requiredFlgArray;
	/** レポートタイプ配列 */
	private Short[] inputTypeArray;
	/** 範囲from配列 */
	private Integer[] rangeFromArray;
	/** 範囲to配列 */
	private Integer[] rangeToArray;

}
