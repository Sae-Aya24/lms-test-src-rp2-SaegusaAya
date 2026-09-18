package jp.co.sss.lms.service;

import java.util.Calendar;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.LoginUser;
import jp.co.sss.lms.mapper.LoginMapper;
import jp.co.sss.lms.mapper.MUserMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.DateUtil;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.PasswordUtil;

/**
 * ログイン情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class LoginService {

	@Autowired
	private LoginMapper loginMapper;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private DateUtil dateUtil;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private HttpSession session;
	@Autowired
	private MUserMapper mUserMapper;
	@Autowired
	private PasswordUtil passwordUtil;

	@Value("${setting.lock.minute}")
	private Integer lockMinute;
	@Value("${setting.lock.count}")
	private Integer lockCount;

	/**
	 * ログイン処理
	 * 
	 * @return String
	 */
	public String getLoginInfo(String loginId, String password) {

		// salt + ストレッチングしたパスワードを取得
		String saltPassword = passwordUtil.getSaltedAndStrechedPassword(password, loginId);

		// アカウントロックチェック
		if (!checkLockTime()) {
			return messageUtil.getMessage(Constants.VALID_KEY_LOGIN_LOCK);
		}
		// ログイン情報を取得
		LoginUser loginUser = loginMapper.getLoginDetailByLoginIdAndPassword(loginId, saltPassword,
				Constants.DB_FLG_FALSE);

		// ログイン情報を取得できなかった場合
		if (loginUser == null) {
			loginUserDto.setPasswordNgCount(loginUserDto.getPasswordNgCount() == null ? 1
					: loginUserDto.getPasswordNgCount() + 1);
			if (!checkLockCount()) {
				return messageUtil.getMessage(Constants.VALID_KEY_LOGIN_LOCK);
			} else {
				return messageUtil.getMessage(Constants.VALID_KEY_LOGIN);
			}
			// ログイン情報を取得できた場合(講師権限及びログイン．非表示フラグが立っている場合)
		} else if (!checkPlaceDisplay(loginUser.getRole(), loginUser.getHiddenFlg())) {
			return messageUtil.getMessage(Constants.VALID_KEY_LOGIN_PLACENODISPLAY);
		} else {
			// ログイン情報をセッションに格納
			BeanUtils.copyProperties(loginUser, loginUserDto);
			loginUserDto.setPasswordNgCount(0);
			loginUserDto.setPasswordNgDate("");
			session.setAttribute("loginUserDto", loginUserDto);
			return "";
		}
	}

	/**
	 * ログインしてログインIDを取得
	 * 
	 * @param userId
	 * @return ログインID
	 */
	public String getLoginId(Integer userId) {
		// ログイン情報を取得
		LoginUser loginUser = loginMapper.getLoginDetailByUserId(userId, Constants.DB_FLG_FALSE);
		if (loginUser == null) {
			return "";
		}
		// セッションに格納
		BeanUtils.copyProperties(loginUser, loginUserDto);
		loginUserDto.setPasswordNgCount(0);
		loginUserDto.setPasswordNgDate("");
		session.setAttribute("loginUserDto", loginUserDto);
		// ログインIDを返す
		String loginId = mUserMapper.getLoginId(userId, Constants.DB_FLG_FALSE);
		return loginId;
	}

	/**
	 * アカウントロックチェック
	 * 
	 * @return boolean
	 */
	private boolean checkLockTime() {
		if (loginUserDto.getPasswordNgDate() == null
				|| loginUserDto.getPasswordNgDate().isEmpty()) {
			return true;
		}
		// 現在日時取得
		Calendar now = dateUtil.toCalendar(dateUtil.getCurrentDateString());
		now.add(Calendar.MINUTE, -lockMinute);
		// ロック日時がロック日時より規定時間たっているか確認
		if (now.compareTo(dateUtil.toCalendar(loginUserDto.getPasswordNgDate())) > 0) {
			loginUserDto.setPasswordNgDate("");
			loginUserDto.setPasswordNgCount(0);
			return true;
		}
		return false;
	}

	/**
	 * アカウントNG回数チェック
	 * 
	 * @return 判定結果
	 */
	private boolean checkLockCount() {
		if (loginUserDto.getPasswordNgCount() >= lockCount) {
			loginUserDto.setPasswordNgDate(dateUtil.getCurrentDateString());
			return false;
		}
		return true;
	}

	/**
	 * 会場非表示判定
	 * 
	 * @return 判定結果
	 */
	private boolean checkPlaceDisplay(String role, Short hiddenFlg) {
		// 講師の場合、担当会場が非表示になっていたらログイン不可とする
		if (role.equals(Constants.CODE_VAL_ROLL_TEACHER)) {
			if (hiddenFlg.equals(Constants.DB_HIDDEN_FLG_TRUE)) {
				return false;
			}
		}
		return true;
	}

}
