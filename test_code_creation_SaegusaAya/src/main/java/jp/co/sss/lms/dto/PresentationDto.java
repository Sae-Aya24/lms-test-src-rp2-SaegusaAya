package jp.co.sss.lms.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Task.60 成果報告会DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class PresentationDto {

	/** 成果報告会スケジュールID */
	private Integer presentationScheduleId;
	/** 予約編集期限 */
	private Date editLimit;
	/** 成果報告会開催日 */
	private Date presentationDate;
	/** 用途 */
	private String purpose;
	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 会場詳細 */
	private String placeDescription;
	/** 会場備考 */
	private String placeNote;
	/** 画面予約状況フラグ */
	private boolean reservationStatusFlg;
	/** 成果報告会詳細DTOリスト */
	private List<PresentationDetailDto> presentationDetailDtoList;
	/** 成果報告会チーム詳細DTOリスト */
	private List<PresentationTeamDetailDto> presentationTeamDetailDtoList;

}
