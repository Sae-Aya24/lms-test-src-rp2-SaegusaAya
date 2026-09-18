package jp.co.sss.lms.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.dto.GenreDto;
import jp.co.sss.lms.form.GenreDetailForm;
import jp.co.sss.lms.form.GenreForm;
import jp.co.sss.lms.service.GenreService;

/**
 * ジャンルコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/genre")
public class GenreController {

	@Autowired
	private GenreService genreService;

	/**
	 * Task.97 試験カテゴリー一覧画面 初期表示
	 * 
	 * @param genreForm
	 * @param model
	 * @return 試験カテゴリー一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(GenreForm genreForm, Model model) {

		List<GenreDto> genreDtoList = genreService.getGenreDtoList(genreForm);
		model.addAttribute("genreDtoList", genreDtoList);

		return "genre/list";
	}

	/**
	 * Task.97 試験カテゴリー一覧画面 『削除（カテゴリー）』ボタン押下
	 * 
	 * @param genreForm
	 * @param redirectAttributes
	 * @return 試験カテゴリー一覧画面
	 */
	@RequestMapping(path = "/list", params = "delete", method = RequestMethod.POST)
	public String delete(GenreForm genreForm, RedirectAttributes redirectAttributes) {

		String message = genreService.delete(genreForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/genre/list";
	}

	/**
	 * Task.97 試験カテゴリー一覧画面 『削除（詳細情報）』ボタン押下
	 * 
	 * @param genreDetailForm
	 * @param redirectAttributes
	 * @return 試験カテゴリー一覧画面
	 */
	@RequestMapping(path = "/list", params = "detailDelete", method = RequestMethod.POST)
	public String detailDelete(GenreDetailForm genreDetailForm,
			RedirectAttributes redirectAttributes) {

		String message = genreService.detailDelete(genreDetailForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/genre/list";
	}

	/**
	 * Task.98 試験カテゴリー一覧画面 『新規登録』または『変更（カテゴリー）』ボタン押下
	 * 
	 * @param genreForm
	 * @return 試験カテゴリー登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(GenreForm genreForm) {
		genreService.getGenreForm(genreForm);
		return "genre/regist";
	}

	/**
	 * Task.98 試験カテゴリー登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param genreForm
	 * @param result
	 * @param redirectAttributes
	 * @return 試験カテゴリー一覧画面
	 */
	@RequestMapping(path = "/regist", params = "complete", method = RequestMethod.POST)
	public String complete(@Valid GenreForm genreForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "genre/regist";
		}

		String message = genreService.regist(genreForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/genre/list";
	}

	/**
	 * Task.99 試験カテゴリー一覧画面 『詳細カテゴリーを追加』または『変更（詳細情報）』ボタン押下
	 * 
	 * @param genreDetailForm
	 * @return 詳細試験カテゴリー登録画面
	 */
	@RequestMapping(path = "/detailRegist", method = RequestMethod.POST)
	public String detailRegist(GenreDetailForm genreDetailForm) {
		genreService.getGenreDetailForm(genreDetailForm);
		return "genre/detailRegist";
	}

	/**
	 * Task.99 詳細試験カテゴリー登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param genreDetailForm
	 * @param result
	 * @param redirectAttributes
	 * @return 試験カテゴリー一覧画面
	 */
	@RequestMapping(path = "/detailRegist", params = "complete", method = RequestMethod.POST)
	public String detailRegistComplete(@Valid GenreDetailForm genreDetailForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			return "genre/detailRegist";
		}

		String message = genreService.detailRegist(genreDetailForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/genre/list";
	}

}
