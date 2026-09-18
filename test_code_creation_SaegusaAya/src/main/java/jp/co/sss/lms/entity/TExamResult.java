package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 試験結果テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TExamResult {

	/** 試験結果ID */
	private Integer examResultId;
	/** 試験・セクション紐付けID */
	private Integer examSectionId;
	/** LMSユーザID */
	private Integer lmsUserId;
	/** 得点 */
	private Short score;
	/** 所要時間 */
	private Integer time;
	/** 評点フラグ */
	private Short markFlg;
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
