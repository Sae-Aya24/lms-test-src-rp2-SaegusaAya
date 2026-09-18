package jp.co.sss.lms.form;

import lombok.Data;

/**
 * Task.52 試験結果一覧フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class ExamResultListForm {

	/** 試験ID */
	private Integer examId;
	/** コースID */
	private Integer courseId;
	/** 研修会場ID */
	private Integer placeId;
	/** 企業ID */
	private Integer companyId;
	/** 試験セクションID */
	private Integer examSectionId; // Task.53
	/** LMSユーザーID */
	private Integer lmsUserId; // Task.53

}
