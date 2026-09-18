package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.106 面談ファイルDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MeetingFileDto {

	/** 面談ファイルID */
	private Integer meetingFileId;
	/** ファイル名 */
	private String fileName;
	/** シート名 */
	private String sheetName;
	/** 社名出力行番号 */
	private Integer rowCompany;
	/** 社名出力列番号 */
	private Integer clmCompany;
	/** ユーザー名出力行番号 */
	private Integer rowUser;
	/** ユーザー名出力列番号 */
	private Integer clmUser;
	/** 日付出力行番号 */
	private Integer rowDate;
	/** 日付出力列番号 */
	private Integer clmDate;
	/** 面談内容出力開始行番号 */
	private Integer rowMeeting;
	/** 面談内容出力開始列番号 */
	private Integer clmMeeting;
	/** 削除フラグ */
	private Short deleteFlg;

}
