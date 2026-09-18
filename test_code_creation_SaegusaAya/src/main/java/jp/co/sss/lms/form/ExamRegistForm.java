package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import org.hibernate.validator.constraints.Range;

import lombok.Data;

/**
 * Task.112 試験登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class ExamRegistForm {

	/** 試験ID */
	private Integer examId;
	/** 試験名 */
	@NotBlank
	@Size(max = 20)
	private String examName;
	/** 制限時間 */
	@NotNull
	@Range(min = 1, max = 32767)
	private Short limitTime;
	/** ジャンルID */
	@NotNull
	private Integer genreId;
	/** 試験問題数 */
	private Integer numOfQuestion = 1; // Task.113
	/** 問題IDリスト */
	private Integer[] questionId = new Integer[numOfQuestion]; // Task.113
	/** 問題リスト */
	private String[] question = new String[numOfQuestion]; // Task.113
	/** 点数リスト */
	private Short[] grade = new Short[numOfQuestion]; // Task.113
	/** 正答リスト */
	private Short[] answerNum = new Short[numOfQuestion]; // Task.113
	/** 選択肢1リスト */
	private String[] choice1 = new String[numOfQuestion]; // Task.113
	/** 選択肢2リスト */
	private String[] choice2 = new String[numOfQuestion]; // Task.113
	/** 選択肢3リスト */
	private String[] choice3 = new String[numOfQuestion]; // Task.113
	/** 選択肢4リスト */
	private String[] choice4 = new String[numOfQuestion]; // Task.113
	/** 解説リスト */
	private String[] explain = new String[numOfQuestion]; // Task.113
	/** ジャンル詳細IDリスト */
	private Integer[] genreDetailId = new Integer[numOfQuestion]; // Task.113

}
