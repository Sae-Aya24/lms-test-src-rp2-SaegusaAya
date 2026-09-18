package jp.co.sss.lms.dto;

import java.sql.Timestamp;

import lombok.Data;

/**
 * セクションサービス試験DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class SectionServiceExamDto {

	/** 試験・セクション紐づけID */
	private Integer examSectionId;
	/** 試験ID */
	private Integer examId;
	/** 試験名 */
	private String examName;
	/** 試験カテゴリID */
	private Integer genreId;
	/** 公開日時 */
	private Timestamp publicDate;
	/** 公開フラグ */
	private boolean publicFlg = false;
	/** セクションID */
	private Integer sectionId;
	/** コースID */
	private Integer courseId;
	/** 試験実施フラグ */
	private boolean examFlg; // Task.19

}
