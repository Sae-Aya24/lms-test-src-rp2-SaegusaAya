package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * 問題DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class QuestionDto {

	/** 問題ID */
	private Integer questionId;
	/** ジャンル詳細 */
	private String genreDetailName;
	/** 問題 */
	private String question;
	/** 回答 */
	private Short reply;
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
	/** アンサーリスト */
	private List<String> answerList;
	/** 解説 */
	private String explain;

}
