package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * コース・面談ファイル付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TCourseMeeting {

	/** ユーザー・会場紐付けID */
	private Integer courseMeetingId;
	/** コースID */
	private Integer courseId;
	/** 面談ファイルID */
	private Integer meetingFileId;
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
