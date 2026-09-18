package jp.co.sss.lms.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 試験詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class ExamDetailDto {

	/** 試験・セクション紐づけID */
	private Integer examSectionId;
	/** 試験ID */
	private Integer examId;
	/** 試験名 */
	private String examName;
	/** 公開日時 */
	private Date publicDate;
	/** 問題数 */
	private Integer numOfQuestion;
	/** 制限時間 */
	private Integer limitTime;
	/** 平均点 */
	private Double avgScore;
	/** セクションID */
	private Integer sectionId;
	/** LMSユーザーID */
	private Integer lmsUserId;
	/** ユーザー名 */
	private String userName;
	/** 試験結果DTOリスト */
	private List<ExamResultDto> examResultDtoList;

}
