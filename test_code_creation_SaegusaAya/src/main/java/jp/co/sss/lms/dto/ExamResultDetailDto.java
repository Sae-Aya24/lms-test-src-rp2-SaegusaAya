package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * 試験結果詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class ExamResultDetailDto {

	/** 試験結果ID */
	private Integer examResultId;
	/** 試験・セクション紐付けID */
	private Integer examSectionId;
	/** 試験名 */
	private String examName;
	/** 得点 */
	private Integer score;
	/** 試験問題数 */
	private Integer numOfQuestion;
	/** 点数 */
	private Double point;
	/** LMSユーザーID */
	private Integer lmsUserId;
	/** ユーザー名 */
	private String userName;
	/** 問題DTOリスト */
	private List<QuestionDto> questionDtoList;

}
