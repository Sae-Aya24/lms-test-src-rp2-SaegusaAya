package jp.co.sss.lms.dto;

import java.util.Date;
import java.util.List;

import lombok.Data;

/**
 * セクションサービスDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class SectionServiceSectionDto {

	/** セクションID */
	private Integer sectionId;
	/** セクション名 */
	private String sectionName;
	/** 概要 */
	private String sectionDescription;
	/** コースID */
	private Integer courseId;
	/** カテゴリーID */
	private Integer categoryId; // Task.116
	/** ファイルサイズ */
	private String maxFileSize;
	/** 日付 */
	private Date date;
	/** セクションファイルDTOリスト */
	private List<SectionServiceFileDto> fileDtoList;
	/** セクションサービス試験DTOリスト */
	private List<SectionServiceExamDto> examDtoList;
	/** セクションサービスレポートDTOリスト */
	private List<SectionServiceDailyReportDto> reportDtoList;
	/** セクションサービス成果物セクションDTOリスト */
	private List<SectionServiceDeliverablesSectionDto> deliverablesDtoList;

}
