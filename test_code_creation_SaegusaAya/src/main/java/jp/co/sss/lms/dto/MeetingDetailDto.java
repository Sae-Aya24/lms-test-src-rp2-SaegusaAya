package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * 面談詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class MeetingDetailDto {

	/** 面談詳細ID */
	private Integer meetingDetailId;
	/** 面談ID */
	private Integer meetingId;
	/** 問題 */
	private String question;
	/** 回答 */
	private String answer;
	/** フォロー */
	private String follow;
	/** タイプ */
	private Short questionType;

}
