package jp.co.sss.lms.util;

import java.math.BigDecimal;
import org.apache.commons.lang3.math.NumberUtils;
import org.springframework.stereotype.Component;

import jp.co.sss.lms.dto.FileDownloadDto;

/**
 * FileUtil<br>
 * ファイル関係のユーティリティクラス<br>
 * 
 * @author 東京ITスクール
 */
@Component
public class FileUtil {

	/** KB[1024byte] */
	private static final Long KB = (long) 1024;

	/** MB[1024KB] */
	private static final Long MB = (long) KB * 1024;

	/** GB[1024MB] */
	private static final Long GB = (long) MB * 1024;

	public Integer getPlaneFileId(FileDownloadDto fileDownloadDto, String hashedFileId) {
		return fileDownloadDto.getFileIdMap().get(hashedFileId);
	}

	/**
	 * 拡張子抜きのファイル名取得
	 * 
	 * @param fileName ファイル名
	 * @return fileName
	 */
	public static String getPrefix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(0, point);
		}
		return fileName;
	}

	/**
	 * ファイルの拡張子取得<br>
	 * 
	 * @param fileName ファイル名
	 * @return fileName
	 */
	public static String getSuffix(String fileName) {
		if (fileName == null)
			return null;
		int point = fileName.lastIndexOf(".");
		if (point != -1) {
			return fileName.substring(point + 1);
		}
		return fileName;
	}

	/**
	 * ファイルサイズを単位をつけた形に変換します。 数値に変換出来ない文字列の場合、nullを返却します。<br>
	 * 
	 * @param fileSize
	 * @return ファイルサイズ
	 */
	public static String parseFileSize(String fileSize) {
		if (!NumberUtils.isDigits(fileSize)) {
			return null;
		}
		return parseFileSize(Long.parseLong(fileSize));
	}

	/**
	 * ファイルサイズを単位をつけた形に変換します。 数値に変換出来ない文字列の場合、nullを返却します。<br>
	 * 
	 * @param fileSize
	 * @return ファイルサイズ
	 */
	public String parseFileSize(Integer fileSize) {
		return parseFileSize(fileSize.longValue());
	}

	/**
	 * ファイルサイズを単位をつけた形に変換します。<br>
	 * 
	 * @param fileSize
	 * @return 単位をつけたファイルサイズ
	 */
	public static String parseFileSize(Long fileSize) {
		String fileSizeUnit;
		double dsize = fileSize;
		if (KB > fileSize) {
			return fileSize + " Byte";

		} else if (MB > fileSize) {
			dsize = dsize / KB;
			fileSizeUnit = " KB";

		} else if (GB > fileSize) {
			dsize = dsize / MB;
			fileSizeUnit = " MB";

		} else {
			dsize = dsize / GB;
			fileSizeUnit = " GB";
		}

		BigDecimal bi = new BigDecimal(String.valueOf(dsize));
		int scale = 3 - (bi.precision() - bi.scale());

		Object value;
		if (scale == 0) {
			value = bi.setScale(scale, BigDecimal.ROUND_HALF_UP).intValue();
		} else {
			value = bi.setScale(scale, BigDecimal.ROUND_HALF_UP).doubleValue();
		}
		return value + fileSizeUnit;
	}
}
