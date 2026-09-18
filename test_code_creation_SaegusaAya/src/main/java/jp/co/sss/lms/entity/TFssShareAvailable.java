package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 共有可能ユーザテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TFssShareAvailable {

	/** 共有可能ID */
	private Integer fssShareAvailableId;
	/** 共有ユーザID */
	private Integer fssUserId;
	/** 共有可能共有ユーザID */
	private Integer shareFssUserId;
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
