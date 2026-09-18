package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * オペレータマスタエンティティクラス
 * 
 * @author 東京ITスクール
 */
@Data
public class MOperator {

	/** オペレータID */
	private Integer operatorId;
	/** オペレータ名 */
	private String operatorName;
	/** URL */
	private String url;
	/** オペレータタイプ */
	private Short operatorType;
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
