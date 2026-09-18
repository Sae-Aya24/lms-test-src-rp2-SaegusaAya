
package jp.co.sss.lms.filter;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.service.ContractService;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.DateUtil;
import jp.co.sss.lms.util.LoggingUtil;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * ログインセッションフィルター
 * 
 * @author 東京ITスクール
 */
@Component
@Order(2)
public class LoginSessionFilter implements Filter {

	@Autowired
	private LoginUserUtil loginUserUtil;
	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private DateUtil dateUtil;
	@Autowired
	private HttpSession session;
	@Autowired
	private LoggingUtil loggingUtil;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private ContractService contractService;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	/**
	 * ログイン判定・利用規約同意判定・パスワード変更判定・最終パスワード変更判定・契約同意判定
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = ((HttpServletRequest) request);
		HttpServletResponse httpRes = (HttpServletResponse) response;
		String uri = httpReq.getRequestURI();
		if (isStatic(uri)) {
			chain.doFilter(request, response);
			return;
		}
		// ログインしていなくても遷移できるURIか判定
		if (!isCheckDisp(uri, httpReq.getContextPath())) {
			if (!loginUserUtil.isLogin()) {
				// ログインしていない場合はログイン画面に遷移
				String timeoutMessage = messageUtil.getMessage(Constants.PROP_KEY_SESSION_TIMEOUT);
				StringBuffer sb = new StringBuffer(timeoutMessage);
				loggingUtil.appendLog(sb);
				logger.info(sb.toString());
				session.setAttribute("sessionTimeout", timeoutMessage);
				httpRes.sendRedirect(httpReq.getContextPath());
				return;
			} else if (!isAccess(loginUserDto.getRole(), uri)) {
				// アクセス可能かチェック
				httpRes.sendError(HttpServletResponse.SC_NOT_FOUND);
				return;
			} else if ((loginUserDto.getSecurityAgreeFlg() == null
					|| loginUserDto.getSecurityAgreeFlg() != 1)) {
				// セキュリティ同意フラグが立っていなければ利用規約画面へ遷移
				httpRes.sendRedirect(httpReq.getContextPath() + "/user/agreeSecurity");
				return;
			} else if (loginUserDto.getPasswordChangeDate() == null
					|| !(isCurrentPasswordExpired())) {
				// 最終パスワード変更日時が1ヶ月を経過していればパスワード変更画面へ遷移
				httpRes.sendRedirect(httpReq.getContextPath() + "/password/changePassword");
				return;
			} else if (loginUserUtil.isCompany()
					&& !contractService.getDisagreementConsentDtoList().isEmpty()) {
				// 企業担当者であり、かつ未同意の契約同意DTOが存在すれば契約書確認画面へ遷移
				httpRes.sendRedirect(httpReq.getContextPath() + "/contract/agreement/regist");
				return;
			}
		}
		chain.doFilter(request, response);
	}

	/**
	 * 静的なURIか確認
	 * 
	 * @return boolean
	 */
	private boolean isStatic(String uri) {
		return uri.contains("/js/") || uri.contains("/css/") || uri.contains("/fonts/")
				|| uri.contains("/img/") || uri.contains("/pdf/") || uri.contains("/mailTemplate/");
	}

	/**
	 * URIが該当するか確認
	 * 
	 * @return boolean
	 */
	private boolean isCheckDisp(String uri, String contextPath) {
		return uri.equals(contextPath) || uri.equals(contextPath + "/")
				|| uri.startsWith(contextPath + "/login") || uri.startsWith(contextPath + "/logout")
				|| uri.startsWith(contextPath + "/password/resetPassword")
				|| uri.equals(contextPath + "/faq")
				|| uri.startsWith(contextPath + "/user/agreeSecurity")
				|| uri.startsWith(contextPath + "/password/changePassword")
				|| uri.startsWith(contextPath + "/password/resetPassword/complete")
				|| uri.startsWith(contextPath + "/contract/agreement/regist"); // Task.120
	}

	/**
	 * パスワード変更日が1ヶ月未満か確認
	 * 
	 * @return boolean
	 */
	public boolean isCurrentPasswordExpired() {
		// 現在日時取得
		Calendar now = dateUtil.toCalendar(dateUtil.getCurrentDateString());
		// 現在日時から1ヶ月マイナスする
		now.add(Calendar.MONTH, -1);
		// 最終パスワード変更日時が1ヶ月未満か確認
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
		Date t = loginUserDto.getPasswordChangeDate();
		String pCD = sdf.format(t);
		if (now.compareTo(dateUtil.toCalendar(pCD)) > 0) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void destroy() {
	}

	// 権限毎のアクセス制限チェック
	private boolean isAccess(String role, String url) {

		boolean flg = false;

		if (Constants.CODE_VAL_ROLL_STUDENT.equals(role) && studentAcessList.contains(url)) {
			flg = true;
		} else if (Constants.CODE_VAL_ROLL_TEACHER.equals(role) && teacherAcessList.contains(url)) {
			flg = true;
		} else if (Constants.CODE_VAL_ROLL_COMPANY.equals(role) && companyAcessList.contains(url)) {
			flg = true;
		} else if (Constants.CODE_VAL_ROLL_ADMIN.equals(role) && adminAcessList.contains(url)) {
			flg = true;
		} else if (Constants.CODE_VAL_ROLL_TRAINING.equals(role)
				&& trainingAcessList.contains(url)) {
			flg = true;
		}

		return flg;
	}

	// 受講生権限のアクセス可能URL
	private final static ArrayList<String> studentAcessList = new ArrayList<String>();
	static {
		studentAcessList.add("/lms/user/agreeSecurity");
		studentAcessList.add("/lms/error");
		studentAcessList.add("/lms/illegal");
		studentAcessList.add("/lms/course/detail");
		studentAcessList.add("/lms/section/detail");
		studentAcessList.add("/lms/exam/start");
		studentAcessList.add("/lms/exam/question");
		studentAcessList.add("/lms/exam/detail");
		studentAcessList.add("/lms/exam/result");
		studentAcessList.add("/lms/exam/resultDetail");
		studentAcessList.add("/lms/exam/answerCheck");
		studentAcessList.add("/lms/report/detail");
		studentAcessList.add("/lms/report/regist");
		studentAcessList.add("/lms/report/complete");
		studentAcessList.add("/lms/report/download");
		studentAcessList.add("/lms/report/feedback/regist");
		studentAcessList.add("/lms/report/feedback/delete");
		studentAcessList.add("/lms/report/feedback/update");
		studentAcessList.add("/lms/download/teachingMaterialList");
		studentAcessList.add("/lms/attendance/detail");
		studentAcessList.add("/lms/attendance/update");
		studentAcessList.add("/lms/fileshare/list");
		studentAcessList.add("/lms/movie");
		studentAcessList.add("/lms/support");
		studentAcessList.add("/lms/help");
		studentAcessList.add("/lms/faq");
		studentAcessList.add("/lms/info");
		studentAcessList.add("/lms/info/json");
		studentAcessList.add("/lms/user/detail");
		studentAcessList.add("/lms/password/changePassword");
	}

	// 講師権限のアクセス可能URL
	private final static ArrayList<String> teacherAcessList = new ArrayList<String>();
	static {
		teacherAcessList.add("/lms/user/agreeSecurity");
		teacherAcessList.add("/lms/error");
		teacherAcessList.add("/lms/illegal");
		teacherAcessList.add("/lms/course/list");
		teacherAcessList.add("/lms/course/detail");
		teacherAcessList.add("/lms/download/teachingMaterialList");
		teacherAcessList.add("/lms/section/detail");
		teacherAcessList.add("/lms/meeting/regist");
		teacherAcessList.add("/lms/meeting/delete");
		teacherAcessList.add("/lms/meeting/download");
		teacherAcessList.add("/lms/user/list");
		teacherAcessList.add("/lms/user/detail");
		teacherAcessList.add("/lms/password/reissuePassword");
		teacherAcessList.add("/lms/report/list");
		teacherAcessList.add("/lms/report/detail");
		teacherAcessList.add("/lms/report/downloadList");
		teacherAcessList.add("/lms/report/regist");
		teacherAcessList.add("/lms/report/feedback/delete");
		teacherAcessList.add("/lms/report/feedback/regist");
		teacherAcessList.add("/lms/report/feedback/update");
		teacherAcessList.add("/lms/exam/list");
		teacherAcessList.add("/lms/exam/detail");
		teacherAcessList.add("/lms/exam/preview");
		teacherAcessList.add("/lms/exam/result");
		teacherAcessList.add("/lms/exam/resultDetail");
		teacherAcessList.add("/lms/exam/resultList");
		teacherAcessList.add("/lms/exam/deleteResultList");
		teacherAcessList.add("/lms/attendance/list");
		teacherAcessList.add("/lms/attendance/detail");
		teacherAcessList.add("/lms/attendance/bulkRegist");
		teacherAcessList.add("/lms/attendance/update");
		teacherAcessList.add("/lms/attendance/updateAdmin");
		teacherAcessList.add("/lms/evReport/score");
		teacherAcessList.add("/lms/evReport/result/regist");
		teacherAcessList.add("/lms/takeOver/list");
		teacherAcessList.add("/lms/takeOver/detail");
		teacherAcessList.add("/lms/presentation/list");
		teacherAcessList.add("/lms/presentation/reserveStatusDetail");
		teacherAcessList.add("/lms/presentation/teamList");
		teacherAcessList.add("/lms/presentation/teamDetail");
		teacherAcessList.add("/lms/fileshare/list");
		teacherAcessList.add("/lms/movie");
		teacherAcessList.add("/lms/help");
		teacherAcessList.add("/lms/faq");
		teacherAcessList.add("/lms/info");
		teacherAcessList.add("/lms/info/json");
		teacherAcessList.add("/lms/user/myAccount");
		teacherAcessList.add("/lms/password/changePassword");
	}

	// 企業担当者権限のアクセス可能URL
	private final static ArrayList<String> companyAcessList = new ArrayList<String>();
	static {
		companyAcessList.add("/lms/user/agreeSecurity");
		companyAcessList.add("/lms/error");
		companyAcessList.add("/lms/illegal");
		companyAcessList.add("/lms/user/list/student");
		companyAcessList.add("/lms/student/update");
		companyAcessList.add("/lms/user/detail");
		companyAcessList.add("/lms/report/list");
		companyAcessList.add("/lms/report/detail");
		companyAcessList.add("/lms/report/feedback/delete");
		companyAcessList.add("/lms/report/feedback/regist");
		companyAcessList.add("/lms/report/feedback/update");
		companyAcessList.add("/lms/attendance/list");
		companyAcessList.add("/lms/attendance/detail");
		companyAcessList.add("/lms/exam/list");
		companyAcessList.add("/lms/exam/resultList");
		companyAcessList.add("/lms/exam/detail");
		companyAcessList.add("/lms/exam/resultDetail");
		companyAcessList.add("/lms/takeOver/list");
		companyAcessList.add("/lms/takeOver/detail");
		companyAcessList.add("/lms/takeOver/regist");
		companyAcessList.add("/lms/presentation/list");
		companyAcessList.add("/lms/presentation/reserveRegist");
		companyAcessList.add("/lms/presentation/reserveComplete");
		companyAcessList.add("/lms/presentation/reserveUpdate");
		companyAcessList.add("/lms/presentation/reserveStatusDetail");
		companyAcessList.add("/lms/presentation/teamDetail");
		companyAcessList.add("/lms/contract/history/list");
		companyAcessList.add("/lms/contract/history/detail");
		companyAcessList.add("/lms/contract/agreement/regist");
		companyAcessList.add("/lms/subsidy/company/update");
		companyAcessList.add("/lms/student/regist");
		companyAcessList.add("/lms/user/list/company");
		companyAcessList.add("/lms/fileshare/list");
		companyAcessList.add("/lms/help");
		companyAcessList.add("/lms/info");
		companyAcessList.add("/lms/info/json");
		companyAcessList.add("/lms/user/myAccount");
		companyAcessList.add("/lms/password/changePassword");
	}

	// 管理者権限のアクセス可能URL
	private final static ArrayList<String> adminAcessList = new ArrayList<String>();
	static {
		adminAcessList.add("/lms/course/list");
		adminAcessList.add("/lms/download/teachingMaterialList");
		adminAcessList.add("/lms/course/detail");
		adminAcessList.add("/lms/course/regist");
		adminAcessList.add("/lms/course/registDetail");
		adminAcessList.add("/lms/section/detail");
		adminAcessList.add("/lms/section/registDetail");
		adminAcessList.add("/lms/user/list");
		adminAcessList.add("/lms/user/detail");
		adminAcessList.add("/lms/user/update");
		adminAcessList.add("/lms/user/delete");
		adminAcessList.add("/lms/user/bulkDelete");
		adminAcessList.add("/lms/company/list");
		adminAcessList.add("/lms/company/regist");
		adminAcessList.add("/lms/place/list");
		adminAcessList.add("/lms/place/regist");
		adminAcessList.add("/lms/place/delete");
		adminAcessList.add("/lms/genre/list");
		adminAcessList.add("/lms/genre/regist");
		adminAcessList.add("/lms/genre/delete");
		adminAcessList.add("/lms/genre/detailRegist");
		adminAcessList.add("/lms/genre/detailDelete");
		adminAcessList.add("/lms/exam/list");
		adminAcessList.add("/lms/exam/resultList");
		adminAcessList.add("/lms/exam/regist");
		adminAcessList.add("/lms/exam/registDetail");
		adminAcessList.add("/lms/exam/delete");
		adminAcessList.add("/lms/report/list");
		adminAcessList.add("/lms/report/detail");
		adminAcessList.add("/lms/report/downloadList");
		adminAcessList.add("/lms/report/regist");
		adminAcessList.add("/lms/report/feedback/regist");
		adminAcessList.add("/lms/report/feedback/delete");
		adminAcessList.add("/lms/report/feedback/update");
		adminAcessList.add("/lms/evReport/score");
		adminAcessList.add("/lms/evReport/list");
		adminAcessList.add("/lms/evReport/regist");
		adminAcessList.add("/lms/meeting/fileList");
		adminAcessList.add("/lms/meeting/fileRegist");
		adminAcessList.add("/lms/holiday/list");
		adminAcessList.add("/lms/movie");
		adminAcessList.add("/lms/movie/categoryList");
		adminAcessList.add("/lms/movie/categoryRegist");
		adminAcessList.add("/lms/movie/categoryDelete");
		adminAcessList.add("/lms/movie/regist");
		adminAcessList.add("/lms/movie/updateMoveup");
		adminAcessList.add("/lms/movie/updateMovedown");
		adminAcessList.add("/lms/movie/delete");
		adminAcessList.add("/lms/mail/sendList");
		adminAcessList.add("/lms/mail/sendTemplateConfirm");
		adminAcessList.add("/lms/mail/queList");
		adminAcessList.add("/lms/mail/queDelete");
		adminAcessList.add("/lms/attendance/list");
		adminAcessList.add("/lms/attendance/detail");
		adminAcessList.add("/lms/attendance/bulkRegist");
		adminAcessList.add("/lms/meeting/regist");
		adminAcessList.add("/lms/meeting/delete");
		adminAcessList.add("/lms/meeting/download");
		adminAcessList.add("/lms/takeOver/list");
		adminAcessList.add("/lms/takeOver/detail");
		adminAcessList.add("/lms/takeOver/candidateList");
		adminAcessList.add("/lms/takeOver/registDate");
		adminAcessList.add("/lms/takeOver/selectCompany");
		adminAcessList.add("/lms/takeOver/selectDate");
		adminAcessList.add("/lms/fileshare/list");
		adminAcessList.add("/lms/faq/categoryList");
		adminAcessList.add("/lms/faq/categoryRegist");
		adminAcessList.add("/lms/faq/categoryDelete");
		adminAcessList.add("/lms/faq/regist");
		adminAcessList.add("/lms/faq/delete");
		adminAcessList.add("/lms/contract/history/list");
		adminAcessList.add("/lms/info/latest");
		adminAcessList.add("/lms/info/regist");
		adminAcessList.add("/lms/info/list");
		adminAcessList.add("/lms/repair/list");
		adminAcessList.add("/lms/repair/delete");
		adminAcessList.add("/lms/repair/regist");
		adminAcessList.add("/lms/repair/complete");
		adminAcessList.add("/lms/help");
		adminAcessList.add("/lms/info");
		adminAcessList.add("/lms/info/json");
		adminAcessList.add("/lms/password/changePassword");
	}

	// 育成担当者権限のアクセス可能URL
	private final static ArrayList<String> trainingAcessList = new ArrayList<String>();
	static {
		trainingAcessList.add("/lms/user/agreeSecurity");
		trainingAcessList.add("/lms/error");
		trainingAcessList.add("/lms/illegal");
		trainingAcessList.add("/lms/password/changePassword");
	}

}
