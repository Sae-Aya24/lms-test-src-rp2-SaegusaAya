package jp.co.sss.lms.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.springframework.stereotype.Component;

/**
 * パスワードユーティリティ
 * 
 * @author 東京ITスクール
 */
@Component
public class PasswordUtil {

	private static int STRETCH_COUNT = 10;

	/** パスワードポリシー */
	public final static String PASSWORD_POLICY = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])[0-9a-zA-Z\\-]{8,}$";

	/** パスワード有効文字 */
	private final static char[] PASSWORD_VALID_CHAR = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
			'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '0', '1', '2',
			'3', '4', '5', '6', '7', '8', '9' };

	/**
	 * salt +ハッシュ化+ストレッチングしたパスワ-ドを 取得
	 * 
	 * @param password
	 * @param userId
	 * @return getStretchedPassword()
	 */
	public String getSaltedAndStrechedPassword(String password, String userId) {
		return getStretchedPassword(getSaltedPassword(password, userId), userId);
	}

	/**
	 * salt＋ハッシュ化したパスワードを取得
	 * 
	 * @param password
	 * @param userId
	 * @return getSha256()
	 * 
	 */
	private String getSaltedPassword(String password, String userId) {
		String salt = getSha256(userId);
		return getSha256(salt + password);
	}

	/**
	 * salt + ストレッチングしたパスワードを取得(推奨)
	 * 
	 * @param password
	 * @param userId
	 * @return hash
	 */
	private String getStretchedPassword(String password, String userId) {
		String salt = getSha256(userId);
		String hash = "";

		for (int i = 0; i < STRETCH_COUNT; i++) {
			hash = getSha256(hash + salt + password);
		}

		return hash;
	}

	/**
	 * 文字列から SHA256 のハッシュ値を取得
	 * 
	 * @param target
	 * @return SHA256 のハッシュ値
	 */
	private String getSha256(String target) {
		MessageDigest md = null;
		StringBuffer buf = new StringBuffer();
		try {
			md = MessageDigest.getInstance("SHA-256");
			md.update(target.getBytes());
			byte[] digest = md.digest();

			for (int i = 0; i < digest.length; i++) {
				buf.append(String.format("%02x", digest[i]));
			}

		} catch (NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}

		return buf.toString();
	}

	/**
	 * パスワードの自動生成
	 * 
	 * @return password
	 */
	public String generatePassword() {
		String password = "";

		while (!isValidPassword(password)) {
			password = "";
			while (password.length() < 8) {
				Random rand = new Random();
				password += PASSWORD_VALID_CHAR[rand.nextInt(PASSWORD_VALID_CHAR.length)];
			}
		}

		return password;
	}

	/**
	 * パスワードポリシーに沿っているパスワードであるかチェックする。
	 * 
	 * @param password
	 * @return 判定結果：パスワードポリシーに沿っている場合true、パスワードポリシーに沿っていない場合false
	 */
	public boolean isValidPassword(String password) {
		if (password == null || password.equals("")) {
			return false;
		} else if (!password.matches(PASSWORD_POLICY)) {
			return false;
		}
		return true;
	}
}