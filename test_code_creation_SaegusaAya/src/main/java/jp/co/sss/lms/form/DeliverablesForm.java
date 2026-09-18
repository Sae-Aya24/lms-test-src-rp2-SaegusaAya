package jp.co.sss.lms.form;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

/**
 * 成果物アップロードフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class DeliverablesForm {

	/** セクションID */
	private Integer sectionId;
	/** 成果物・セクションID */
	private Integer deliverablesSectionId;
	/** アップロードファイル */
	private MultipartFile uploadFile;

}
