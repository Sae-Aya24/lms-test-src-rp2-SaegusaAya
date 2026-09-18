package jp.co.sss.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * ファイル共有コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/fileshare")
public class FileShareController {

	/**
	 * ファイル一覧画面 初期表示
	 * 
	 * @return ファイル一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list() {
		// 未実装
		return "fileshare/list/index";
	}

}
