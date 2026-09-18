package jp.co.sss.lms.form;

import lombok.Data;

/**
 * セクションフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class SectionForm {

	/** コースID */
	private Integer courseId;
	/** セクションID */
	private Integer sectionId;

}
