package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 動画カテゴリマスタエンティティクラス
 * 
 * @author 東京ITスクール
 */
@Data
public class MMovieCategory {

	/** 動画カテゴリID */
	private Integer movieCategoryId;
	/** 動画カテゴリ名 */
	private String movieCategoryName;
	/** アカウントID */
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
