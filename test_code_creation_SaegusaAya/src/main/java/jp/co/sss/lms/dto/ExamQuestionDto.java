package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * 試験問題DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class ExamQuestionDto {

	/** 試験ID */
	private Integer examId;
	/** 試験名 */
	private String examName;
	/** 制限時間 */
	private Integer limitTime;
	/** 問題DTOリスト */
	private List<QuestionDto> questionDtoList;

}
