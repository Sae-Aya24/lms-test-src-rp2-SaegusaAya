package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 共有ファイルテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TFssFile {

	/** 共有ファイルID */
	private Integer fssFileId;
	/** 所有共有ユーザID */
	private Integer ownerFssUserId;
	/** 共有先共有ユーザID */
	private Integer sharedFssUserId;
	/** ファイルパス */
	private String filePath;
	/** ファイルサイズ */
	private Integer fileSize;
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
