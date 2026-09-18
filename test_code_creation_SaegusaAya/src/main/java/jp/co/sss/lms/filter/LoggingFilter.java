package jp.co.sss.lms.filter;

import java.io.IOException;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import jp.co.sss.lms.util.LoggingUtil;

/**
 * ロギングフィルター
 * 
 * @author 東京ITスクール
 */
@Component
@Order(1)
public class LoggingFilter implements Filter {
	
	@Autowired
	private LoggingUtil loggingUtil;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest httpReq = ((HttpServletRequest) request);
		String uri = httpReq.getRequestURI();
		if (isStatic(uri)) {
			chain.doFilter(request, response);
			return;
		}
		StringBuffer sb = new StringBuffer("\n[before]");
		loggingUtil.appendLog(sb);
		logger.info(sb.toString());

		chain.doFilter(request, response);

		sb = new StringBuffer("\n[after]");
		loggingUtil.appendLog(sb);
		logger.info(sb.toString());
	}

	private boolean isStatic(String uri) {
		return uri.contains("/js/") || uri.contains("/css/") || uri.contains("/fonts/") || uri.contains("/img/");
	}

	@Override
	public void destroy() {
	}

}
