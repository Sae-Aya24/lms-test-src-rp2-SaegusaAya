package jp.co.sss.lms.form;

import java.util.List;

import jp.co.sss.lms.dto.MeetingDetailDto;
import jp.co.sss.lms.dto.UserDetailDto;
import lombok.Data;

/**
 * Task.47 面談記録登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class MeetingRegistForm {

	/** 面談ID */
	private Integer meetingId;
	/** LMSユーザID */
	private Integer lmsUserId;
	/** 企業アカウントID */
	private Integer accountId;
	/** ユーザー基本情報DTO */
	private UserDetailDto userDetailDto;
	/** 面談詳細DTOリスト */
	private List<MeetingDetailDto> meetingDetailDtoList;

}
