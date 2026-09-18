package jp.co.sss.lms.dto;

import java.util.Date;

import lombok.Data;

/**
 * Task.46 面談DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MeetingDto {

	/** 面談ID */
	private Integer meetingId;
	/** LMSユーザID */
	private Integer lmsUserId;
	/** 初回作成者 */
	private Integer firstCreateUser;
	/** 初回作成日時 */
	private Date firstCreateDate;
	/** 面談ファイルID */
	private Integer meetingFileId;
	/** 面談ファイル削除フラグ */
	private Short meetingFileDeleteFlg;

}
