package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.91 よくある質問登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class FaqRegistForm {

	/** アクション種別 */
	private Integer actionType;
	/** 質問ID */
	private Integer faqId;
	/** FAQカテゴリID */
	private Integer faqCategoryId;
	/** 質問内容 */
	@NotBlank
	@Size(max = 50)
	private String question;
	/** 回答内容 */
	@NotBlank
	@Size(max = 400)
	private String answer;

}
