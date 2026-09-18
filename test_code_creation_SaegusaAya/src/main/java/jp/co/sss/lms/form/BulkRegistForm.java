package jp.co.sss.lms.form;

import java.util.List;

import jakarta.validation.constraints.NotBlank;

import lombok.Data;

/**
 * Task.58 勤怠一括登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class BulkRegistForm {

	/** 会場ID */
	private Integer placeId;
	/** 検索期間from */
	@NotBlank
	private String searchPeriodFrom;
	/** 検索期間to */
	@NotBlank
	private String searchPeriodTo;
	/** 会場名 */
	private String placeName;
	/** 対象インデックス */
	private String targetIndex;
	/** 日次の勤怠フォームリスト */
	private List<DailyAttendanceForm> dailyAttendanceFormList;

}
