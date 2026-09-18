package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * ファイル共有DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class FileShareDto {

	/** ファイル共有ID */
	private String fileId;
	/** ファイルパス */
	private String filePath;
	/** ファイル名 */
	private String fileName;
	/** 変更日時 */
	private String modifiedDate;
	/** ファイルタイプ */
	private String fileType;
	/** ファイルサイズ */
	private String fileSize;
	/** 所有ユーザID */
	private Integer ownerFssUserId;
	/** 所有ニックネーム */
	private String ownerNickname;
	/** 所有ユーザ名 */
	private String ownerUserName;
	/** 共有先ユーザID */
	private Integer sharedFssUserId;
	/** 共有先ニックネーム */
	private String sharedNickname;
	/** 共有先ユーザ名 */
	private String sharedUserName;
	/** 所有ユーザー */
	private String owner;
	/** 共有元ユーザー(所有者) */
	private String sharePerson;
	/** 共有ユーザー */
	private String sharedPerson;

}