package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 改修履歴テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TRepairHistory {

	/** 改修履歴ID */
	private Integer historyId;
	/** 内容 */
	private String content;
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
