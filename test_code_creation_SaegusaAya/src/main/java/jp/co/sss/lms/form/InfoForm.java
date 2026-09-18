package jp.co.sss.lms.form;

import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.83 お知らせフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class InfoForm {

	/** お知らせID */
	private Integer infoId; // Task.85
	/** 内容 */
	@Size(max = 10000)
	private String content;

}
