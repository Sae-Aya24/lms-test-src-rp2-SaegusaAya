package jp.co.sss.lms.controller;

import java.util.Enumeration;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import jp.co.sss.lms.dto.InfoDto;
import jp.co.sss.lms.form.LoginForm;
import jp.co.sss.lms.form.LoginGroup;
import jp.co.sss.lms.service.InfoService;
import jp.co.sss.lms.service.LoginService;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * ログインコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/")
public class LoginController {

	@Autowired
	private LoginService loginService;
	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private InfoService infoService;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private HttpSession session;

	/**
	 * ログイン画面 初期表示
	 * 
	 * @param loginForm
	 * @return ログイン画面
	 */
	@RequestMapping(path = "")
	public String index(@ModelAttribute LoginForm loginForm) {
		// 既にログインしている場合、権限ごとの初期画面に遷移
		if (loginUserUtil.isLogin()) {
			return loginUserUtil.sendDisp();
		}
		// お知らせ情報取得
		InfoDto infoDto = infoService.getInfo();
		session.setAttribute("infoDto", infoDto);
		return "login/index";
	}

	/**
	 * ログイン画面 『ログイン』ボタン押下
	 * 
	 * @param loginForm
	 * @param result
	 * @param session
	 * @param model
	 * @return ログイン後の画面
	 */
	@RequestMapping(value = "login", method = RequestMethod.POST)
	public String login(@Validated(LoginGroup.class) @ModelAttribute LoginForm loginForm,
			BindingResult result, HttpSession session, Model model) {
		// メッセージが残っていれば消す
		session.removeAttribute("logout");
		session.removeAttribute("sessionTimeout");
		// 入力チェック
		if (result.hasErrors()) {
			return "login/index";
		}
		// ログイン判定
		String message = loginService.getLoginInfo(loginForm.getLoginId(), loginForm.getPassword());
		if (!message.isEmpty()) {
			model.addAttribute("loginFaild", message);
			return "login/index";
		}
		// ログイン後の遷移先
		return loginUserUtil.sendDisp();
	}

	/**
	 * ログイン後ヘッダー『ログアウト』ボタン押下
	 * 
	 * @param loginForm
	 * @return ログイン画面
	 */
	@RequestMapping(value = "logout")
	public String logout() {

		// セッション情報を削除
		Enumeration<String> e = session.getAttributeNames();
		while (e.hasMoreElements()) {
			String key = e.nextElement();
			session.removeAttribute(key);
		}

		String message = messageUtil.getMessage("logout");
		session.setAttribute("logout", message);

		return "redirect:/";
	}

}
