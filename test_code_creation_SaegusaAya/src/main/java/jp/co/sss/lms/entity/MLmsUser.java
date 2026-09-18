package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * LMSユーザーマスタエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class MLmsUser {

	/** LMSユーザーID */
	private Integer lmsUserId;
	/** ユーザーID */
	private Integer userId;
	/** ロール */
	private String role;
	/** 管理者権限 */
	private Short adminFlg;
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
	/** 研修を通じてどのようになって欲しいか */
	private String hopeViaTraning;
	/** プログラミング経験 */
	private Short programmingExperience;

}
