package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.73 引継面談／会場見学スケジュール詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class TakeOverScheduleDetailDto {

	/** 面談スケジュール詳細ID */
	private Integer meetingScheduleDetailId;
	/** 面談日付 */
	private Date meetingDate;
	/** 面談時刻 */
	private String meetingTime;
	/** 企業ID */
	private Integer companyId;
	/** 面談対象企業ID */
	private Integer meetingCompanyId;
	/** 参加人数 */
	private Integer joinAmount;
	/** お客様ご要望欄 */
	private String companyRequest;

}
