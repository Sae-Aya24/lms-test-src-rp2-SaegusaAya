package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 企業マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MCompany {

	/** 企業ID */
	private Integer companyId;
	/** 企業名 */
	private String companyName;
	/** 企業名（カナ） */
	private String companyNameKana;
	/** 郵便番号1 */
	private String postNumber1;
	/** 郵便番号2 */
	private String postNumber2;
	/** 都道府県 */
	private String prefecture;
	/** 所在地 */
	private String address;
	/** 電話番号1 */
	private String phoneNumber1;
	/** 電話番号2 */
	private String phoneNumber2;
	/** 電話番号3 */
	private String phoneNumber3;
	/** 代表者役職名 */
	private String representativePost;
	/** 代表者氏名 */
	private String representativeName;
	/** 資本額 */
	private Integer capital;
	/** 労働者数 */
	private Integer workerAmount;
	/** 申請担当者電話番号1 */
	private String subsidyPhoneNumber1;
	/** 申請担当者電話番号2 */
	private String subsidyPhoneNumber2;
	/** 申請担当者電話番号3 */
	private String subsidyPhoneNumber3;
	/** 休日 */
	private String holiday;
	/** 始業時間 */
	private String workStartTime;
	/** 終業時間 */
	private String workEndTime;
	/** 休憩開始時間 */
	private String restStartTime;
	/** 休憩終了時間 */
	private String restEndTime;
	/** アカウントID */
	private Integer accountId;
	/** 削除フラグ */
	private Short deleteFlg;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回更新日時 */
	private Date firstCreateDate;
	/** 最終更新者 */
	private Integer lastModifiedUser;
	/** 最終更新日時 */
	private Date lastModifiedDate;
	/** ファイル共有フラグ */
	private Short fileShareFlg;

}
