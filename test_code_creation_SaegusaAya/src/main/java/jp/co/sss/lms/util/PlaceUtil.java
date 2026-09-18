package jp.co.sss.lms.util;

/**
 * 会場ユーティリティ
 * 
 * @author 東京ITスクール
 */
public class PlaceUtil {
	/** 備考区切り文字 */
	public static final String REGEX_CHAR = "\\$";

	/**
	 * 備考よりタイトルを取得
	 * 
	 * @param placeDto
	 * @return
	 */
	public static String getPlaceNoteTitle(String placeNote) {
		if (placeNote == null) {
			return "";
		}

		String[] placeNoteArray = placeNote.split(REGEX_CHAR, -1);
		if (placeNoteArray.length < 3) {
			return "";
		}
		return placeNoteArray[0];
	}

	/**
	 * 備考より教室名を取得
	 * 
	 * @param placeDto
	 * @return
	 */
	public static String getPlaceNoteClassRoomName(String placeNote) {
		if (placeNote == null) {
			return "";
		}

		String[] placeNoteArray = placeNote.split(REGEX_CHAR, -1);
		if (placeNoteArray.length < 3) {
			return "";
		}
		return placeNoteArray[1];
	}

	/**
	 * 備考より備考を取得
	 * 
	 * @param placeDto
	 * @return
	 */
	public static String getPlaceNoteRemarks(String placeNote) {
		if (placeNote == null) {
			return "";
		}

		String[] placeNoteArray = placeNote.split(REGEX_CHAR, -1);
		if (placeNoteArray.length < 3) {
			return "";
		}
		return placeNoteArray[2];
	}
}
