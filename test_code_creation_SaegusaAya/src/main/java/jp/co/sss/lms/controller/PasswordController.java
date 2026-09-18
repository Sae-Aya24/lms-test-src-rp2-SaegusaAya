package jp.co.sss.lms.controller;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import jp.co.sss.lms.enums.LmsUserRoleEnum;
import jp.co.sss.lms.form.ChangePasswordGroup;
import jp.co.sss.lms.form.LoginForm;
import jp.co.sss.lms.form.MailAddressForm;
import jp.co.sss.lms.form.ResetPasswordGroup;
import jp.co.sss.lms.service.LoginService;
import jp.co.sss.lms.service.PasswordService;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * パスワードコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/password")
public class PasswordController {

	@Autowired
	private PasswordService passwordService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private MessageUtil messageUtil;

	/**
	 * パスワード変更画面 初期表示
	 * 
	 * @return パスワード変更画面
	 */
	@RequestMapping(value = "/changePassword")
	public String index(@ModelAttribute LoginForm loginForm) {
		return "password/changePassword";
	}

	/**
	 * パスワード変更画面 『変更』ボタン押下
	 * 
	 * @param loginForm
	 * @param result
	 * @return 遷移先画面
	 */
	@RequestMapping(value = "/changePassword/change", method = RequestMethod.POST)
	public String change(@Validated(ChangePasswordGroup.class) LoginForm loginForm,
			BindingResult result, Model model) {
		// 入力チェック（変更用）
		passwordService.changePasswordInputCheck(loginForm, result);
		if (result.hasErrors()) {
			return "password/changePassword";
		}
		// パスワード変更
		String error = passwordService.changePassword(loginForm);
		if (error != null) {
			model.addAttribute("error", error);
			return "password/changePassword";
		}
		// 権限ごとの初期画面へ遷移
		return loginUserUtil.sendDisp();
	}

	/**
	 * ログイン画面 『パスワードを忘れた方はこちら』リンク押下
	 * 
	 * @param mailAddressForm
	 * @param model
	 * @return パスワード再設定画面
	 */
	@RequestMapping(value = "/resetPassword", method = RequestMethod.GET)
	public String index(@ModelAttribute MailAddressForm mailAddressForm, Model model) {
		// Task.05 ①権限プルダウンの追加
		mailAddressForm.setRoleEnum(LmsUserRoleEnum.values());

		// 設定ファイルのメール送信フラグが0の場合、メッセージ表示処理を追加
		boolean sendFlg = messageUtil.getMessage("setting.mail.send.flg").equals("0") ? true
				: false;
		model.addAttribute("sendFlg", sendFlg);

		return "password/resetPassword";
	}

	/**
	 * パスワード再設定画面 『送信』ボタン押下
	 * 
	 * @return パスワード再設定メール送信完了画面
	 */
	@RequestMapping(value = "/resetPassword/complete", method = RequestMethod.POST)
	// Task.04 ①メールアドレスの入力チェック
	public String complete(@Valid MailAddressForm mailAddressForm, BindingResult result,
			HttpServletRequest request, Model model) {

		// フォームの入力チェック
		if (result.hasErrors()) {
			// Task.04 ①メールアドレスの入力チェック
			mailAddressForm.setRoleEnum(LmsUserRoleEnum.values());
			boolean sendFlg = messageUtil.getMessage("setting.mail.send.flg").equals("0") ? true : false;
			model.addAttribute("sendFlg", sendFlg);
			return "password/resetPassword";
		}

		// パスワード変更テーブル登録
		passwordService.registTemporaryPassStorage(mailAddressForm);

		// パスワード再設定メール送信
		String sendFlg = messageUtil.getMessage("setting.mail.send.flg");
		if (!sendFlg.equals("0")) {
			passwordService.sendPasswordResetMail(request, mailAddressForm);
		}

		model.addAttribute("mailAddress", mailAddressForm.getMailAddress());
		model.addAttribute("timeLimit", messageUtil.getMessage("setting.timelimit.hour"));

		return "password/complete";
	}

	/**
	 * パスワード再設定メール リンク押下
	 * 
	 * @param redirectAttributes
	 * @param key
	 * @param model
	 * @return パスワード再設定（パスワード変更）画面
	 */
	@RequestMapping(value = "/resetPassword/set", method = RequestMethod.GET)
	public String set(@RequestParam("key") String key, RedirectAttributes redirectAttributes,
			Model model) {

		// アクセス妥当性確認
		Integer userId = passwordService.checkAccessValidity(key);
		if (userId == null) {
			model.addAttribute("timeLimit", messageUtil.getMessage("setting.timelimit.hour"));
			model.addAttribute("tismail", messageUtil.getMessage("setting.tismail"));
			return "password/error";
		}

		redirectAttributes.addFlashAttribute("userId", userId);
		return "redirect:/password/changePassword/mailInput";
	}

	/**
	 * パスワード再設定（パスワード変更）画面 初期表示
	 * 
	 * @param userId
	 * @param loginForm
	 * 
	 * @return 遷移先画面
	 */
	@RequestMapping(value = "/changePassword/mailInput")
	public String mailInput(@ModelAttribute("userId") Integer userId,
			@ModelAttribute LoginForm loginForm) {

		// ログインIDを取得（この時点でログインが行われる）
		String loginId = loginService.getLoginId(userId);
		if (StringUtils.isBlank(loginId)) {
			return "password/error";
		}

		loginForm.setLoginId(loginId);
		return "password/mailInput";
	}

	/**
	 * パスワード再設定（パスワード変更）画面 『変更』ボタン押下
	 * 
	 * @param loginForm
	 * @param result
	 * 
	 * @return 遷移先画面
	 */
	@RequestMapping(value = "/changePassword/mailComplete", method = RequestMethod.POST)
	public String mailComplete(
			@Validated(ResetPasswordGroup.class) @ModelAttribute LoginForm loginForm,
			BindingResult result, Model model) {
		// 入力チェック（再設定用）
		passwordService.resetPasswordInputCheck(loginForm, result);
		if (result.hasErrors()) {
			return "password/mailInput";
		}
		// パスワード変更
		String error = passwordService.changePassword(loginForm);
		if (error != null) {
			model.addAttribute("error", error);
			return "password/mailInput";
		}
		// 権限ごとの初期画面へ遷移
		return loginUserUtil.sendDisp();
	}

	/**
	 * Task.45 パスワード再発行画面 初期表示
	 * 
	 * @param userId
	 * @param model
	 * @return パスワード再発行画面
	 */
	@RequestMapping(value = "/reissuePassword", method = RequestMethod.POST)
	public String reissuePassword(Integer userId, Model model) {

		String[] credential = passwordService.reissuePassword(userId);
		model.addAttribute("loginId", credential[0]);
		model.addAttribute("randomPassword", credential[1]);

		return "password/reissuePassword";
	}

}
