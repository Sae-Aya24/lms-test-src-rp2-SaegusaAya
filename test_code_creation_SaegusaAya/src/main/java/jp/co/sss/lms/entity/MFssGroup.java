package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 共有グループマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MFssGroup {

	/** 共有グループID */
	private Integer fssGroupId;
	/** グループ名 */
	private String groupName;
	/** 説明 */
	private String description;
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
