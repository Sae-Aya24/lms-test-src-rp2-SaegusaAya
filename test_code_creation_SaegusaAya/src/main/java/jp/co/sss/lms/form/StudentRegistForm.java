package jp.co.sss.lms.form;

import java.util.List;

import jp.co.sss.lms.dto.StudentDto;
import jp.co.sss.lms.dto.SubsidyCategoryDto;
import lombok.Data;

/**
 * Task.67 受講生登録フォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class StudentRegistForm {

	/** 企業ID */
	private Integer companyId;
	/** 企業・コース紐付けID */
	private Integer companyCourseId;
	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** 助成金カテゴリDTOリスト */
	private List<SubsidyCategoryDto> subsidyCategoryDtoList;
	/** 受講生DTOリスト */
	private List<StudentDto> studentDtoList;

}
