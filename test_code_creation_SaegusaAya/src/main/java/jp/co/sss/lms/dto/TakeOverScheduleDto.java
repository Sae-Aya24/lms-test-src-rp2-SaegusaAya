package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.59 引継面談／会場見学スケジュールDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class TakeOverScheduleDto {

	/** 面談スケジュールID */
	private Integer meetingScheduleId;
	/** 用途 */
	private String purpose;
	/** 面談開始日 */
	private Date meetingOpenDate;
	/** 面談終了日 */
	private Date meetingCloseDate;
	/** 予約編集期限 */
	private Date editLimit;
	/** 面談対象会場ID */
	private Integer meetingPlaceId;
	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 会場詳細 */
	private String placeDescription;
	/** 会場備考 */
	private String placeNote;
	/** 公開フラグ */
	private Integer publishedFlg; // Task.108
	/** 予約フラグ */
	private Integer reservedFlg; // Task.108

}
