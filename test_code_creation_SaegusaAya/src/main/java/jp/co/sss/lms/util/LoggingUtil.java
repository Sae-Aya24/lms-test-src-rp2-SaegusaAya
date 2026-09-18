package jp.co.sss.lms.util;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import jp.co.sss.lms.dto.LoginUserDto;

/**
 * ロギングユーティリティ
 * 
 * @author 東京ITスクール
 */
@Component
public class LoggingUtil {

	@Autowired
	LoginUserDto loginUserDto;
	@Autowired
	HttpServletRequest request;

	/**
	 * 権限マップ
	 */
	private final Map<String, String> ROLE_MAP = new HashMap<>();
	{
		ROLE_MAP.put(Constants.CODE_VAL_ROLL_STUDENT, "受講生");
		ROLE_MAP.put(Constants.CODE_VAL_ROLL_TEACHER, "講師");
		ROLE_MAP.put(Constants.CODE_VAL_ROLL_COMPANY, "企業担当者");
		ROLE_MAP.put(Constants.CODE_VAL_ROLL_ADMIN, "管理者");
		ROLE_MAP.put(Constants.CODE_VAL_ROLL_TRAINING, "育成担当者");
	}

	/**
	 * ログの追加
	 * 
	 * @param sb
	 */
	public void appendLog(StringBuffer sb) {

		sb.append(request.getRequestURI());

		// ヘッダ情報
		for (Enumeration<?> headerNames = request.getHeaderNames(); headerNames.hasMoreElements();) {
			String key = (String) headerNames.nextElement();
			sb.append("\n[header]");
			sb.append(key);
			sb.append("=");
			sb.append(request.getHeader(key));
		}

		// 入力情報
		Enumeration<?> paramNames = request.getParameterNames();
		if (paramNames != null) {
			while (paramNames.hasMoreElements()) {
				String name = (String) paramNames.nextElement();
				sb.append("\n[param]");
				sb.append(name);
				sb.append("=");
				if (name.equals("password") || name.equals("passwordConfirm")) {
					// パスワードは表示させない
					sb.append("*****************");
				} else {
					sb.append(request.getParameter(name));
				}
			}
		}

		if (loginUserDto == null || loginUserDto.getLmsUserId() == null) {
			sb.append("\n[loginUserInfo]");
			sb.append("loginUserInfo=");
			sb.append(loginUserDto);

		} else {
			sb.append("\n[loginUserInfo]");
			sb.append("loginUserInfo=");
			sb.append(loginUserDto);
			sb.append("\n[loginUserInfo]");
			sb.append("lmsUserId=");
			sb.append(loginUserDto.getLmsUserId());
			sb.append("\n[loginUserInfo]");
			sb.append("userName=");
			sb.append(loginUserDto.getUserName());
			sb.append("\n[loginUserInfo]");
			sb.append("role=");
			sb.append(ROLE_MAP.get(loginUserDto.getRole()));
		}

	}
}
