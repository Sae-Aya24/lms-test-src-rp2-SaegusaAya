package jp.co.sss.lms.form;

import lombok.Data;

/**
 * Task.89 よくある質問カテゴリ検索フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class FaqCategorySearchForm {

	/** FAQカテゴリID */
	private Integer faqCategoryId;
	/** FAQカテゴリ名 */
	private String faqCategoryName;

}
