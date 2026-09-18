package jp.co.sss.lms.form;

import java.util.Date;
import java.util.List;

import jp.co.sss.lms.dto.TakeOverScheduleDto;
import lombok.Data;

/**
 * Task.108 引継面談／会場見学 実施日一覧フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class TakeOverCandidateListForm {

	/** 完了メッセージ */
	private String successMessage;
	/** エラーメッセージ */
	private String errorMessage;
	/** 引継面談／会場見学スケジュールDTOリスト */
	private List<TakeOverScheduleDto> takeOverScheduleDtoList;
	/** 面談スケジュールID */
	private Integer meetingScheduleId;
	/** 予約編集期限 */
	private Date editLimit;
	/** 面談対象会場ID */
	private Integer meetingPlaceId;
	/** 公開フラグ */
	private Short publishedFlg;
	/** 予約フラグ */
	private Short reservedFlg;

}
