package jp.co.sss.lms.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * レポートダウンロードDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyReportDownloadDto {

	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** 日報ID */
	private Integer dailyReportId;
	/** ファイル名 */
	private String fileName;
	/** シート名 */
	private String sheetName;
	/** 企業名 */
	private String companyName;
	/** 社名出力行番号 */
	private Integer rowCompany;
	/** 社名出力列番号 */
	private Integer clmCompany;
	/** ユーザー名 */
	private String userName;
	/** ユーザー名出力行番号 */
	private Integer rowUser;
	/** ユーザー名出力列番号 */
	private Integer clmUser;
	/** 日付 */
	private Date date;
	/** 日付出力行番号 */
	private Integer rowDate;
	/** 日付出力列番号 */
	private Integer clmDate;
	/** 理解度項目出力開始行番号 */
	private Integer rowIntelFld;
	/** 理解度項目出力開始列番号 */
	private Integer clmIntelFld;
	/** 理解度出力開始行番号 */
	private Integer rowIntel;
	/** 理解度出力開始列番号 */
	private Integer clmIntel;
	/** レポート詳細DTOリスト */
	private List<DailyReportDetailDto> dailyReportDetailDtoList;
	/** 理解度DTOリスト */
	private List<IntelligibilityDto> intelligibilityDtoList;
	/** レポートフィードバックDTOリスト */
	private List<DailyReportFbDto> dailyReportFbDtoList;

}
