package jp.co.sss.lms.form;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import jp.co.sss.lms.dto.DailyReportDto;
import jp.co.sss.lms.dto.EvReportDto;
import jp.co.sss.lms.dto.MeetingFileDto;
import lombok.Data;

/**
 * Task.114 コース登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class CourseRegistForm {

	/** コースID */
	private Integer courseId;
	/** コース名 */
	@NotBlank
	@Size(max = 100)
	private String courseName;
	/** 概要 */
	@Size(max = 100)
	private String courseDescription;
	/** 開始日 */
	@NotBlank
	private String openTime;
	/** 終了日 */
	@NotBlank
	private String closeTime;
	/** レポートDTOリスト */
	private List<DailyReportDto> dailyReportDtoList;
	/** 日報ID */
	private Integer dailyReportId;
	/** 評価レポートリスト */
	private List<EvReportDto> evReportDtoList;
	/** 評価レポートID配列 */
	private Integer[] evReportIdArray;
	/** 面談ファイルDTOリスト */
	private List<MeetingFileDto> meetingFileDtoList;
	/** 面談ファイルID */
	private Integer meetingFileId;

}
