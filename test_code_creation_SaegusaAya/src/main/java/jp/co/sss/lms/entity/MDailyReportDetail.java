package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * レポート詳細マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MDailyReportDetail {

	/** 日報詳細ID */
	private Integer dailyReportDetailId;
	/** 日報ID */
	private Integer dailyReportId;
	/** 項目番号 */
	private Integer fieldNum;
	/** 項目名 */
	private String fieldName;
	/** 出力行番号 */
	private Integer row;
	/** 出力列番号 */
	private Integer clm;
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
	/** 必須 */
	private Short requiredFlg;
	/** 型 */
	private Short inputType;
	/** 範囲from */
	private Integer rangeFrom;
	/** 範囲to */
	private Integer rangeTo;

}
