package jp.co.sss.lms.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * ファイルダウンロードDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class FileDownloadDto {

	/** ファイルマップ */
	private Map<String, Integer> fileIdMap = new HashMap<>();

}
