package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * レポートフィードバックDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyReportFbDto {

	/** 日報フィードバックコメントID */
	private Integer dailyReportFbId;
	/** LMSユーザーID */
	private Integer lmsUserId;
	/** ユーザー名 */
	private String userName;
	/** 内容 */
	private String content;
	/** 初回登録日時 */
	private Date firstCreateDate;
	/** 投稿日時 */
	private Date date;

}
