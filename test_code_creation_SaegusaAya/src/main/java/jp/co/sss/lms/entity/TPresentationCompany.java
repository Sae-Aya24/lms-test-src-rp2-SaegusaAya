package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 成果報告会対象企業エンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TPresentationCompany {

	/** 成果報告会対象企業ID */
	private Integer presentationCompanyId;
	/** 企業ID */
	private Integer companyId;
	/** 成果報告会チームID */
	private Integer presentationTeamId;
	/** 参加可能フラグ */
	private Short joinAbleFlg;
	/** 参加人数 */
	private Integer joinAmount;
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
	/** 参加者名 */
	private String joinName;

}
