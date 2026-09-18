package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import lombok.Data;

/**
 * Task.105 企業登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class CompanyRegistForm {

	/** 企業ID */
	private Integer companyId;
	/** 企業名 */
	@NotBlank
	@Size(max = 50)
	private String companyName;
	/** ファイル共有フラグ */
	private Short fileShareFlg;

}
