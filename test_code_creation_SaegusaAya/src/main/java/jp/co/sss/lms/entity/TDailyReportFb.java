package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 日報フィードバックコメントテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TDailyReportFb {

	/** 日報フィードバックコメントID */
	private Integer dailyReportFbId;
	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** LMSユーザID */
	private Integer lmsUserId;
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
