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

import jp.co.sss.lms.dto.FaqCategoryDto;
import jp.co.sss.lms.dto.FaqDto;
import jp.co.sss.lms.form.FaqCategoryRegistForm;
import jp.co.sss.lms.form.FaqCategorySearchForm;
import jp.co.sss.lms.form.FaqRegistForm;
import jp.co.sss.lms.form.FaqSearchForm;
import jp.co.sss.lms.service.FaqService;

/**
 * Task.11 よくある質問コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/faq")
public class FaqController {

	@Autowired
	private FaqService faqService;

	/**
	 * Task.11 よくある質問画面 初期表示
	 * 
	 * @param faqSearchForm
	 * @param model
	 * @return よくある質問画面
	 */
	@RequestMapping(path = "")
	public String index(FaqSearchForm faqSearchForm, Model model) {

		// よくある質問カテゴリDTOリストの取得
		List<FaqCategoryDto> faqCategoryDtoList = faqService.getFaqCategoryList();
		model.addAttribute("faqCategoryList", faqCategoryDtoList);

		// よくある質問DTOリストの取得
		List<FaqDto> faqDtoList = faqService.getFaqDtoList(faqSearchForm);
		model.addAttribute("faqList", faqDtoList);

		return "faq/index";
	}

	/**
	 * Task.89 よくある質問一覧画面 初期表示 および 『検索』ボタン押下
	 * 
	 * @param faqCategorySearchForm
	 * @param model
	 * @return よくある質問一覧画面
	 */
	@RequestMapping(path = "/categoryList")
	public String categoryList(FaqCategorySearchForm faqCategorySearchForm, Model model) {

		List<FaqCategoryDto> faqCategoryDtoList = faqService
				.getFaqCategoryDtoList(faqCategorySearchForm);
		model.addAttribute("faqCategoryDtoList", faqCategoryDtoList);

		return "faq/categoryList";
	}

	/**
	 * Task.89 よくある質問一覧画面 『削除（カテゴリ情報）』ボタン押下
	 * 
	 * @param faqCategorySearchForm
	 * @param redirectAttributes
	 * @return よくある質問一覧画面
	 */
	@RequestMapping(path = "/categoryList", params = "categoryDelete", method = RequestMethod.POST)
	public String categoryDelete(FaqCategorySearchForm faqCategorySearchForm,
			RedirectAttributes redirectAttributes) {
		String message = null;
		String error = faqService.deleteCategoryInputCheck(faqCategorySearchForm);
		redirectAttributes.addFlashAttribute("error", error);
		if (error == null) {
			message = faqService.categoryDelete(faqCategorySearchForm);
		}
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/faq/categoryList";
	}

	/**
	 * Task.89 よくある質問一覧画面 『削除（質問一覧）』ボタン押下
	 * 
	 * @param frequentlyAskedQuestionId
	 * @param redirectAttributes
	 * @return よくある質問一覧画面
	 */
	@RequestMapping(path = "/categoryList", params = "delete", method = RequestMethod.POST)
	public String delete(Integer frequentlyAskedQuestionId, RedirectAttributes redirectAttributes) {
		String message = faqService.delete(frequentlyAskedQuestionId);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/faq/categoryList";
	}

	/**
	 * Task.90 よくある質問一覧画面 『新規登録』または『変更（カテゴリ情報）』ボタン押下
	 * 
	 * @param faqCategoryRegistForm
	 * @return 質問カテゴリー登録画面
	 */
	@RequestMapping(path = "/categoryRegist", method = RequestMethod.POST)
	public String categoryRegist(FaqCategoryRegistForm faqCategoryRegistForm) {
		faqService.setFaqCategoryRegistForm(faqCategoryRegistForm);
		return "faq/categoryRegist";
	}

	/**
	 * Task.90 質問カテゴリー登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param faqCategoryRegistForm
	 * @param result
	 * @param redirectAttributes
	 * @return よくある質問一覧画面
	 */
	@RequestMapping(path = "/categoryRegist", params = "complete", method = RequestMethod.POST)
	public String categoryRegistComplete(@Valid FaqCategoryRegistForm faqCategoryRegistForm,
			BindingResult result, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			return "faq/categoryRegist";
		}
		String message = faqService.categoryComplete(faqCategoryRegistForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/faq/categoryList";
	}

	/**
	 * Task.91 よくある質問一覧画面 『質問追加』または『変更（質問一覧）』ボタン押下
	 * 
	 * @param faqRegistForm
	 * @param model
	 * @return 質問登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(FaqRegistForm faqRegistForm, Model model) {
		List<FaqCategoryDto> faqCategoryDtoList = faqService.getFaqCategoryList();
		model.addAttribute("faqCategoryDtoList", faqCategoryDtoList);
		faqService.setFaqRegistForm(faqRegistForm);
		return "faq/regist";
	}

	/**
	 * Task.91 質問登録画面 『登録する』または『変更する』ボタン押下
	 * 
	 * @param faqRegistForm
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return よくある質問一覧画面
	 */
	@RequestMapping(path = "/regist", params = "complete", method = RequestMethod.POST)
	public String registComlete(@Valid FaqRegistForm faqRegistForm, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			List<FaqCategoryDto> faqCategoryDtoList = faqService.getFaqCategoryList();
			model.addAttribute("faqCategoryDtoList", faqCategoryDtoList);
			return "faq/regist";
		}
		String message = faqService.registComlete(faqRegistForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/faq/categoryList";
	}

}
