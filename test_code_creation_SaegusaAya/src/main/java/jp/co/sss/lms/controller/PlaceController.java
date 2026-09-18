package jp.co.sss.lms.controller;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.form.PlaceForm;
import jp.co.sss.lms.service.PlaceService;

/**
 * 会場コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/place")
public class PlaceController {

	@Autowired
	private PlaceService placeService;

	/**
	 * Task.95 会場一覧画面 初期表示
	 * 
	 * @param PlaceForm
	 * @param model
	 * @return 会場一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(PlaceForm placeForm, Model model) {
		placeService.getPlaceDtoList(placeForm, model);
		return "place/list";
	}

	/**
	 * Task.95 会場一覧画面 『削除』ボタン押下
	 * 
	 * @param placeForm
	 * @param model
	 * @param redirectAttributes
	 * @return 会場一覧画面
	 */
	@RequestMapping(path = "/delete")
	public String delete(PlaceForm placeForm, Model model, RedirectAttributes redirectAttributes) {

		Boolean result = placeService.checkDeletePlace(placeForm, model);
		if (result) {
			placeService.getPlaceDtoList(placeForm, model);
			return "place/list";
		}

		String message = placeService.delete(placeForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/place/list";
	}

	/**
	 * Task.96 会場一覧画面 『新規登録』または『変更』ボタン押下
	 * 
	 * @param placeForm
	 * @param model
	 * @return 会場登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(PlaceForm placeForm, Model model) {
		placeService.setPlaceForm(placeForm);
		return "place/regist";
	}

	/**
	 * Task.96 会場登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param placeForm
	 * @param result
	 * @param redirectAttributes
	 * @return 会場一覧画面
	 */
	@RequestMapping(path = "/regist", params = "complete", method = RequestMethod.POST)
	public String complete(@Valid PlaceForm placeForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		placeService.checkRegistPlace(placeForm, result);
		if (result.hasErrors()) {
			return "place/regist";
		}

		String message = placeService.regist(placeForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/place/list";
	}

}
