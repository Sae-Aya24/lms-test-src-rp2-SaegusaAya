package jp.co.sss.lms.controller;

import java.io.IOException;
import java.util.List;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.fasterxml.jackson.core.JsonParseException;

import jp.co.sss.lms.dto.InfoDto;
import jp.co.sss.lms.form.InfoForm;
import jp.co.sss.lms.service.InfoService;
import net.arnx.jsonic.JSON;

/**
 * お知らせコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/info")
public class InfoController {

	@Autowired
	private InfoService infoService;
	@Autowired
	private HttpSession session;

	/**
	 * お知らせ情報取得
	 * 
	 * @return お知らせ情報
	 * @throws JsonParseException
	 * @throws IOException
	 */
	@RequestMapping(path = "")
	@ResponseBody
	public String index() throws JsonParseException, IOException {

		// お知らせ情報を取得
		InfoDto infoDto = infoService.getInfo();
		// セッションに格納
		session.setAttribute("infoDto", infoDto);
		// JSON形式で返す
		return JSON.encode(infoDto);

	}

	/**
	 * Task.82 お知らせ画面 初期表示
	 * 
	 * @param model
	 * @return お知らせ画面
	 */
	@RequestMapping(path = "/latest")
	public String latest(Model model) {

		InfoDto infoDto = infoService.getInfo();
		model.addAttribute("infoDto", infoDto);

		return "info/latest";
	}

	/**
	 * Task.83 お知らせ画面 『登録』ボタン押下<br />
	 * Task.85 お知らせ履歴一覧画面 『変更』ボタン押下
	 * 
	 * @param infoForm
	 * @return お知らせ登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(@ModelAttribute InfoForm infoForm) {
		return "info/regist";
	}

	/**
	 * Task.83 お知らせ登録画面 『登録』ボタン押下
	 * 
	 * @param infoForm
	 * @param result
	 * @param redirectAttributes
	 * @return お知らせ画面
	 */
	@RequestMapping(path = "/regist", params = "regist", method = RequestMethod.POST)
	public String registComplete(@Valid InfoForm infoForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "info/regist";
		}

		String message = infoService.insert(infoForm);
		redirectAttributes.addFlashAttribute("message", message);

		InfoDto infoDto = infoService.getInfo();
		session.setAttribute("infoDto", infoDto);

		return "redirect:/info/latest";
	}

	/**
	 * Task.84 お知らせ画面 『一覧を開く』ボタン押下
	 * 
	 * @param model
	 * @return お知らせ履歴一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(Model model) {

		List<InfoDto> infoDtoList = infoService.getInfoDtoList();
		model.addAttribute("infoDtoList", infoDtoList);

		return "info/list";
	}

	/**
	 * Task.85 お知らせ登録画面 『変更』ボタン押下
	 * 
	 * @param infoForm
	 * @param result
	 * @param redirectAttributes
	 * @return お知らせ履歴一覧画面
	 */
	@RequestMapping(path = "/regist", params = "update", method = RequestMethod.POST)
	public String updateComplete(@Valid InfoForm infoForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "info/regist";
		}

		String message = infoService.update(infoForm);
		redirectAttributes.addFlashAttribute("message", message);

		InfoDto infoDto = infoService.getInfo();
		session.setAttribute("infoDto", infoDto);

		return "redirect:/info/list";

	}

	/**
	 * Task.85 お知らせ履歴一覧画面 『削除』ボタン押下
	 * 
	 * @param infoForm
	 * @param redirectAttributes
	 * @return お知らせ編集画面
	 */
	@RequestMapping(path = "/list", params = "delete", method = RequestMethod.POST)
	public String delete(InfoForm infoForm, RedirectAttributes redirectAttributes) {

		String message = infoService.delete(infoForm);
		redirectAttributes.addFlashAttribute("message", message);

		InfoDto infoDto = infoService.getInfo();
		session.setAttribute("infoDto", infoDto);

		return "redirect:/info/list";

	}

}
