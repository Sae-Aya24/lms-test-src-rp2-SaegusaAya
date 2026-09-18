package jp.co.sss.lms.form;

import lombok.Data;

/**
 * 教材ダウンロードフォーム
 * 
 * @author 東京ITスクール
 */
@Data
public class TeachingMaterialsDownloadForm {

	/** コースID */
	private String courseId;
	/** ファイル名 */
	private String fileName;
	/** ファイルパス */
	private String filePath;

}
