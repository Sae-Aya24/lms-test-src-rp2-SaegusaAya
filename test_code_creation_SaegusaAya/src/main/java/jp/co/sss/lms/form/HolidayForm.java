package jp.co.sss.lms.form;

import jakarta.validation.constraints.Pattern;

import lombok.Data;

/**
 * Task.86 休日フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class HolidayForm {

	/** 開始月 */
	@Pattern(regexp = "^(\\d{4})[-/]?(\\d{1,2})$", message = "{pattern.invalid}")
	private String startDate;
	/** 休日IDリスト */
	private Integer[] holidayId;
	/** 日付リスト */
	private String[] day;
	/** 休日名リスト */
	private String[] holidayName;
	/** 取消フラグリスト */
	private Short[] cancelFlg;
	/** 休日フラグ */
	private Short[] holidayFlg;

}
