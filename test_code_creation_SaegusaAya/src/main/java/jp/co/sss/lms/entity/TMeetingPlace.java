package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * Task.108 面談対象会場テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TMeetingPlace {

	/** 面談対象会場ID */
	private Integer meetingPlaceId;
	/** 面談スケジュールID */
	private Integer meetingScheduleId;
	/** 会場ID */
	private Integer placeId;
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
	/** 内部向けメモ */
	private String meetingPlaceNote;
	/** 公開フラグ */
	private Short publishedFlg;

}
