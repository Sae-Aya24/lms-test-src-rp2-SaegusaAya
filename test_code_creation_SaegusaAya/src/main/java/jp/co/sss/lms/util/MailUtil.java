package jp.co.sss.lms.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.stereotype.Component;

/**
 * メールユーティリティ
 * 
 * @author 東京ITスクール
 */
@Component
public class MailUtil {
	
	@Autowired
	private MailSender mailSender;
	@Autowired
	private MessageUtil messageUtil;
	
	/**
	 * メール送信
	 * 
	 * @param to
	 * @param cc
	 * @param subject
	 * @param text
	 */
	public void sendMail(String to, String[] cc, String subject, String text) {
		SimpleMailMessage msg = new SimpleMailMessage();
		msg.setFrom(messageUtil.getMessage("setting.mail.sender.address"));
		msg.setTo(to);
		msg.setCc(cc);
		msg.setSubject(subject);
		msg.setText(text);
		
		this.mailSender.send(msg);
	}

}
