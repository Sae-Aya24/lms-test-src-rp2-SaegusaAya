package jp.co.sss.lms.dto;

import java.util.List;

import lombok.Data;

/**
 * ユーザー詳細DTO
 * 
 * @author 東京ITスクール
 */
@Data
public class LmsUserDto {

	/** ユーザー基本情報DTO */
	private UserDetailDto userDetailDto;
	/** 試験結果DTOリスト */
	private List<ExamResultDto> examResultDtoList;
	/** 成果物詳細DTOリスト */
	private List<DeliverablesResultDto> deliverablesResultDtoList;
	/** レポートDTOリスト */
	private List<DailyReportDto> dailyReportDtoList;
	/** 面談DTOリスト */
	private List<MeetingDto> meetingDtoList; // Task.46

}
