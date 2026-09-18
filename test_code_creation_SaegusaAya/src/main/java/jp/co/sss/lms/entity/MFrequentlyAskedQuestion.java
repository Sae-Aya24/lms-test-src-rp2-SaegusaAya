package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 質問マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MFrequentlyAskedQuestion {

	/** 質問ID */
	private Integer frequentlyAskedQuestionId;
	/** 質問カテゴリーID */
	private Integer frequentlyAskedQuestionCategoryId;
	/** 質問内容 */
	private String question;
	/** 回答内容 */
	private String answer;
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
