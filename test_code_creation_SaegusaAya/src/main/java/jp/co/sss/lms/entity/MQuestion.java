package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 試験問題マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MQuestion {

	/** 問題ID */
	private Integer questionId;
	/** 問題 */
	private String question;
	/** 点数 */
	private Short grade;
	/** 正当 */
	private Short answerNum;
	/** 選択肢1 */
	private String choice1;
	/** 選択肢2 */
	private String choice2;
	/** 選択肢3 */
	private String choice3;
	/** 選択肢4 */
	private String choice4;
	/** 解説 */
	private String explain;
	/** 試験ID */
	private Integer examId;
	/** ジャンル詳細ID */
	private Integer genreDetailId;
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
