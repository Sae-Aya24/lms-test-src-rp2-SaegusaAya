package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * 試験結果DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class ExamResultDto {

	/** 試験ID */
	private Integer examId;
	/** 試験結果ID */
	private Integer examResultId;
	/** 試験・セクション紐づけID */
	private Integer examSectionId;
	/** 試験名 */
	private String examName;
	/** 試験実施日付 */
	private Date date;
	/** 正当数 */
	private Short score;
	/** 問題数 */
	private Integer numOfQuestion;
	/** 点数 */
	private double point;
	/** 時間 */
	private Long time; // Task.34
	/** 時間（画面表示用） */
	private String timeDisp; // Task.34

}
