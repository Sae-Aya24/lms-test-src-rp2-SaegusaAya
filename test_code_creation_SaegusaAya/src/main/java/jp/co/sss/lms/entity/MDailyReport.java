package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * レポートマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MDailyReport {

	/** 日報ID */
	private Integer dailyReportId;
	/** 日報名 */
	private String reportName;
	/** ファイル名 */
	private String fileName;
	/** シート名 */
	private String sheetName;
	/** 社名出力行番号 */
	private Integer rowCompany;
	/** 社名出力列番号 */
	private Integer clmCompany;
	/** ユーザー名出力行番号 */
	private Integer rowUser;
	/** ユーザー名出力列番号 */
	private Integer clmUser;
	/** 日付出力行番号 */
	private Integer rowDate;
	/** 日付出力列番号 */
	private Integer clmDate;
	/** 学習理解度入力フラグ */
	private Short intelligibilityFlg;
	/** 学習理解度項目数 */
	private Short intelligibilityFieldNum;
	/** 学習理解度数 */
	private Short intelligibilityNum;
	/** 理解度項目出力開始行番号 */
	private Integer rowIntelFld;
	/** 理解度項目出力開始列番号 */
	private Integer clmIntelFld;
	/** 理解度出力開始行番号 */
	private Integer rowIntel;
	/** 理解度出力開始列番号 */
	private Integer clmIntel;
	/** 企業アカウントID */
	private Integer accountId;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回作成日時 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日時 */
	private Date lastModifiedDate;
	/** 非表示フラグ */
	private Short hiddenFlg;

}
