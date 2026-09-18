package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * ファイル・課題紐づけエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TFileTicket {

	/** ファイル・課題紐づけID */
	private Integer fileTicketId;
	/** 課題ID */
	private Integer ticketId;
	/** アカウントID */
	private Integer accountId;
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