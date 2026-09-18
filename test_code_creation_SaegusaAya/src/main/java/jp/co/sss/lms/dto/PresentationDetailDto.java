package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * Task.60 成果報告会詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class PresentationDetailDto {

	/** 成果報告会スケジュール詳細ID */
	private Integer presentationScheduleDetailId;
	/** 成果報告会時間 */
	private String presentationTime;
	/** 成果報告会チームID */
	private Integer presentationTeamId;
	/** 成果報告会対象企業ID */
	private Integer presentationCompanyId;
	/** 企業ID */
	private Integer companyId;
	/** 企業名 */
	private String companyName;
	/** 所在地 */
	private String address;
	/** 参加可能フラグ */
	private Short joinAbleFlg;
	/** 参加人数 */
	private String joinAmount;
	/** 参加者名 */
	private String joinName;
	/** 画面予約状況フラグ */
	private boolean reservationStatusFlg;

}
