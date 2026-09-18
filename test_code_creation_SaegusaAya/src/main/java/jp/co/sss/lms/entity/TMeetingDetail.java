package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 面談詳細テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TMeetingDetail {

	/** 面談詳細ID */
	private Integer meetingDetailId;
	/** 面談ID */
	private Integer meetingId;
	/** 問題 */
	private String question;
	/** 回答 */
	private String answer;
	/** フォロー */
	private String follow;
	/** タイプ */
	private Short questionType;
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
