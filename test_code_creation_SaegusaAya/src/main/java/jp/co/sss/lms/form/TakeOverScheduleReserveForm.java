package jp.co.sss.lms.form;

import java.util.Date;
import java.util.List;

import jp.co.sss.lms.dto.TakeOverScheduleDetailDto;
import lombok.Data;

/**
 * Task.73 引継面談／会場見学スケジュール登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class TakeOverScheduleReserveForm {

	/** 面談スケジュールID */
	private Integer meetingScheduleId;
	/** 用途 */
	private String purpose;
	/** 予約編集期限 */
	private Date editLimit;
	/** 面談対象会場ID */
	private Integer meetingPlaceId;
	/** 会場名 */
	private String placeName;
	/** 会場詳細 */
	private String placeDescription;
	/** 会場備考 */
	private String placeNote;
	/** 引継面談／会場見学スケジュール詳細DTOリスト */
	private List<TakeOverScheduleDetailDto> takeOverScheduleDetailDtoList;
	/** 登録可能フラグ */
	private Boolean registerableFlg;
	/** 更新可能フラグ */
	private Boolean updatableFlg;
	/** 予約済インデックス */
	private Integer reservedIndex; // Task.74

}
