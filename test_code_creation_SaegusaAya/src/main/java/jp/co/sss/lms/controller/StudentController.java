package jp.co.sss.lms.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.dto.SubsidyCategoryDto;
import jp.co.sss.lms.form.StudentRegistForm;
import jp.co.sss.lms.form.StudentUpdateForm;
import jp.co.sss.lms.service.StudentService;
import jp.co.sss.lms.service.SubsidyService;

/**
 * 受講生情報コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/student")
public class StudentController {

	@Autowired
	private StudentService studentService;
	@Autowired
	private SubsidyService subsidyService;

	/**
	 * Task.67 受講生登録画面 初期表示
	 * 
	 * @param studentRegistForm
	 * @return 受講生登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.GET)
	public String regist(@ModelAttribute StudentRegistForm studentRegistForm) {
		studentService.setStudentRegistForm(studentRegistForm);
		return "student/regist";
	}

	/**
	 * Task.67 受講生登録画面 『登録』ボタン押下
	 * 
	 * @param studentRegistForm
	 * @param result
	 * @param redirectAttributes
	 * @return ユーザー一覧(受講生一覧)画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String registPost(StudentRegistForm studentRegistForm, BindingResult result,
			RedirectAttributes redirectAttributes) {

		studentService.studentRegistInputCheck(studentRegistForm, result);
		if (result.hasErrors()) {
			List<SubsidyCategoryDto> subsidyCategoryDtoList = subsidyService
					.getSubsidyCategoryList();
			studentRegistForm.setSubsidyCategoryDtoList(subsidyCategoryDtoList);
			return "student/regist";
		}

		String message = studentService.registStudent(studentRegistForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/user/list/student";
	}

	/**
	 * Task.69 受講生変更画面 初期表示
	 * 
	 * @param studentUpdateForm
	 * @param model
	 * @return 受講生変更画面
	 */
	@RequestMapping(path = "/update")
	public String update(StudentUpdateForm studentUpdateForm, Model model) {
		// ユーザー詳細取得
		studentService.getStudentUpdateForm(studentUpdateForm);
		// 助成金一覧取得
		List<SubsidyCategoryDto> subsidyCategoryDtoList = subsidyService.getSubsidyCategoryList();
		model.addAttribute("subsidyCategoryDtoList", subsidyCategoryDtoList);
		return "student/update";
	}

	/**
	 * Task.69 受講生変更画面 『変更』ボタン押下
	 * 
	 * @param studentUpdateForm
	 * @param result
	 * @param model
	 * @param redirectAttributes
	 * @return ユーザー一覧（受講生一覧) 画面
	 */
	@RequestMapping(path = "/update", params = "complete", method = RequestMethod.POST)
	public String updateComplete(@Validated StudentUpdateForm studentUpdateForm,
			BindingResult result, Model model, RedirectAttributes redirectAttributes) {

		if (result.hasErrors()) {
			// 助成金一覧取得
			List<SubsidyCategoryDto> subsidyCategoryDtoList = subsidyService.getSubsidyCategoryList();
			model.addAttribute("subsidyCategoryDtoList", subsidyCategoryDtoList);
			return "student/update";
		}
		String message = studentService.updateStudent(studentUpdateForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/user/list/student";
	}

}
