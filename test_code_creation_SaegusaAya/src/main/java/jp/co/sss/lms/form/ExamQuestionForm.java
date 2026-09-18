package jp.co.sss.lms.form;

import java.util.List;

import jp.co.sss.lms.dto.QuestionDto;
import lombok.Data;

/**
 * 試験問題画面フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class ExamQuestionForm {

	/** 試験ID */
	private Integer examId;
	/** セクションID */
	private Integer sectionId;
	/** 試験・セクション紐付けID */
	private Integer examSectionId;
	/** 試験名 */
	private String examName;
	/** 制限時間 */
	private Integer limitTime;
	/** 試験問題DTOリスト */
	private List<QuestionDto> questionDtoList;
	/** 回答リスト */
	private Short[] answer;
	/** 経過時間 */
	private Integer time;
	/** 試験結果ID */
	private Integer examResultId;
	/** 点数 */
	private Short score;

}
