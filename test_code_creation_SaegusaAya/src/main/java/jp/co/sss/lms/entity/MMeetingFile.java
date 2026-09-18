package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 面談ファイルマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MMeetingFile {

	/** 面談ファイルID */
	private Integer meetingFileId;
	/** ファイル名 */
	private String fileName;
	/** シート名 */
	private String sheetName;
	/** 社名出力行番号 */
	private Integer rowCompany;
	/** 社名出力列番号 */
	private Integer clmCompany;
	/** ユーザー名出力行番号 */
	private Integer rowUser;
	/** ユーザー名出力列番号 */
	private Integer clmUser;
	/** 日付出力行番号 */
	private Integer rowDate;
	/** 日付出力列番号 */
	private Integer clmDate;
	/** 面談内容出力行番号 */
	private Integer rowMeeting;
	/** 面談内容出力列番号 */
	private Integer clmMeeting;
	/** 企業アカウントID */
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
