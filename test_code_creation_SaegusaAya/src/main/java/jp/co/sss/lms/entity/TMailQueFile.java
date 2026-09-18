package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * メール送信キュー・ファイル紐づけテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TMailQueFile {

	/** メール送信キュー・ファイル紐づけID */
	private Integer mailQueFileId;
	/** メール送信キューID */
	private Integer mailQueId;
	/** ファイルID */
	private Integer fileId;
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
