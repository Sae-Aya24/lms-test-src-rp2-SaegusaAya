package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * コース・教材紐付けエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TCourseTeachingMaterial {

	/** コース・教材紐付けID */
	private Integer courseTeachingMaterialId;
	/** ファイル格納場所 */
	private String filePath;
	/** コースID */
	private Integer courseId;
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