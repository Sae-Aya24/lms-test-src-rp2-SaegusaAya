package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * コースマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MCourse {

	/** コースID */
	private Integer courseId;
	/** コース名前 */
	private String courseName;
	/** 概要 */
	private String courseDescription;
	/** 開校日 */
	private Date openTime;
	/** 閉校日 */
	private Date closeTime;
	/** コースタイプ */
	private Short courseType;
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
	/** パスワード */
	private String password;
	/** 非表示フラグ */
	private Short hiddenFlg;

}
