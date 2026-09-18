package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.88 成果報告会予定DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class PresentationTeamDto {

	/** 成果報告会予定ID */
	private Integer presentationScheduleId;
	/** 予約編集期限 */
	private Date editLimit;
	/** 開催日 */
	private Date presentationDate;
	/** 用途 */
	private String purpose;
	/** 会場名 */
	private String placeName;
	/** 備考 */
	private String placeNote;
	/** 説明 */
	private String placeDescription;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回作成日時 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日時 */
	private Date lastModifiedDate;

}
