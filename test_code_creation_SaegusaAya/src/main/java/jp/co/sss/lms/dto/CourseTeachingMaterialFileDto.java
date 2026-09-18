package jp.co.sss.lms.dto;

import org.springframework.util.MultiValueMap;

import lombok.Data;

/**
 * コース教材紐づけDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class CourseTeachingMaterialFileDto {

	/** コース教材紐づけデータID */
	private Integer courseTeachingMaterialId;
	/** コースID */
	private Integer courseId;
	/** コース名 */
	private String courseName;
	/** カレントファイル */
	private String currentFile;
	/** ディレクトリ名 */
	private String directoryName;
	/** ファイルマップ */
	private MultiValueMap<String, String> fileMap;

}