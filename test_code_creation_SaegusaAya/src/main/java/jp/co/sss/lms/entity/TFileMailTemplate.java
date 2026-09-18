package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * ファイル・メールテンプレート紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TFileMailTemplate {

	/** ファイル・メールテンプレート紐付け紐付けID */
	private Integer fileMailTemplateId;
	/** ファイルID */
	private Integer fileId;
	/** メールテンプレートID */
	private Integer mailTemplateId;
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