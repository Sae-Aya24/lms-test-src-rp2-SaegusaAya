package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 成果物結果テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TDeliverablesResult {

	/** 成果物結果ID */
	private Integer deliverablesResultId;
	/** 成果物・セクション紐付けID */
	private Integer deliverablesSectionId;
	/** LMSユーザID */
	private Integer lmsUserId;
	/** 採点 */
	private Integer score;
	/** フィードバック */
	private String feedback;
	/** ファイルパス */
	private String filePath;
	/** ファイルサイズ */
	private Long fileSize;
	/** 提出時間 */
	private Date submissionTime;
	/** アカウントID */
	private Integer accountId;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回作成日 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日 */
	private Date lastModifiedDate;

}