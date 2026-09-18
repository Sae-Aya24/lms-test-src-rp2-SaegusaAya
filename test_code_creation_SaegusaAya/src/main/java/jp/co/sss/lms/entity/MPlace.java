package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 会場マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MPlace {

	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 会場詳細 */
	private String placeDescription;
	/** サポートセンター表示 */
	private Short supportAvailable;
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
	/** 収容人数 */
	private Integer seatingCapacity;
	/** 備考 */
	private String placeNote;
	/** 非表示フラグ */
	private Short hiddenFlg;

}
