package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.107 面談テンプレートファイル登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class MeetingFileRegistForm {

	/** 面談ファイルID */
	private Integer meetingFileId;
	/** ファイル名 */
	private String fileName;
	/** シート名 */
	@NotBlank
	@Size(max = 100)
	private String sheetName;
	/** 企業名出力行番号 */
	@NotNull
	private Integer rowCompany;
	/** 企業名出力列番号 */
	@NotBlank
	private String clmCompany;
	/** ユーザー名出力行番号 */
	@NotNull
	private Integer rowUser;
	/** ユーザー名出力列番号 */
	@NotBlank
	private String clmUser;
	/** 日付出力行番号 */
	@NotNull
	private Integer rowDate;
	/** 日付出力列番号 */
	@NotBlank
	private String clmDate;
	/** 面談内容出力開始行番号 */
	@NotNull
	private Integer rowMeeting;
	/** 面談内容出力開始列番号 */
	@NotBlank
	private String clmMeeting;

}
