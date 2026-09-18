package jp.co.sss.lms.util;

/**
 * StringUtil(文字列)の追加ユーティリティ
 * 
 * @author 東京ITスクール
 */
public class StringUtil {

	/**
	 * 第一引数の値が第二引数の文字列内に幾つ入っているかカウント
	 * 
	 * @param count
	 * @return
	 */
	public static int separatorCount(char judgeChar, String searchString) {
		int count = 0;

		if (searchString == null) {
			return count;
		}

		for (int i = 0; i < searchString.length(); i++) {
			char s = searchString.charAt(i);
			if (s == judgeChar) {
				count++;
			}
		}
		return count;
	}

}
