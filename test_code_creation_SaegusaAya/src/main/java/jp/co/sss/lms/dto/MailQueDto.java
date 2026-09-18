package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.94 メール送信キューDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MailQueDto {

	/** メール送信キューID */
	private Integer mailQueId;
	/** メールアドレスTo */
	private String mailAddressTo;
	/** メールアドレスCC */
	private String mailAddressCc;
	/** メールアドレスBCC */
	private String mailAddressBcc;
	/** 件名 */
	private String subject;
	/** 本文 */
	private String body;
	/** 初回作成日時 */
	private Date firstCreateDate;

}
