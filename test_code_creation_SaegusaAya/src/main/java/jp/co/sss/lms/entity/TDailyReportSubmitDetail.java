package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 日報提出詳細テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TDailyReportSubmitDetail {

	/** 日報提出詳細ID */
	private Integer dailyReportSubmitDetailId;
	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** 項目番号 */
	private Integer fieldNum;
	/** 内容 */
	private String content;
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

}
