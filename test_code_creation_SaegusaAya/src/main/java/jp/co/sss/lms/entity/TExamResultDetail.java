package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 試験結果詳細テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TExamResultDetail {

	/** 試験結果詳細ID */
	private Integer examResultDetailId;
	/** 試験結果ID */
	private Integer examResultId;
	/** LMSユーザID */
	private Integer lmsUserId;
	/** 問題ID */
	private Integer questionId;
	/** 回答 */
	private Short reply;
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
