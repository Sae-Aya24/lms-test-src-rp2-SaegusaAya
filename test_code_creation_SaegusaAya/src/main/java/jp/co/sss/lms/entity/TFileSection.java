package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * ファイル・セクション紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TFileSection {

	/** ファイル・セクション紐付けID */
	private Integer fileSectionId;
	/** ファイルID */
	private Integer fileId;
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
