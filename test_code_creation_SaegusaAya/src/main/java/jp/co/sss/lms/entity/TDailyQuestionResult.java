package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 日次テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TDailyQuestionResult {

	/** 日次問題ID */
	private Integer dailyQuestionResultId;
	/** 問題ID */
	private Integer questionId;
	/** LMSユーザーID */
	private Integer lmsUserId;
	/** 回答 */
	private Short reply;
	/** 回答日時 */
	private Date replyDate;
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
