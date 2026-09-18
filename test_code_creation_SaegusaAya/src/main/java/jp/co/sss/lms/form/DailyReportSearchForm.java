package jp.co.sss.lms.form;

import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

import jp.co.sss.lms.dto.CompanyDto;
import jp.co.sss.lms.dto.CourseDto;
import jp.co.sss.lms.dto.PlaceDto;
import lombok.Data;

/**
 * Task.50 レポート検索フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class DailyReportSearchForm {

	/** 検索日付from */
	@NotBlank
	@Pattern(regexp = "^\\d{4}\\-\\d{2}\\-\\d{2}$", message = "{pattern.invalid}")
	private String dateFrom;
	/** 検索日付to */
	@NotBlank
	@Pattern(regexp = "^\\d{4}\\-\\d{2}\\-\\d{2}$", message = "{pattern.invalid}")
	private String dateTo;
	/** コースDTOリスト */
	private List<CourseDto> courseDtoList;
	/** コース名 */
	private String courseName;
	/** 会場DTOリスト */
	List<PlaceDto> placeDtoList; // Task.80
	/** 会場ID */
	private Integer placeId;
	/** 会場名 */
	private String placeName;
	/** 会場備考 */
	private String placeNote;
	/** 企業DTOリスト */
	private List<CompanyDto> companyDtoList;
	/** 企業名 */
	private String companyName;
	/** 経過時間ラベル */
	private String pastTimeLabel;
	/** 経過フラグ */
	private Short pastFlg;

}
