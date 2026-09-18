package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * お知らせテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TInfo {

	/** お知らせID */
	private Integer infoId;
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
