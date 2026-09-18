package jp.co.sss.lms.controller;

import java.util.Date;
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

import jp.co.sss.lms.dto.CompanyDto;
import jp.co.sss.lms.dto.CourseDto;
import jp.co.sss.lms.dto.ExamDetailDto;
import jp.co.sss.lms.dto.ExamDto;
import jp.co.sss.lms.dto.ExamResultDetailDto;
import jp.co.sss.lms.dto.GenreDetailDto;
import jp.co.sss.lms.dto.GenreDto;
import jp.co.sss.lms.dto.LmsUserDto;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.dto.PlaceDto;
import jp.co.sss.lms.dto.QuestionDto;
import jp.co.sss.lms.form.ExamQuestionForm;
import jp.co.sss.lms.form.ExamRegistForm;
import jp.co.sss.lms.form.ExamResultListForm;
import jp.co.sss.lms.service.CompanyService;
import jp.co.sss.lms.service.CourseService;
import jp.co.sss.lms.service.ExamService;
import jp.co.sss.lms.service.GenreService;
import jp.co.sss.lms.service.PlaceService;

/**
 * 試験コントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/exam")
public class ExamController {

	@Autowired
	private ExamService examService;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private CourseService courseService;
	@Autowired
	private PlaceService placeService;
	@Autowired
	private CompanyService companyService;
	@Autowired
	private GenreService genreService;

	/**
	 * 試験詳細画面 初期表示
	 * 
	 * @param examSectionId
	 * @param lmsUserId
	 * @param model
	 * @return 試験詳細画面
	 */
	@RequestMapping(path = "/detail", method = RequestMethod.POST)
	public String detail(Integer examSectionId, Integer lmsUserId, Model model) {

		// Task.32
		ExamDetailDto examDetailDto = examService.getExamDetail(examSectionId, lmsUserId);
		Date now = new Date();
		if (examDetailDto != null && examDetailDto.getPublicDate().compareTo(now) >= 0) {
			return "illegal";
		}
		model.addAttribute("examDetailDto", examDetailDto);

		return "exam/detail";
	}

	/**
	 * 試験結果詳細画面 初期表示
	 * 
	 * @param examResultId
	 * @param model
	 * @return 試験結果詳細画面
	 */
	@RequestMapping(path = "/resultDetail", method = RequestMethod.POST)
	public String resultDetail(Integer examResultId, Model model) {

		// 試験結果詳細の取得
		ExamResultDetailDto examResultDetailDto = examService.getExamResultDetail(examResultId);
		model.addAttribute("examResultDetailDto", examResultDetailDto);

		return "exam/resultDetail";
	}

	/**
	 * 試験開始画面 初期表示
	 * 
	 * @param examSectionId
	 * @param model
	 * @return 試験開始画面
	 */
	@RequestMapping(path = "/start", method = RequestMethod.POST)
	public String start(Integer examSectionId, Model model) {

		// 試験詳細DTOの取得
		ExamDetailDto examDetailDto = examService.getExamDetail(examSectionId,
				loginUserDto.getLmsUserId());

		// 公開前の試験ならエラー画面へ遷移
		Date now = new Date();
		if (examDetailDto != null && examDetailDto.getPublicDate().compareTo(now) >= 0) {
			return "illegal";
		}

		model.addAttribute("examDetailDto", examDetailDto);
		return "exam/start";
	}

	/**
	 * 試験開始画面 『試験を開始する』ボタン押下
	 * 
	 * @param examQuestionForm
	 * @return 試験問題画面
	 */
	@RequestMapping(path = "/question", method = RequestMethod.POST)
	public String question(@ModelAttribute ExamQuestionForm examQuestionForm) {

		// 試験問題画面フォームの設定
		examService.setExamQuestionForm(examQuestionForm);

		return "exam/question";
	}

	/**
	 * 試験問題画面 『確認画面へ進む』ボタン押下
	 * 
	 * @param examQuestionForm
	 * @param model
	 * @return 試験問題確認画面
	 */
	@RequestMapping(path = "/answerCheck", method = RequestMethod.POST)
	public String answerCheck(ExamQuestionForm examQuestionForm, Model model) {

		// 試験問題画面フォームの設定
		examService.setExamQuestionForm(examQuestionForm);

		// Task.37 回答数の表示
		int[] answerQuestionCount = examService.getAnswerQuestionCount(examQuestionForm);
		model.addAttribute("answerCount", answerQuestionCount[0]);
		model.addAttribute("questionCount", answerQuestionCount[1]);

		return "exam/answerCheck";
	}

	/**
	 * 試験問題確認画面 『回答を送信する』ボタン押下
	 * 
	 * @param examQuestionForm
	 * @param model
	 * @return 試験結果画面
	 */
	@RequestMapping(path = "/result", method = RequestMethod.POST)
	public String complete(ExamQuestionForm examQuestionForm, Model model) {

		// 試験結果登録
		Integer examResultId = examService.insert(examQuestionForm);

		// 試験結果詳細の取得
		ExamResultDetailDto examResultDetailDto = examService.getExamResultDetail(examResultId);
		model.addAttribute("examResultDetailDto", examResultDetailDto);

		return "exam/result";
	}

	/**
	 * Task.51 試験一覧画面 初期表示
	 * 
	 * @param model
	 * @return 試験一覧画面
	 */
	@RequestMapping(path = "/list")
	public String list(String examName, Model model) {

		// Task.81 ②検索条件の追加
		List<ExamDto> examDtoList = examService.getExamDtoList(examName);
		model.addAttribute("examDtoList", examDtoList);
		model.addAttribute("examName", examName);

		return "exam/list";
	}

	/**
	 * Task.52 試験結果一覧画面 初期表示
	 * 
	 * @param examResultListForm
	 * @param model
	 * @return 試験結果一覧画面
	 */
	@RequestMapping(path = "/resultList")
	public String resultList(@ModelAttribute ExamResultListForm examResultListForm, Model model) {
		// コース情報
		List<CourseDto> courseDtoList = courseService.getCourseDtoList();
		model.addAttribute("courseDtoList", courseDtoList);
		// 会場情報
		PlaceDto placeDto = placeService.getPlaceDto();
		model.addAttribute("placeDto", placeDto);
		// 企業情報
		List<CompanyDto> companyDtoList = companyService.getCompanyDtoList();
		model.addAttribute("companyDtoList", companyDtoList);
		// 試験情報
		ExamDetailDto examDetailDto = examService.getExamDetailDto(examResultListForm.getExamId());
		model.addAttribute("examDetailDto", examDetailDto);
		// ユーザー試験情報
		List<LmsUserDto> lmsUserDtoList = examService.getLmsUserDtoList(examResultListForm,
				examDetailDto.getNumOfQuestion());
		model.addAttribute("lmsUserDtoList", lmsUserDtoList);
		return "exam/resultList";
	}

	/**
	 * Task.53 試験結果一覧画面 『削除』ボタン押下
	 * 
	 * @param examResultListForm
	 * @return 試験結果一覧画面
	 */
	@RequestMapping(path = "/resultList", params = "delete", method = RequestMethod.POST)
	public String deleteResultList(ExamResultListForm examResultListForm,
			RedirectAttributes redirectAttributes) {

		String message = examService.deleteResultList(examResultListForm.getExamSectionId(),
				examResultListForm.getLmsUserId());

		redirectAttributes.addFlashAttribute("message", message);
		redirectAttributes.addFlashAttribute("examResultListForm", examResultListForm);
		return "redirect:/exam/resultList";

	}

	/**
	 * Task.56 試験プレビュー画面 初期表示
	 * 
	 * @param examId
	 * @param model
	 * @return 試験プレビュー画面
	 */
	@RequestMapping(path = "/preview", method = RequestMethod.POST)
	public String preview(Integer examId, Model model) {

		// 試験情報取得
		ExamDto examDto = examService.getExamDto(examId);
		model.addAttribute("examDto", examDto);
		// 試験問題解答取得
		List<QuestionDto> questionDtoList = examService.getExamPreview(examId);
		model.addAttribute("questionDtoList", questionDtoList);

		return "exam/preview";
	}

	/**
	 * Task.81 試験一覧画面 『削除』ボタン押下
	 * 
	 * @param examId
	 * @param redirectAttributes
	 * @return 試験一覧画面
	 */
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	public String delete(Integer examId, RedirectAttributes redirectAttributes) {

		String message = examService.delete(examId);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/exam/list";
	}

	/**
	 * Task.112 試験内容登録画面 初期表示
	 * 
	 * @param examRegistForm
	 * @param redirectAttributes
	 * @param model
	 * @return 試験内容登録画面
	 */
	@RequestMapping(path = "/regist", method = RequestMethod.POST)
	public String regist(ExamRegistForm examRegistForm, RedirectAttributes redirectAttributes,
			Model model) {

		List<GenreDto> genreDtoList = genreService.getGenreDtoList(null);
		model.addAttribute("genreDtoList", genreDtoList);

		if (examRegistForm.getExamId() != null) {
			examService.setExamRegistForm(examRegistForm);
		}

		return "exam/regist";
	}

	/**
	 * Task.112 試験内容登録画面 『次へ』ボタン押下
	 * 
	 * @param examRegistForm
	 * @param result
	 * @param model
	 * @return 試験問題登録画面
	 */
	@RequestMapping(path = "/registDetail", method = RequestMethod.POST)
	public String registDetail(@Valid ExamRegistForm examRegistForm, BindingResult result,
			Model model) {

		if (result.hasErrors()) {
			List<GenreDto> genreDtoList = genreService.getGenreDtoList(null);
			model.addAttribute("genreDtoList", genreDtoList);
			return "exam/regist";
		}

		// Task.113
		List<GenreDetailDto> genreDetailDtoList = genreService
				.getGenreDetailDtoList(examRegistForm.getGenreId());
		model.addAttribute("genreDetailDtoList", genreDetailDtoList);

		if (examRegistForm.getExamId() != null) {
			examService.setExamRegistFormQuestion(examRegistForm);
		}

		return "exam/registDetail";
	}

	/**
	 * Task.113 試験問題登録画面 『＋』ボタン押下
	 * 
	 * @param examRegistForm
	 * @param model
	 * @return 試験問題登録画面
	 */
	@RequestMapping(path = "/registDetail", params = "addDetail", method = RequestMethod.POST)
	public String registDetailAddDetail(ExamRegistForm examRegistForm, Model model) {

		examService.addDetail(examRegistForm);

		List<GenreDetailDto> genreDetailDtoList = genreService
				.getGenreDetailDtoList(examRegistForm.getGenreId());
		model.addAttribute("genreDetailDtoList", genreDetailDtoList);

		return "exam/registDetail";
	}

	/**
	 * Task.113 試験問題登録画面 『－』ボタン押下
	 * 
	 * @param examRegistForm
	 * @param model
	 * @return 試験問題登録画面
	 */
	@RequestMapping(path = "/registDetail", params = "remDetail", method = RequestMethod.POST)
	public String registDetailRemDetail(ExamRegistForm examRegistForm, Model model) {

		examService.remDetail(examRegistForm);

		List<GenreDetailDto> genreDetailDtoList = genreService
				.getGenreDetailDtoList(examRegistForm.getGenreId());
		model.addAttribute("genreDetailDtoList", genreDetailDtoList);

		return "exam/registDetail";
	}

	/**
	 * Task.113 試験問題登録画面 『登録する』ボタン押下
	 * 
	 * @param examRegistForm
	 * @param result
	 * @param redirectAttributes
	 * @return 試験一覧画面
	 */
	@RequestMapping(path = "/registDetail", params = "regist", method = RequestMethod.POST)
	public String registDetailRegist(ExamRegistForm examRegistForm, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {

		examService.questionRegistInputCheck(examRegistForm, result);
		if (result.hasErrors()) {
			List<GenreDetailDto> genreDetailDtoList = genreService
					.getGenreDetailDtoList(examRegistForm.getGenreId());
			model.addAttribute("genreDetailDtoList", genreDetailDtoList);
			return "exam/registDetail";
		}

		String message = examService.questionDetailRegist(examRegistForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/exam/list";
	}

	/**
	 * Task.113 試験問題登録画面 『変更する』ボタン押下
	 * 
	 * @param examRegistForm
	 * @param result
	 * @param redirectAttributes
	 * @return 試験一覧画面
	 */
	@RequestMapping(path = "/registDetail", params = "update", method = RequestMethod.POST)
	public String registDetailUpdate(ExamRegistForm examRegistForm, BindingResult result,
			Model model, RedirectAttributes redirectAttributes) {

		examService.questionRegistInputCheck(examRegistForm, result);
		if (result.hasErrors()) {
			List<GenreDetailDto> genreDetailDtoList = genreService
					.getGenreDetailDtoList(examRegistForm.getGenreId());
			model.addAttribute("genreDetailDtoList", genreDetailDtoList);
			return "exam/registDetail";
		}

		String message = examService.questionDetailUpdate(examRegistForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/exam/list";
	}

}
