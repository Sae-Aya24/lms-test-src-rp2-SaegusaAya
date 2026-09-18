package jp.co.sss.lms.form;

import java.util.Date;
import java.util.List;

import jp.co.sss.lms.dto.CompanyCourseDto;
import jp.co.sss.lms.dto.PlaceDto;
import lombok.Data;

/**
 * Task.110 引継面談／会場見学スケジュール企業選択フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class TakeOverSelectCompanyForm {

	/** 面談スケジュールID */
	private Integer meetingScheduleId;
	/** 用途 */
	private String purpose;
	/** 開始日 */
	private Date meetingOpenDate;
	/** 終了日 */
	private Date meetingCloseDate;
	/** 予約編集期限 */
	private Date editLimit;
	/** 会場DTOリスト */
	private List<PlaceDto> placeDtoList;
	/** 選択会場ID */
	private Integer selectedPlaceId;
	/** 企業コースDTOリスト */
	private List<CompanyCourseDto> companyCourseDtoList;
	/** 選択企業IDリスト */
	private List<Integer> selectedCompanyIdList;

}
