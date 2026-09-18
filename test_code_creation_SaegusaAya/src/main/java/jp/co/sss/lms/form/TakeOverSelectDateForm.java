package jp.co.sss.lms.form;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * Task.111 引継面談／会場見学スケジュール日程選択フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class TakeOverSelectDateForm {

	/** 面談スケジュールID */
	private Integer meetingScheduleId;
	/** 用途 */
	private String purpose;
	/** 開始日 */
	private Date meetingOpenDate;
	/** 終了日 */
	private Date meetingCloseDate;
	/** 予約編集期限 */
	private Date editLimit;
	/** 面談日付リスト */
	private List<String> meetingDateList;
	/** 面談時刻リスト */
	private List<String> meetingTimeList;

}
