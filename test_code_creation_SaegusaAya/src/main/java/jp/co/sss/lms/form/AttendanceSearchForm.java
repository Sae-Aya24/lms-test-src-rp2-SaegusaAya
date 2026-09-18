package jp.co.sss.lms.form;

import lombok.Data;

/**
 * Task.57 勤怠検索フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class AttendanceSearchForm {

	/** LMSユーザーID */
	private Integer lmsUserId;
	/** ユーザー名 */
	private String userName;
	/** コース名 */
	private String courseName;
	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 企業名 */
	private String companyName;
	/** 過去の受講生検索期間 */
	private String pastTimeLabel;
	/** 過去の受講生検索フラグ */
	private Short pastFlg;

}
