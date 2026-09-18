package jp.co.sss.lms.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * レポートDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyReportDto {

	/** 日報ID */
	private Integer dailyReportId;
	/** 日報提出ID */
	private Integer dailyReportSubmitId;
	/** コースID */
	private Integer courseId;
	/** セクションID */
	private Integer sectionId;
	/** セクション・日報紐付けID */
	private Integer sectionDailyReportId;
	/** 日報名 */
	private String reportName;
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
	/** ユーザー名出力例番号 */
	private Integer clmUser;
	/** 日付出力行番号 */
	private Integer rowDate;
	/** 日付出力列番号 */
	private Integer clmDate;
	/** 学習理解度入力フラグ */
	private Short intelligibilityFlg;
	/** 学習理解度項目数 */
	private Short intelligibilityFieldNum;
	/** 学習理解度数 */
	private Short intelligibilityNum;
	/** 理解度項目出力開始行番号 */
	private Integer rowIntelFld;
	/** 理解度項目出力開始列番号 */
	private Integer clmIntelFld;
	/** 理解度出力開始行番号 */
	private Integer rowIntel;
	/** 理解度出力開始列番号 */
	private Integer clmIntel;
	/** ユーザー名 */
	private String userName;
	/** 日付 */
	private Date date;
	/** 日付from */
	private String dateFrom;
	/** 日付To */
	private String dateTo;
	/** フィードバックカウント */
	private Integer fbCount;
	/** フィードバック日 */
	private Date lastFeedbackDate;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 学習理解度DTOリスト */
	private List<IntelligibilityDto> intelligibilityDtoList;
	/** レポート詳細DTOリスト */
	private List<DailyReportDetailDto> dailyReportDetailDtoList;
	/** レポートフィードバックDTOリスト */
	private List<DailyReportFbDto> dailyReportFbDtoList;
	/** フィードバックコメント最新フラグ */
	private Boolean fbNewFlg; // Task.21

}
