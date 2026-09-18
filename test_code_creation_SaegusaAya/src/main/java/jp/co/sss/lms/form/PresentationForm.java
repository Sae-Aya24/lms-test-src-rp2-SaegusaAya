package jp.co.sss.lms.form;

import lombok.Data;

/**
 * Task.76 成果報告会フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class PresentationForm {

	/** 成果報告会対象企業ID */
	private Integer presentationCompanyId;
	/** 参加可能フラグ */
	private Short joinAbleFlg;
	/** 参加人数 */
	private Integer joinAmount;
	/** 参加者名 */
	private String joinName;

}
