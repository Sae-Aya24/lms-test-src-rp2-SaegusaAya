package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * メール送信キューテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TMailQue {

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
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回作成日時 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日時 */
	private Date lastModifiedDate;

}
