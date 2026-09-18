package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * ジャンル詳細マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MGenreDetail {

	/** ジャンル詳細ID */
	private Integer genreDetailId;
	/** ジャンルID */
	private Integer genreId;
	/** ジャンル詳細 */
	private String genreDetailName;
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
