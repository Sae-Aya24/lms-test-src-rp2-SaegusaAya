package jp.co.sss.lms.dto;

import java.util.HashMap;
import java.util.Map;

import lombok.Data;

/**
 * セクションサービスファイルダウンロードDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class SectionServiceFileDownloadDto {

	/** ファイルIDマップ */
	private Map<String, Integer> fileIdMap = new HashMap<>();

}
