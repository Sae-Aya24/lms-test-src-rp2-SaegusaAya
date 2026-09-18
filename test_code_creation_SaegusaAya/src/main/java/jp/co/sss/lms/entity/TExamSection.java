package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 試験・セクション紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TExamSection {

	/** 試験・セクション紐付けID */
	private Integer examSectionId;
	/** 試験ID */
	private Integer examId;
	/** セクションID */
	private Integer sectionId;
	/** 公開日時 */
	private Date publicDate;
	/** 非公開日時 */
	private Date privateDate;
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
