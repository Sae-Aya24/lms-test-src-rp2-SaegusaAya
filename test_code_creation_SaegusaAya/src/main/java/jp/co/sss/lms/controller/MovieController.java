package jp.co.sss.lms.controller;

import java.util.List;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.dto.MovieCategoryDto;
import jp.co.sss.lms.form.MovieCategoryForm;
import jp.co.sss.lms.form.MovieForm;
import jp.co.sss.lms.service.MovieService;

/**
 * Task.30 動画コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/movie")
public class MovieController {

	@Autowired
	private MovieService movieService;

	/**
	 * Task.30 動画視聴画面 初期表示
	 * 
	 * @param model
	 * @return 動画視聴画面
	 */
	@RequestMapping(path = "")
	public String index(Model model) {
		List<MovieCategoryDto> movieCategoryDtoList = movieService.getMovieCategoryList();
		model.addAttribute("movieCategoryDtoList", movieCategoryDtoList);
		return "movie/index";
	}

	/**
	 * Task.100 動画一覧画面 初期表示
	 * 
	 * @param movieCategoryForm
	 * @param model
	 * @return 動画一覧画面
	 */
	@RequestMapping(path = "/categoryList")
	public String categoryList(@ModelAttribute MovieCategoryForm movieCategoryForm,
			@ModelAttribute MovieForm movieForm, Model model) {
		List<MovieCategoryDto> movieCategoryDtoList = movieService
				.searchMovieCategoryList(movieCategoryForm.getMovieCategoryName());
		model.addAttribute("movieCategoryDtoList", movieCategoryDtoList);
		return "movie/categoryList";
	}

	/**
	 * Task.100 動画一覧画面 『削除（カテゴリ情報）』ボタン押下
	 * 
	 * @param movieCategoryForm
	 * @param result
	 * @param movieForm
	 * @param model
	 * @param redirectAttributes
	 * @return 動画一覧画面
	 */
	@RequestMapping(path = "/categoryDelete", method = RequestMethod.POST)
	public String categoryDelete(MovieCategoryForm movieCategoryForm, BindingResult result,
			MovieForm movieForm, Model model, RedirectAttributes redirectAttributes) {
		String message = movieService.categoryDelete(movieCategoryForm, result);
		if (result.hasErrors()) {
			List<MovieCategoryDto> movieCategoryDtoList = movieService
					.searchMovieCategoryList(movieCategoryForm.getMovieCategoryName());
			model.addAttribute("movieCategoryDtoList", movieCategoryDtoList);
			return "movie/categoryList";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/movie/categoryList";
	}

	/**
	 * Task.100 動画一覧画面 『削除（動画一覧）』ボタン押下
	 * 
	 * @param movieCategoryForm
	 * @param movieForm
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return 動画一覧画面
	 */
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	public String delete(MovieCategoryForm movieCategoryForm, MovieForm movieForm,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		String message = movieService.delete(movieForm, result);
		if (result.hasErrors()) {
			List<MovieCategoryDto> movieCategoryDtoList = movieService
					.searchMovieCategoryList(movieCategoryForm.getMovieCategoryName());
			model.addAttribute("movieCategoryDtoList", movieCategoryDtoList);
			return "movie/categoryList";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/movie/categoryList";
	}

	/**
	 * Task.100 動画一覧画面 『↑』ボタン押下
	 * 
	 * @param movieCategoryForm
	 * @param movieForm
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return 動画一覧画面
	 */
	@RequestMapping(path = "/updateMoveup", method = RequestMethod.POST)
	public String updateMoveup(MovieCategoryForm movieCategoryForm, MovieForm movieForm,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		String message = movieService.updateMoveup(movieForm, result);
		if (result.hasErrors()) {
			List<MovieCategoryDto> movieCategoryDtoList = movieService
					.searchMovieCategoryList(movieCategoryForm.getMovieCategoryName());
			model.addAttribute("movieCategoryDtoList", movieCategoryDtoList);
			return "movie/categoryList";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/movie/categoryList";
	}

	/**
	 * Task.100 動画一覧画面 『↓』ボタン押下
	 * 
	 * @param movieCategoryForm
	 * @param movieForm
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return 動画一覧画面
	 */
	@RequestMapping(path = "/updateMovedown", method = RequestMethod.POST)
	public String updateMovedown(MovieCategoryForm movieCategoryForm, MovieForm movieForm,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {
		String message = movieService.updateMovedown(movieForm, result);
		if (result.hasErrors()) {
			List<MovieCategoryDto> movieCategoryDtoList = movieService
					.searchMovieCategoryList(movieCategoryForm.getMovieCategoryName());
			model.addAttribute("movieCategoryDtoList", movieCategoryDtoList);
			return "movie/categoryList";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/movie/categoryList";
	}

	/**
	 * Task.101 動画一覧画面 『新規登録』または『変更（カテゴリ情報）』ボタン押下
	 * 
	 * @param movieCategoryForm
	 * @return 動画カテゴリー登録画面
	 */
	@RequestMapping(path = "/categoryRegist", method = RequestMethod.POST)
	public String categoryRegist(MovieCategoryForm movieCategoryForm) {
		movieService.setMovieCategoryForm(movieCategoryForm);
		return "movie/categoryRegist";
	}

	/**
	 * Task.101 動画カテゴリー登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param movieCategoryForm
	 * @param result
	 * @param redirectAttributes
	 * @return 動画一覧画面
	 */
	@RequestMapping(path = "/categoryRegist", params = "complete", method = RequestMethod.POST)
	public String categoryRegistComplete(@Valid MovieCategoryForm movieCategoryForm,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "movie/categoryRegist";
		}
		String message = movieService.categoryRegistComplete(movieCategoryForm, result);
		if (result.hasErrors()) {
			return "movie/categoryRegist";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/movie/categoryList";
	}

	/**
	 * Task.102 動画一覧画面 『動画を追加』または『変更（動画一覧）』ボタン押下
	 * 
	 * @param movieForm
	 * @param redirectAttributes
	 * @return 動画登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(MovieForm movieForm, RedirectAttributes redirectAttributes) {
		movieService.setMovieForm(movieForm);
		return "movie/regist";
	}

	/**
	 * Task.102 動画変更画面 『登録』または『変更』ボタン押下
	 * 
	 * @param movieForm
	 * @param result
	 * @param redirectAttributes
	 * @return 動画一覧画面
	 */
	@RequestMapping(path = "/regist", params = "complete", method = RequestMethod.POST)
	public String registComplete(@Valid MovieForm movieForm, BindingResult result,
			RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "movie/regist";
		}
		String message = movieService.movieRegistComplete(movieForm, result);
		if (result.hasErrors()) {
			return "movie/regist";
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/movie/categoryList";
	}

}
