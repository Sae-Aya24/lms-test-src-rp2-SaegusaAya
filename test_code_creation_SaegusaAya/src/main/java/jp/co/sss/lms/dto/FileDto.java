package jp.co.sss.lms.dto;

import lombok.Data;

/**
 * ファイルDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class FileDto {

	/** ファイルID */
	private String fileId;
	/** ファイル名 */
	private String fileName;
	/** ファイルパス */
	private String filePath;
	/** ファイルサイズ */
	private String fileSize;

}
