package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * よくある質問カテゴリDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class FaqCategoryDto {

	/** 質問カテゴリID */
	private Integer frequentlyAskedQuestionCategoryId;
	/** 質問カテゴリ名 */
	private String frequentlyAskedQuestionCategoryName;
	/** 質問リスト */
	private List<FaqDto> faqDtoList;

}
