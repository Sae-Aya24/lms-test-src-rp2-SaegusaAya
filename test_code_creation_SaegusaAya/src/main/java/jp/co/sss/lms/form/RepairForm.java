package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.87 改修履歴フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class RepairForm {

	/** 改修履歴ID */
	private Integer historyId;
	/** 内容 */
	@NotBlank
	@Size(max = 10000)
	private String content;

}
