package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * Task.86 休日マスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MHoliday {

	/** 休日ID */
	private Integer holidayId;
	/** 休日名 */
	private String holidayName;
	/** 日付 */
	private Date holidayDate;
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
