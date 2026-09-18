package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * コース・ユーザー紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TCourseUser {

	/** コース・ユーザー紐付けID */
	private Integer courseUserId;
	/** コースID */
	private Integer courseId;
	/** LMSユーザID */
	private Integer lmsUserId;
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
