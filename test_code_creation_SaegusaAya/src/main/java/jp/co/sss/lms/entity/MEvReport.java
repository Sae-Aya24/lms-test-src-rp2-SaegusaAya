package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * Task.103 評価レポートマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MEvReport {

	/** 評価レポートID */
	private Integer evReportId;
	/** 日報ID */
	private String reportName;
	/** ファイル名 */
	private String fileName;
	/** 基本情報出力シート名 */
	private String sheetNameStd;
	/** 社名出力行番号 */
	private Integer rowCompany;
	/** 社名出力列番号 */
	private Integer clmCompany;
	/** ユーザー名出力行番号 */
	private Integer rowUser;
	/** ユーザー名出力列番号 */
	private Integer clmUser;
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
