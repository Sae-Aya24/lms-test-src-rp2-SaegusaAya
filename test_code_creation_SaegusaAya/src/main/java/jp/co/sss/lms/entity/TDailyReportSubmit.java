package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 日報提出テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TDailyReportSubmit {

	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** 日報ID */
	private Integer dailyReportId;
	/** 日付 */
	private Date date;
	/** LMSユーザID */
	private Integer lmsUserId;
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
