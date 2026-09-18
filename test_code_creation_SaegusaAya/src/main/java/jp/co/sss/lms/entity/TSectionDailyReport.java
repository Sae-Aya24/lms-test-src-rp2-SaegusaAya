package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * セクション・日報紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TSectionDailyReport {

	/** セクション・日報紐付けID */
	private Integer sectionDailyReportId;
	/** セクションID */
	private Integer sectionId;
	/** 日報ID */
	private Integer dailyReportId;
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
