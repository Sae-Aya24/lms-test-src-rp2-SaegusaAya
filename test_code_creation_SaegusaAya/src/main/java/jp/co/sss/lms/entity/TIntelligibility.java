package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 理解度テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TIntelligibility {

	/** 理解度ID */
	private Integer intelligibilityId;
	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** 項目番号 */
	private Integer fieldNum;
	/** 項目名 */
	private String fieldName;
	/** 値 */
	private Short fieldValue;
	/** アカウントID */
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
