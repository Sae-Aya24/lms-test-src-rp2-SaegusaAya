package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * ユーザーレポート情報DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class UserDailyReportDto {

	/** LMSユーザID */
	private Integer lmsUserId;
	/** ユーザID */
	private Integer userId;
	/** ユーザ名 */
	private String userName;
	/** 企業アカウントID */
	private Integer accountId;
	/** 企業ID */
	private Integer companyId;
	/** 企業名 */
	private String companyName;
	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 会場備考 */
	private String placeNote;
	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** セクションID */
	private Integer sectionId;
	/** セクション名 */
	private String sectionName;
	/** セクション日付 */
	private Date sectionDate;
	/** 日報ID */
	private Integer dailyReportId;
	/** 日報名 */
	private String reportName;
	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** 日報提出日付 */
	private Date submitDate;
	/** フィードバック件数 */
	private Integer fbCount;

}
