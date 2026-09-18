package jp.co.sss.lms.form;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import jp.co.sss.lms.util.PasswordUtil;
import lombok.Data;

/**
 * ログインフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class LoginForm {

	/** ログインID */
	@NotBlank(groups = { LoginGroup.class })
	private String loginId;
	/** 現在のパスワード */
	@NotBlank(groups = { ChangePasswordGroup.class })
	private String currentPassword;
	/** 新しいパスワード */
	@NotBlank(groups = { LoginGroup.class, ChangePasswordGroup.class, ResetPasswordGroup.class })
	@Size(max = 20, groups = { LoginGroup.class, ChangePasswordGroup.class,
			ResetPasswordGroup.class })
	@Pattern(groups = { ChangePasswordGroup.class,
			ResetPasswordGroup.class }, regexp = PasswordUtil.PASSWORD_POLICY, message = "{pattern.password}")
	private String password;
	/** 確認パスワード */
	@NotBlank(groups = { ChangePasswordGroup.class, ResetPasswordGroup.class })
	private String passwordConfirm;

}
