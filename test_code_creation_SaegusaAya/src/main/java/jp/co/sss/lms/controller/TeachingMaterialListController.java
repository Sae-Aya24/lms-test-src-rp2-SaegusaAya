package jp.co.sss.lms.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 教材ダウンロードコントローラー
 * 
 * @author 東京ITスクール
 */

@Controller
public class TeachingMaterialListController {

	/**
	 * 教材ダウンロード初期表示
	 * 
	 * @param model
	 * @return 教材ダウンロード画面
	 */
	@RequestMapping(path = "/download/teachingMaterialList")
	public String showTeachingMaterialList() {
		// 未実装
		return "course/download/teachingMaterialList";
	}

}
