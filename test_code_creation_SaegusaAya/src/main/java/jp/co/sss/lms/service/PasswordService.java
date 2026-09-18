package jp.co.sss.lms.service;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.LoginUser;
import jp.co.sss.lms.entity.MUser;
import jp.co.sss.lms.entity.TTemporaryPassStorage;
import jp.co.sss.lms.form.LoginForm;
import jp.co.sss.lms.form.MailAddressForm;
import jp.co.sss.lms.mapper.LoginMapper;
import jp.co.sss.lms.mapper.MUserMapper;
import jp.co.sss.lms.mapper.TTemporaryPassStorageMapper;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.DateUtil;
import jp.co.sss.lms.util.MailUtil;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.PasswordUtil;

/**
 * パスワード情報サービス
 * 
 * @author 東京ITスクール
 */
@Service
public class PasswordService {

	@Autowired
	private MUserMapper mUserMapper;
	@Autowired
	private HttpSession session;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private DateUtil dateUtil;
	@Autowired
	private LoginMapper loginMapper;
	@Autowired
	private TTemporaryPassStorageMapper tTemporaryPassStorageMapper;
	@Autowired
	private PasswordUtil passwordUtil;
	@Autowired
	private MailService mailService;
	@Autowired
	private MailUtil mailUtil;

	/**
	 * パスワード相関チェック（パスワード変更）
	 * 
	 * @param loginForm
	 */
	public void changePasswordInputCheck(LoginForm loginForm, BindingResult result) {
		// フォーム入力チェックでエラーがある場合はチェックを行わない
		if (result.hasErrors()) {
			return;
		}
		MUser mUser = mUserMapper.findByUserId(loginUserDto.getUserId(), Constants.DB_FLG_FALSE);
		// 現在のパスワードと登録パスワードが一致しない場合
		String hashedCurrentPassword = passwordUtil
				.getSaltedAndStrechedPassword(loginForm.getCurrentPassword(), mUser.getLoginId());
		if (!mUser.getPassword().equals(hashedCurrentPassword)) {
			String currentPassword = "「" + messageUtil.getMessage("currentPassword") + "」";
			String registPassword = messageUtil.getMessage("registPassword");
			result.addError(new FieldError(result.getObjectName(), "currentPassword", messageUtil
					.getMessage("match", new String[] { currentPassword, registPassword })));
		}
		// 新しいパスワードと現在のパスワードが一致する場合
		if (loginForm.getPassword().equals(loginForm.getCurrentPassword())) {
			result.addError(new FieldError(result.getObjectName(), "password",
					messageUtil.getMessage(Constants.VALID_KEY_USEDPASSWORD_NG)));
		}
		// 新しいパスワードと確認パスワードが一致しない場合
		if (!loginForm.getPassword().equals(loginForm.getPasswordConfirm())) {
			String password = messageUtil.getMessage("password");
			String passwordConfirm = messageUtil.getMessage("passwordConfirm");
			result.addError(new FieldError(result.getObjectName(), "password",
					messageUtil.getMessage("match", new String[] { password, passwordConfirm })));
		}
	}

	/**
	 * パスワード変更
	 * 
	 * @param loginForm
	 * @param result
	 * @return エラー情報
	 */
	public String changePassword(LoginForm loginForm) {
		// 更新
		MUser mUser = mUserMapper.findByUserId(loginUserDto.getUserId(), Constants.DB_FLG_FALSE);
		mUser.setPassword(passwordUtil.getSaltedAndStrechedPassword(loginForm.getPassword(),
				mUser.getLoginId()));
		mUser.setPasswordChangeDate(dateUtil.stringToTimestamp(dateUtil.getCurrentDateString()));
		mUser.setLastModifiedUser(loginUserDto.getUserId());
		Date now = new Date();
		mUser.setLastModifiedDate(now);
		boolean updateFlg = mUserMapper.updatePassword(mUser);
		if (updateFlg) {
			// 最新のログイン詳細が取得できればセッションに登録
			LoginUser loginUser = loginMapper.getLoginDetailByLmsUserId(loginUserDto.getLmsUserId(),
					Constants.DB_FLG_FALSE);
			if (loginUser != null) {
				BeanUtils.copyProperties(loginUser, loginUserDto);
				loginUserDto.setPasswordNgCount(0);
				loginUserDto.setPasswordNgDate("");
				session.setAttribute("loginUserDto", loginUserDto);
				// パスワード変更テーブルの情報があれば削除
				TTemporaryPassStorage tTemporaryPassStorage = tTemporaryPassStorageMapper
						.findByUserId(loginUserDto.getUserId(), Constants.DB_FLG_FALSE);
				if (tTemporaryPassStorage != null) {
					tTemporaryPassStorage.setDeleteFlg(Constants.DB_FLG_TRUE);
					tTemporaryPassStorage.setLastModifiedDate(now);
					tTemporaryPassStorage.setLastModifiedUser(loginUserDto.getLmsUserId());
					tTemporaryPassStorageMapper.deleteUpdate(tTemporaryPassStorage);
				}
			} else {
				// ログイン詳細が取得できなかった場合
				return messageUtil.getMessage(Constants.VALID_KEY_LOGINDATA_GET_FAILED);
			}
		} else {
			// 更新失敗
			return messageUtil.getMessage(Constants.VALID_KEY_PASSWORD_UPDATE_FAILED);
		}
		return null;
	}

	/**
	 * パスワード変更テーブル登録
	 * 
	 * @param mailAddressForm
	 */
	public void registTemporaryPassStorage(MailAddressForm mailAddressForm) {

		// Task.05 ②対象者特定の条件に権限を追加
		List<MUser> mUserList = mUserMapper.findByMailAddress(mailAddressForm.getMailAddress(),
				mailAddressForm.getRole(), Constants.DB_FLG_FALSE);
		// 入力されたメールアドレスが存在しない場合
		if (mUserList.size() == 0) {
			return;
		}

		// パスワード変更可能時間を設定
		Integer timeLimit = Integer.parseInt(messageUtil.getMessage("setting.timelimit.hour"));
		Date now = new Date();

		// Task.06 対象者が複数の場合の対応
		for (MUser mUser : mUserList) {
			// 変更キーの生成
			String key = passwordUtil.generatePassword() + passwordUtil.generatePassword();
			// パスワード変更の取得
			TTemporaryPassStorage tTemporaryPassStorage = tTemporaryPassStorageMapper
					.findByUserId(mUser.getUserId(), Constants.DB_FLG_FALSE);

			boolean hasTemporaryPassStorage = (tTemporaryPassStorage != null);
			if (!hasTemporaryPassStorage) {
				tTemporaryPassStorage = new TTemporaryPassStorage();
			}
			tTemporaryPassStorage.setChangeKey(key);
			tTemporaryPassStorage
					.setTimeLimit(new Timestamp(dateUtil.addHour(now, timeLimit).getTime()));
			tTemporaryPassStorage.setUserId(mUser.getUserId());
			tTemporaryPassStorage.setDeleteFlg(Constants.DB_FLG_FALSE);
			tTemporaryPassStorage.setLastModifiedDate(now);
			tTemporaryPassStorage.setLastModifiedUser(mUser.getUserId());

			// パスワード変更テーブルの登録／更新
			if (!hasTemporaryPassStorage) {
				tTemporaryPassStorage.setFirstCreateDate(now);
				tTemporaryPassStorage.setFirstCreateUser(mUser.getUserId());
				tTemporaryPassStorageMapper.insert(tTemporaryPassStorage);
			} else {
				tTemporaryPassStorageMapper.update(tTemporaryPassStorage);
			}
			// ユーザーマスタの最終パスワード変更日を削除（パスワード変更対象）
			mUser.setPasswordChangeDate(null);
			mUser.setLastModifiedDate(now);
			mUser.setLastModifiedUser(mUser.getUserId());
			mUserMapper.updatePasswordChangeDate(mUser);
		}

	}

	/**
	 * パスワード再設定メール送信
	 * 
	 * @param request
	 * @param mailAddress
	 */
	public void sendPasswordResetMail(HttpServletRequest request, MailAddressForm mailAddressForm) {

		// Task.05 ②対象者特定の条件に権限を追加
		List<MUser> mUserList = mUserMapper.findByMailAddress(mailAddressForm.getMailAddress(),
				mailAddressForm.getRole(), Constants.DB_FLG_FALSE);
		// 入力されたメールアドレスが存在しない場合
		if (mUserList.size() == 0) {
			return;
		}

		for (MUser mUser : mUserList) {
			TTemporaryPassStorage tTemporaryPassStorage = tTemporaryPassStorageMapper
					.findByUserId(mUser.getUserId(), Constants.DB_FLG_FALSE);
			// パスワード変更テーブルの情報がない場合
			if (tTemporaryPassStorage == null) {
				return;
			}
			// メール送信
			String nowUrl = request.getRequestURL().toString();
			String servletPath = request.getServletPath();
			String resetPasswordUrl = "/password/resetPassword/set";
			String contextRoot = nowUrl.replace(servletPath, "");
			String url = contextRoot + resetPasswordUrl + "?key="
					+ tTemporaryPassStorage.getChangeKey();
			String to = mailAddressForm.getMailAddress();
			String subject = messageUtil.getMessage(Constants.PROP_KEY_MAIL_RESETPASS_SUBJECT);
			String body = messageUtil.getMessage(Constants.PROP_KEY_MAIL_RESETPASS_BODY);
			body = body.replace(Constants.REPLACE_CHAR_RESET_PASSWORD_URL, url);
			// メール送信キューテーブルの登録
			mailService.registMailQue(to, subject, body, null, null);
			// メール送信
			mailUtil.sendMail(to, null, subject, body);
		}

	}

	/**
	 * アクセス妥当性確認
	 * 
	 * @param key
	 * @return 確認結果
	 */
	public Integer checkAccessValidity(String key) {
		// 制限時間内でのアクセスであるか確認
		TTemporaryPassStorage tTemporaryPassStorage = tTemporaryPassStorageMapper
				.findByChangeKey(key, Constants.DB_FLG_FALSE);
		// keyに該当するデータが無いまたは、制限時間を過ぎている場合にエラー画面に遷移
		Date now = new Date();
		if (tTemporaryPassStorage == null
				|| tTemporaryPassStorage.getTimeLimit().compareTo(now) < 0) {
			return null;
		}
		return tTemporaryPassStorage.getUserId();
	}

	/**
	 * パスワード相関チェック（パスワード再設定）
	 * 
	 * @param loginForm
	 */
	public void resetPasswordInputCheck(LoginForm loginForm, BindingResult result) {
		// フォーム入力チェックでエラーがある場合はチェックを行わない
		if (result.hasErrors()) {
			return;
		}
		// 新しいパスワードと確認パスワードが一致しない場合
		if (!loginForm.getPassword().equals(loginForm.getPasswordConfirm())) {
			String password = messageUtil.getMessage("password");
			String passwordConfirm = messageUtil.getMessage("passwordConfirm");
			result.addError(new FieldError(result.getObjectName(), "password",
					messageUtil.getMessage("match", new String[] { password, passwordConfirm })));
		}
	}

	/**
	 * Task.45 パスワード再発行
	 * 
	 * @param userId
	 * @return ログインID, 生成パスワードを含む配列
	 */
	public String[] reissuePassword(Integer userId) {

		Date today = new Date();
		MUser mUser = mUserMapper.findByUserId(userId, Constants.DB_FLG_FALSE);
		String randomPassword = passwordUtil.generatePassword();
		mUser.setPassword(passwordUtil.getSaltedAndStrechedPassword(randomPassword,
				String.valueOf(mUser.getLoginId())));
		mUser.setPasswordChangeDate(null);
		mUser.setLastModifiedUser(loginUserDto.getLmsUserId());
		mUser.setLastModifiedDate(today);
		mUserMapper.updatePassword(mUser);

		return new String[] { mUser.getLoginId(), randomPassword };
	}

}