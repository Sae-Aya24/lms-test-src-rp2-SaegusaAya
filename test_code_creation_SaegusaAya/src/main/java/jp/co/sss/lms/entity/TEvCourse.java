package jp.co.sss.lms.entity;

import java.util.Date;

import lombok.Data;

/**
 * 評価レポート・コース紐付けテーブルエンティティ
 * 
 * @author 東京ITスクール
 */
@Data
public class TEvCourse {

	/** 評価レポート・コース紐付けID */
	private Integer evCoursetId;
	/** 評価レポートID */
	private Integer evReportId;
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
