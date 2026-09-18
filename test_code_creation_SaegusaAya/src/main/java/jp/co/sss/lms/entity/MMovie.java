package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 動画マスタエンティティクラス
 * 
 * @author 東京ITスクール
 */
@Data
public class MMovie {

	/** 動画ID */
	private Integer movieId;
	/** 動画名 */
	private String movieName;
	/** URL */
	private String url;
	/** ソート順 */
	private Integer sortNumber;
	/** 動画カテゴリID */
	private Integer movieCategoryId;
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
