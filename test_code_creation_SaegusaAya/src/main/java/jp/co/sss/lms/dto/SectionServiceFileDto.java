package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * セクションファイルDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class SectionServiceFileDto {

	/** ファイルID */
	private String fileId;
	/** ファイル名 */
	private String fileName;
	/** ファイルパス */
	private String filePath;

}
