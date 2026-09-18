package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * レポート詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyReportDetailDto {

	/** 日報詳細ID */
	private Integer dailyReportSubmitId;
	/** 項目番号 */
	private Integer fieldNum;
	/** 項目名 */
	private String fieldName;
	/** 日付出力行 */
	private Integer row;
	/** 日付出力列 */
	private Integer clm;
	/** 必須フラグ */
	private Short requiredFlg;
	/** 型 */
	private Short inputType;
	/** 範囲From */
	private Integer rangeFrom;
	/** 範囲To */
	private Integer rangeTo;
	/** 内容 */
	private String content;

}
