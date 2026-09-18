package jp.co.sss.lms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.exception.NoLoginException;

/**
 * ログインユーザーユーティリティ
 * 
 * @author 東京ITスクール
 */
@Component
public class LoginUserUtil {

	@Autowired
	private LoginUserDto loginUserDto;

	/**
	 * ログインしているユーザーが管理者か判定
	 * 
	 * @return 管理者の場合true
	 */
	public boolean isAdmin() {
		return Constants.CODE_VAL_ROLL_ADMIN.equals(getLoginUserDto().getRole());
	}

	/**
	 * ログインしているユーザーが受講生か判定
	 * 
	 * @return 受講生の場合true
	 */
	public boolean isStudent() {
		return Constants.CODE_VAL_ROLL_STUDENT.equals(getLoginUserDto().getRole());

	}

	/**
	 * ログインしているユーザーが講師か判定
	 * 
	 * @return 講師の場合true
	 */
	public boolean isTeacher() {
		return Constants.CODE_VAL_ROLL_TEACHER.equals(getLoginUserDto().getRole());
	}

	/**
	 * ログインしているユーザーが企業担当者か判定
	 * 
	 * @return 企業担当者の場合true
	 */
	public boolean isCompany() {
		return Constants.CODE_VAL_ROLL_COMPANY.equals(getLoginUserDto().getRole());
	}

	/**
	 * ログインしているユーザーが育成担当者か判定
	 * 
	 * @return 育成担当者の場合true
	 */
	public boolean isTraining() {
		return Constants.CODE_VAL_ROLL_TRAINING.equals(getLoginUserDto().getRole());
	}

	/**
	 * ログイン情報を取得
	 * 
	 * @throws NoLoginException ログイン情報が存在しない場合、例外を送出する。
	 * @return loginUserDto
	 */
	public LoginUserDto getLoginUserDto() {
		if (!isLogin()) {
			throw new NoLoginException();
		}
		return loginUserDto;
	}

	/**
	 * ログインチェック
	 * 
	 * @return ログインしている場合はtrue
	 */
	public boolean isLogin() {
		return !(loginUserDto == null || loginUserDto.getUserId() == null
				|| loginUserDto.getLmsUserId() == null || loginUserDto.getRole() == null);
	}

	/**
	 * 遷移先画面の決定
	 * 
	 * @return 権限ごとの遷移先
	 */
	public String sendDisp() {
		if (isStudent()) {
			return "redirect:/course/detail";
		} else if (isTeacher()) {
			return "redirect:/course/list"; // Task.42
		} else if (isCompany()) {
			return "redirect:/user/list/student"; // Task.68
		} else if (isAdmin()) {
			return "redirect:/course/list"; // Task.78
		} else if (isTraining()) {
			return "";
		} else {
			throw new NoLoginException();
		}
	}

}
