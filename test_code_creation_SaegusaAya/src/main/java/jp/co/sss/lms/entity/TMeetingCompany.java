package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 面談対象企業テーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TMeetingCompany {

	/** 面談対象企業ID */
	private Integer meetingCompanyId;
	/** 面談対象会場ID */
	private Integer meetingPlaceId;
	/** 企業ID */
	private Integer companyId;
	/** 面談スケジュール詳細ID */
	private Integer meetingScheduleDetailId;
	/** 参加人数 */
	private Integer joinAmount;
	/** お客様ご要望欄 */
	private String companyRequest;
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
