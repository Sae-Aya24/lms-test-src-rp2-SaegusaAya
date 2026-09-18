package jp.co.sss.lms.form;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jp.co.sss.lms.enums.LmsUserRoleEnum;
import lombok.Data;

/**
 * パスワード再設定フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class MailAddressForm {

	/** メールアドレス */
	@NotBlank // Task.04
	@Size(max = 254) // Task.04
	@Email // Task.04
	private String mailAddress;
	/** 権限の列挙 */
	private LmsUserRoleEnum[] roleEnum; // Task.05
	/** 権限 */
	private String role; // Task.05

}
