package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.109 引継面談／会場見学スケジュール登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class TakeOverScheduleRegistForm {

	/** 用途 */
	@NotBlank
	@Size(max = 100)
	private String purpose;
	/** 開始日 */
	@NotBlank
	private String meetingOpenDate;
	/** 終了日 */
	@NotBlank
	private String meetingCloseDate;
	/** 予約編集期限 */
	@NotBlank
	private String editLimit;

}
