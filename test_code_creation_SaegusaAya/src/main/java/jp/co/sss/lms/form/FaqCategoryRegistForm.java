package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.90 よくある質問カテゴリ登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class FaqCategoryRegistForm {

	/** アクション種別 */
	private Integer actionType;
	/** FAQカテゴリID */
	private Integer faqCategoryId;
	/** FAQカテゴリ名 */
	@NotBlank
	@Size(max = 25)
	private String faqCategoryName;

}
