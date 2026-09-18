package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 試験マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MExam {

	/** 試験ID */
	private Integer examId;
	/** 試験名 */
	private String examName;
	/** 概要 */
	private String examDescription;
	/** 制限時間 */
	private Short limitTime;
	/** ジャンルID */
	private Integer genreId;
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
	/** 非表示フラグ */
	private Short hiddenFlg;

}
