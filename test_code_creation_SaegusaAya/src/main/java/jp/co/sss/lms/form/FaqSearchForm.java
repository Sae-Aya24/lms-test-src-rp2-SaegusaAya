package jp.co.sss.lms.form;

import lombok.Data;

/**
 * Task.11 よくある質問検索フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class FaqSearchForm {

	/** キーワード */
	private String keyword;
	/** 質問カテゴリID */
	private Integer frequentlyAskedQuestionCategoryId;

}
