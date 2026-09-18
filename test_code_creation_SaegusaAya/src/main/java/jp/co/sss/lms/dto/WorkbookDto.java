package jp.co.sss.lms.dto;

import org.apache.poi.ss.usermodel.Workbook;

import lombok.Data;

/**
 * ワークシートDTO
 * 
 * @author 東京ITスクール
 */
@Data
public class WorkbookDto {

	/** ワークブック */
	private Workbook wb;
	/** ワークブック名 */
	private String wbName;

}
