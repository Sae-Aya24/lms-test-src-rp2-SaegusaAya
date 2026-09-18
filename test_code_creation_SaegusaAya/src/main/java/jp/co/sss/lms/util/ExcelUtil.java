package jp.co.sss.lms.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import jp.co.sss.lms.dto.WorkbookDto;

import org.apache.poi.hssf.util.CellReference;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FormulaEvaluator;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

/**
 * ExcelUtil<br>
 * ファイル関係のユーティリティクラス<br>
 * 
 * @author 東京ITスクール
 */
public class ExcelUtil {

	@Autowired
	private static MessageUtil messageUtil;
	/* 縦軸最大桁数 */
	public static final int MAX_COL_NUM = 16384;
	/* エクセル縦軸文字を数値に変換 */
	public static final String MAX_COL_STR = CellReference.convertNumToColString(ExcelUtil.MAX_COL_NUM - 1);
	/* ワークブック初期化 */
	private Workbook wb = null;

	/**
	 * ファイルパスから作成済みのファイルを開く
	 * 
	 * @param filePath
	 */
	public ExcelUtil(String filePath) {
		try (InputStream in = new ClassPathResource(filePath).getInputStream()) {
			wb = WorkbookFactory.create(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 作成済みのファイルを開く
	 * 
	 * @param request
	 */
	public ExcelUtil(HttpServletRequest request) {
		try (InputStream in = request.getInputStream()) {
			wb = WorkbookFactory.create(in);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Excelシート名、行列からセルを取得<br>
	 * 入力文字(String)がある場合はセルに設定
	 * 
	 * @param sheetName
	 * @param rowNum
	 * @param clmNum
	 * @param String型value
	 */
	public void setVal(String sheetName, int rowNum, int clmNum, String value) {
		Cell cell = getCell(sheetName, rowNum, clmNum);
		if (value != null) {
			cell.setCellValue(value);
		}
	}

	/**
	 * Excelシート名、行列からセルを取得<br>
	 * 入力文字(Integer)がある場合はセルに設定
	 * 
	 * @param sheetName
	 * @param rowNum
	 * @param clmNum
	 * @param Integer型value
	 */
	public void setVal(String sheetName, int rowNum, int clmNum, Integer value) {
		Cell cell = getCell(sheetName, rowNum, clmNum);
		if (value != null) {
			cell.setCellValue(value);
		}
	}

	/**
	 * Excelシート名、行列からセルを取得<br>
	 * 入力文字(double)をセルに設定
	 * 
	 * @param sheetName
	 * @param rowNum
	 * @param clmNum
	 * @param double型value
	 */
	public void setVal(String sheetName, int rowNum, int clmNum, double value) {
		Cell cell = getCell(sheetName, rowNum, clmNum);
		cell.setCellValue(value);
	}

	/**
	 * Excelシート名、行列からセルを取得<br>
	 * 入力文字(Date)をセルに設定
	 * 
	 * @param sheetName
	 * @param rowNum
	 * @param clmNum
	 * @param Date型value
	 */
	public void setVal(String sheetName, int rowNum, int clmNum, Date value) {
		Cell cell = getCell(sheetName, rowNum, clmNum);
		cell.setCellValue(value);
	}

	/**
	 * セルを取得<br>
	 * 
	 * @param sheetName
	 * @param rowNum
	 * @param clmNum
	 */
	private Cell getCell(String sheetName, int rowNum, int clmNum) {
		Sheet sheet = wb.getSheet(sheetName);
		Row row = sheet.getRow(rowNum);
		if (row == null) {
			row = sheet.createRow(rowNum);
		}
		Cell cell = row.getCell(clmNum);
		if (cell == null) {
			cell = row.createCell(clmNum);
		}
		return cell;
	}

	/**
	 * ワークブックを取得<br>
	 * 
	 * @return ワークブック
	 */
	public Workbook getWb() {
		return wb;
	}

	/**
	 * 印刷範囲を設定する
	 * 
	 * @param sheetIdx
	 * @param startClm
	 * @param endClm
	 * @param startRow
	 * @param endRow
	 */
	public void setPrintArea(int sheetIdx, int startClm, int endClm, int startRow, int endRow) {
		wb.setPrintArea(sheetIdx, startClm, endClm, startRow, endRow);
	}

	/**
	 * 指定された位置のシートをコピーする
	 * 
	 * @param sheetName シート名
	 * @param st        sheetworksheet
	 * @param en        コピーの開始行(start from 0)
	 * @param cnt       コピーの最終行(end from 1)
	 * 
	 */
	public void sheetCopy(String sheetName, int st, int en, int cnt) {
		// シート情報を取得
		Sheet sheet = wb.getSheet(sheetName);
		int x = sheet.getNumMergedRegions();
		Row row = null, row2 = null;
		Cell cell = null, cell2 = null;
		CellStyle cellstyle = null;
		short height = 0;
		for (int i = st; i < en; i++) {
			row = sheet.getRow(i);
			if (row != null) {
				height = row.getHeight();
				// 最終行をベースに下(Row)へコピーしていく。
				row2 = sheet.createRow((en - st) * cnt + i);
				row2.setHeight(height);
				for (int j = 0; j < row.getLastCellNum(); j++) {
					cell = row.getCell(j);
					if (cell != null) {
						cell2 = row2.createCell(j);
						cellstyle = cell.getCellStyle();
						cell2.setCellStyle(cellstyle);
						switch (cell.getCellType()) {
						case Cell.CELL_TYPE_STRING:
							cell2.setCellValue(cell.getRichStringCellValue());
							break;
						case Cell.CELL_TYPE_NUMERIC:
							if (org.apache.poi.ss.usermodel.DateUtil.isCellDateFormatted(cell)) {
								cell2.setCellValue(cell.getDateCellValue());
							} else {
								cell2.setCellValue(cell.getNumericCellValue());
							}
							break;
						case Cell.CELL_TYPE_FORMULA:
							cell2.setCellFormula(cell.getCellFormula());
							break;
						case Cell.CELL_TYPE_BOOLEAN:
							cell2.setCellValue(cell.getBooleanCellValue());
							break;
						}
					}
				}
			}
		}
		// 結合状態を設定
		CellRangeAddress cra = null;
		for (int i = 0; i < x; i++) {
			cra = sheet.getMergedRegion(i);
			if (cra.getFirstRow() >= st) {
				CellRangeAddress newCra = new CellRangeAddress(cra.getFirstRow() + (en - st) * cnt,
						cra.getLastRow() + (en - st) * cnt, cra.getFirstColumn(), cra.getLastColumn());
				sheet.addMergedRegion(newCra);
			}
		}
	}

	/**
	 * Workbookのダウンロードを行う
	 * 
	 * @param workbookDto
	 * @param response
	 * @throws IOException
	 */
	public static void downloadBook(WorkbookDto workbookDto, HttpServletResponse response) throws IOException {

		String fileNameSjis = new String(workbookDto.getWbName().getBytes("Shift_JIS"), "ISO-8859-1").replace(" ",
				"%20");
		String fileNameUtf8 = URLEncoder.encode(workbookDto.getWbName(), "UTF-8").replace("+", "%20");
		response.setContentType("application/octet-stream");
		response.setHeader("Content-Disposition",
				"attachment;filename=" + fileNameSjis + ";filename*=utf-8''" + fileNameUtf8);

		try (ServletOutputStream out = response.getOutputStream()) {
			workbookDto.getWb().write(out);
		}
	}

	/**
	 * Workbookの一括ダウンロードを行う
	 * 
	 * @param workbookDto
	 * @param response
	 * @throws IOException
	 */
	public static void downloadBookInZip(List<WorkbookDto> workbookDtoList, HttpServletResponse response)
			throws IOException {
		try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("MS932"))) {
			List<String> wbNameList = new ArrayList<String>();
			for (WorkbookDto workbookDto : workbookDtoList) {

				// もし、同名のファイルがあった場合ファイル名末尾に「(n)」を付与する
				int count = 1;
				String wbName = workbookDto.getWbName();
				while (wbNameList.contains(wbName)) {
					String prefix = FileUtil.getPrefix(workbookDto.getWbName());
					String suffix = FileUtil.getSuffix(workbookDto.getWbName());
					wbName = prefix + "(" + count + ")." + suffix;
					count++;
				}
				wbNameList.add(wbName);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String zipName = "レポート";
				String fileName = new String((zipName + new Date().getTime()).getBytes("Windows-31J"), "ISO-8859-1");
				fileName += ".zip";
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
				workbookDto.getWb().write(baos);

				zos.putNextEntry(new ZipEntry(wbName));
				zos.write(baos.toByteArray(), 0, baos.toByteArray().length);
				zos.closeEntry();
			}
		}
	}

	/**
	 * レポートの一括ダウンロードを行う
	 * 
	 * @param workbookDto
	 * @param response
	 * @throws IOException
	 */
	public static void downloadReportInZip(List<WorkbookDto> workbookDtoList, HttpServletResponse response)
			throws IOException {
		try (ZipOutputStream zos = new ZipOutputStream(response.getOutputStream(), Charset.forName("MS932"))) {
			List<String> wbNameList = new ArrayList<String>();
			for (WorkbookDto workbookDto : workbookDtoList) {

				// もし、同名のファイルがあった場合ファイル名末尾に「(n)」を付与する
				int count = 1;
				String wbName = workbookDto.getWbName();
				while (wbNameList.contains(wbName)) {
					String prefix = FileUtil.getPrefix(workbookDto.getWbName());
					String suffix = FileUtil.getSuffix(workbookDto.getWbName());
					wbName = prefix + "(" + count + ")." + suffix;
					count++;
				}
				wbNameList.add(wbName);

				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				String zipName = messageUtil.getMessage("dailyReportId");
				String fileName = new String((zipName + new Date().getTime()).getBytes("Windows-31J"), "ISO-8859-1");
				fileName += ".zip";
				response.setContentType("application/octet-stream");
				response.setHeader("Content-Disposition", "filename=\"" + fileName + "\"");
				workbookDto.getWb().write(baos);

				zos.putNextEntry(new ZipEntry(wbName));
				zos.write(baos.toByteArray(), 0, baos.toByteArray().length);
				zos.closeEntry();
			}
		}
	}

	/**
	 * シート存在チェック
	 * 
	 * @param SheetName
	 * @return 対象のシート名が存在する場合：true<br>
	 *         対象のシート名が存在しない場合：false
	 */
	public boolean isExistSheet(String SheetName) {
		if (wb.getSheet(SheetName) != null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * セル内のタイプを取得
	 * 
	 * @param sheetName
	 * @param rowNum
	 * @param clmNum
	 * @return セルのタイプを表すint型の値
	 */
	public int getCellType(String sheetName, int rowNum, int clmNum) {
		Cell cell = this.getCell(sheetName, rowNum, clmNum);
		return cell.getCellType();
	}

	/**
	 * セル内の文字を取得
	 * 
	 * @param sheetName
	 * @param rowNum
	 * @param clmNum
	 * @return セル内の文字
	 */
	public String getCellValue(String sheetName, int rowNum, int clmNum) {
		Cell cell = this.getCell(sheetName, rowNum, clmNum);
		return this.getValue(cell);
	}

	/**
	 * セルタイプ(数値)からセル内を文字列へ変換
	 * 
	 * @param sheetName
	 * @param rowNum
	 * @param clmNum
	 * @return
	 */
	private String getValue(Cell cell) {
		CreationHelper crateHelper = this.getWb().getCreationHelper();
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_BLANK:
			// 空白の場合
			return "";
		case Cell.CELL_TYPE_BOOLEAN:
			// BOOLEANの場合
			return String.valueOf(cell.getBooleanCellValue());
		case Cell.CELL_TYPE_ERROR:
			// ERRORの場合
			return String.valueOf(cell.getErrorCellValue());
		case Cell.CELL_TYPE_FORMULA:
			// 計算式の場合
			FormulaEvaluator evaluator = crateHelper.createFormulaEvaluator();
			return this.getValue(evaluator.evaluateInCell(cell));
		case Cell.CELL_TYPE_NUMERIC:
			// 数値の場合
			if (DateUtil.isCellDateFormatted(cell)) {
				// 日付の場合
				return String.valueOf(cell.getDateCellValue());
			} else {
				// 数値の場合
				return String.valueOf(cell.getNumericCellValue());
			}
		case Cell.CELL_TYPE_STRING:
			// 文字列の場合
			return cell.getRichStringCellValue().toString();
		default:
			// 上記以外の場合、リッチテキストの文字列で返す
			return cell.getRichStringCellValue().toString();
		}
	}
}
