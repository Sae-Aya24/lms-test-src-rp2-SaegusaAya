package jp.co.sss.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Task.10 ヘルプコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/help")
public class HelpController {

	/**
	 * Task.10 ヘルプ画面 初期表示
	 * 
	 * @return ヘルプ画面
	 */
	@RequestMapping(path = "", method = RequestMethod.GET)
	public String index() {
		return "help/index";
	}

}
