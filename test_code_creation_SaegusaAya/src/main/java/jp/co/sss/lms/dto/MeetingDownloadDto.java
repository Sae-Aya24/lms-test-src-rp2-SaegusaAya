package jp.co.sss.lms.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * 面談ダウンロードDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MeetingDownloadDto {

	/** 面談ID */
	private Integer meetingId;
	/** 面談実施日 */
	private Date interviewDate;
	/** 面談実施者 */
	private String interviewer;
	/** 受講生 */
	private String interviewee;
	/** 企業名 */
	private String companyName;
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
	/** 面談詳細DTOリスト */
	private List<MeetingDetailDto> meetingDetailDtoList;

}
