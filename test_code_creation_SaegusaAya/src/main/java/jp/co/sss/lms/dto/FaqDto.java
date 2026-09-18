package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * よくある質問DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class FaqDto {

	/** FAQID */
	private Integer frequentlyAskedQuestionId;
	/** 質問内容 */
	private String question;
	/** 回答内容 */
	private String answer;

}
