package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * Task.78 面談ファイルフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class MeetingFileForm {

	/** 面談ファイルID */
	private Integer meetingFileId;
	/** ファイル名 */
	private String fileName;
	/** シート名 */
	@NotBlank
	@Size(max = 100)
	private String sheetName;
	/** 企業名出力行番号 */
	@NotBlank
	@Pattern(regexp = "[0-9]+", message = "{pattern.integer}")
	private String rowCompany;
	/** 企業名出力列番号 */
	@NotBlank
	private String clmCompany;
	/** ユーザー名出力行番号 */
	@NotBlank
	@Pattern(regexp = "[0-9]+", message = "{pattern.integer}")
	private String rowUser;
	/** ユーザー名出力列番号 */
	@NotBlank
	private String clmUser;
	/** 日付出力行番号 */
	@NotBlank
	@Pattern(regexp = "[0-9]+", message = "{pattern.integer}")
	private String rowDate;
	/** 日付出力列番号 */
	@NotBlank
	private String clmDate;
	/** 面談内容出力開始行番号 */
	@NotBlank
	@Pattern(regexp = "[0-9]+", message = "{pattern.integer}")
	private String rowMeeting;
	/** 面談内容出力開始列番号 */
	@NotBlank
	private String clmMeeting;
	/** タイトル */
	private String title;
	/** 最大ファイルサイズ */
	private String maxFileSize;

}
