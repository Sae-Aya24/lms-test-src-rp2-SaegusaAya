package jp.co.sss.lms.controller;

import java.text.ParseException;
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

import jp.co.sss.lms.dto.CourseServiceCourseDto;
import jp.co.sss.lms.dto.CourseWithTeachingMaterialCountDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.form.CourseDetailRegistForm;
import jp.co.sss.lms.form.CourseRegistForm;
import jp.co.sss.lms.service.CourseService;

/**
 * コースコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/course")
public class CourseController {

	@Autowired
	private CourseService courseService;
	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * コース詳細画面 初期表示
	 * 
	 * @param model
	 * @return コース詳細画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/detail")
	public String detail(Integer courseId, Model model) throws ParseException {

		// Task.42
		Integer targetCourseId = courseId == null ? loginUserDto.getCourseId() : courseId;

		// パラメータチェック
		String message = courseService.checkCourseId(targetCourseId);
		if (!message.isEmpty()) {
			return "illegal";
		}

		// コース詳細関連情報の取得
		CourseServiceCourseDto courseServiceCourseDto = courseService
				.getCourseDetail(targetCourseId);
		model.addAttribute("courseServiceCourseDto", courseServiceCourseDto);

		return "course/detail";
	}

	/**
	 * Task.42 コース一覧画面 初期表示
	 * 
	 * @param model
	 * @return コース一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(Model model) {

		List<CourseWithTeachingMaterialCountDto> courseWithTeachingMaterialCountDtoList = courseService
				.getCourseList();
		model.addAttribute("courseDtoList", courseWithTeachingMaterialCountDtoList);

		return "course/list";
	}

	/**
	 * Task.114 コース登録画面 初期表示
	 * 
	 * @param courseRegistForm
	 * @return コース登録画面
	 */
	@RequestMapping(path = "/regist")
	public String regist(@ModelAttribute CourseRegistForm courseRegistForm) {
		courseService.setCourseRegistForm(courseRegistForm);
		if (courseRegistForm.getCourseId() != null) {
			courseService.getCourseReportDto(courseRegistForm);
		}
		return "course/regist";
	}

	/**
	 * Task.114 コース登録画面 『決定』ボタン押下
	 * 
	 * @param courseRegistForm
	 * @param result
	 * @return コース詳細変更画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String registPost(@Valid CourseRegistForm courseRegistForm, BindingResult result)
			throws ParseException {

		courseService.courseRegistFormInputCheck(courseRegistForm, result);
		if (result.hasErrors()) {
			courseService.setCourseRegistForm(courseRegistForm);
			return "course/regist";
		}

		Integer courseId = courseService.registCourseReport(courseRegistForm);
		return "redirect:/course/registDetail?courseId=" + courseId;
	}

	/**
	 * Task.115 コース詳細登録画面 初期表示
	 * 
	 * @param courseDetailRegistForm
	 * @return コース詳細登録画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/registDetail")
	public String registDetail(@ModelAttribute CourseDetailRegistForm courseDetailRegistForm)
			throws ParseException {
		courseService.setCourseDetailRegistForm(courseDetailRegistForm);
		return "course/registDetail";
	}

	/**
	 * Task.115 コース詳細登録画面 『決定』ボタン押下
	 * 
	 * @param courseDetailRegistForm
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(path = "/registDetail", method = RequestMethod.POST)
	public String registDetailPost(CourseDetailRegistForm courseDetailRegistForm,
			RedirectAttributes redirectAttributes) throws ParseException {
		String message = courseService.registCourseDetail(courseDetailRegistForm);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/course/detail?courseId=" + courseDetailRegistForm.getCourseId();
	}

}
