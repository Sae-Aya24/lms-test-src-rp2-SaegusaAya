package jp.co.sss.lms.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import jp.co.sss.lms.dto.LmsUserDto;
import jp.co.sss.lms.dto.MyAccountDto;
import jp.co.sss.lms.dto.UserDetailDto;
import jp.co.sss.lms.form.UserListForm;
import jp.co.sss.lms.service.UserService;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * ユーザーコントローラー
 * 
 * @author 東京ITスクール
 */
@Controller
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private MessageUtil messageUtil;

	/**
	 * 利用規約画面 初期表示
	 * 
	 * @return 利用規約画面
	 */
	@RequestMapping(value = "/agreeSecurity")
	public String index() {
		return "user/agreeSecurity/index";
	}

	/**
	 * 利用規約画面 『次へ』ボタン押下
	 * 
	 * @param securityFlg
	 * @param model
	 * @return パスワード変更画面
	 */
	@RequestMapping(value = "/agreeSecurity/changePassword", method = RequestMethod.POST)
	public String agreeSecurity(Short securityFlg, Model model) {
		if (securityFlg != Constants.CODE_VAL_SECURITY_AGREE) {
			model.addAttribute("error",
					messageUtil.getMessage(Constants.PROP_KEY_SECURITY_AGREE_FLG));
			return "user/agreeSecurity/index";
		}
		userService.updateSecurityFlg();
		return "redirect:/";
	}

	/**
	 * ユーザー詳細画面 初期表示
	 * 
	 * @param lmsUserId
	 * @param model
	 * @return ユーザー詳細画面
	 */
	@RequestMapping(path = "/detail")
	public String detail(@RequestParam(required = false) Integer lmsUserId, Model model) {

		LmsUserDto lmsUserDto = userService.getUserDetail(lmsUserId);
		model.addAttribute("lmsUserDto", lmsUserDto);

		return "user/detail";
	}

	/**
	 * Task.43 ユーザー一覧画面 初期表示
	 * 
	 * @param userListForm
	 * @return ユーザー一覧画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list")
	public String list(@ModelAttribute UserListForm userListForm) throws ParseException {

		userService.setUserListForm(userListForm);

		if (!loginUserUtil.isAdmin()) {
			List<UserDetailDto> userDetailDtoList = userService.getUserDetailDtoList(userListForm);
			userListForm.setUserDetailDtoList(userDetailDtoList);
		}

		return "user/list";
	}

	/**
	 * Task.43 ユーザー一覧画面 『検索』ボタン押下
	 * 
	 * @param userListForm
	 * @param result
	 * @return ユーザー一覧画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list", params = "search", method = RequestMethod.POST)
	public String search(UserListForm userListForm, BindingResult result) throws ParseException {

		userService.searchInputCheck(userListForm, result);
		userService.setUserListForm(userListForm);

		List<UserDetailDto> userDetailDtoList = userService.getUserDetailDtoList(userListForm);
		userListForm.setUserDetailDtoList(userDetailDtoList);

		return "user/list";
	}

	/**
	 * Task.48 マイアカウント画面 初期表示
	 * 
	 * @param model
	 * @return マイアカウント画面
	 */
	@RequestMapping(path = "/myAccount")
	public String myAccount(Model model) {
		MyAccountDto myAccountDto = userService.getMyAccountDto();
		model.addAttribute("myAccountDto", myAccountDto);
		return "user/myAccount";
	}

	/**
	 * Task.68 ユーザー一覧(受講生一覧) 画面 初期表示
	 * 
	 * @param userListForm
	 * @param model
	 * @return ユーザー一覧(受講生一覧) 画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list/student")
	public String studentList(UserListForm userListForm, Model model) throws ParseException {
		// 経過期間ラベル設定
		userListForm.setPastTimeLabel(messageUtil.getMessage("setting.search.pastTimeLabel"));

		List<UserDetailDto> userDetailDtoList = userService
				.getStudentUserDetailDtoList(userListForm);
		model.addAttribute("userDetailDtoList", userDetailDtoList);

		return "user/list/student";
	}

	/**
	 * Task.70 ユーザー一覧(受講生以外のユーザー) 画面 初期表示
	 * 
	 * @param model
	 * @return ユーザー一覧(受講生以外のユーザー) 画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list/company")
	public String companyList(Model model) throws ParseException {
		List<UserDetailDto> userDetailDtoList = userService.getCompanyUserDetailDtoList();
		model.addAttribute("userDetailDtoList", userDetailDtoList);
		return "user/list/company";
	}

	/**
	 * Task.70 ユーザー一覧(受講生以外のユーザー) 画面 『削除』ボタン押下
	 * 
	 * @param userListForm
	 * @param redirectAttributes
	 * @return ユーザー一覧(受講生以外のユーザー) 画面
	 * @throws ParseException
	 */
	@RequestMapping(path = "/list/company", params = "delete", method = RequestMethod.POST)
	public String companyUserDelete(UserListForm userListForm,
			RedirectAttributes redirectAttributes) {

		String message = userService.companyUserDelete(userListForm);
		redirectAttributes.addFlashAttribute("message", message);

		return "redirect:/user/list/company";
	}

	/**
	 * Task.79 ユーザー一覧画面 『一括削除』ボタン押下
	 * 
	 * @param userListForm
	 * @param redirectAttributes
	 * @return ユーザー一覧画面
	 */
	@RequestMapping(path = "/bulkDelete", method = RequestMethod.POST)
	public String bulkDelete(UserListForm userListForm, RedirectAttributes redirectAttributes) {

		if (userListForm.getLmsUserIdArr() != null && userListForm.getLmsUserIdArr().length > 0) {
			List<Integer> lmsUserIdList = new ArrayList<>();
			for (String lmsUserId : userListForm.getLmsUserIdArr()) {
				lmsUserIdList.add(Integer.parseInt(lmsUserId));
			}
			String message = userService.bulkDelete(lmsUserIdList);
			redirectAttributes.addFlashAttribute("message", message);
		}

		return "redirect:/user/list";
	}

	/**
	 * Task.79 ユーザー一覧画面 『削除』ボタン押下
	 * 
	 * @param userForm
	 * @param redirectAttributes
	 * @return ユーザー一覧画面
	 */
	@RequestMapping(path = "/delete", method = RequestMethod.POST)
	public String delete(UserListForm userListForm, RedirectAttributes redirectAttributes) {
		List<Integer> lmsUserIdList = new ArrayList<>();
		lmsUserIdList.add(userListForm.getLmsUserId());
		String message = userService.bulkDelete(lmsUserIdList);
		redirectAttributes.addFlashAttribute("message", message);
		return "redirect:/user/list";
	}

}
