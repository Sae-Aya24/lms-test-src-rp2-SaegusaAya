package jp.co.sss.lms.ut.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.lang.reflect.Method;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.util.ReflectionTestUtils;

import jakarta.servlet.http.HttpSession;
import jp.co.sss.lms.dto.LoginUserDto;
import jp.co.sss.lms.entity.LoginUser;
import jp.co.sss.lms.mapper.LoginMapper;
import jp.co.sss.lms.mapper.MUserMapper;
import jp.co.sss.lms.service.LoginService;
import jp.co.sss.lms.util.Constants;
import jp.co.sss.lms.util.DateUtil;
import jp.co.sss.lms.util.MessageUtil;
import jp.co.sss.lms.util.PasswordUtil;

/**
 * JUnitによる機能試験(ホワイトボックステスト)_テストコードサンプル<br>
 * サービスクラスを試験する際のサンプルコードです。<br>
 * 
 * */

@SpringBootTest
public class LoginServiceTest {

	@Autowired
	private LoginUserDto loginUserDto;
	@Autowired
	private MessageUtil messageUtil;
	@Autowired
	private DateUtil dateUtil;

	@Mock
	private HttpSession session;
	@Mock
	private MUserMapper mUserMapper;
	@Mock
	private PasswordUtil passwordUtil;
	@Mock
	private LoginMapper loginMapper;

	@InjectMocks
	private LoginService loginService;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.initMocks(this);
		// テスト対象クラスのフィールドは以下の記述で任意に設定することが出来る 
		// ReflectionTestUtils.setField(第1引数：試験対象のクラス 第2引数：セットしたいフィールド名 第3引数：セットする値)
		ReflectionTestUtils.setField(loginService, "lockCount", 3);
		ReflectionTestUtils.setField(loginService, "lockMinute", 1);

		// 既に完成されているクラスに対しても、@BeforEachで設定することで、各テストメソッドで呼び出す処理を省略出来る。
		ReflectionTestUtils.setField(loginService, "messageUtil", messageUtil);
		ReflectionTestUtils.setField(loginService, "dateUtil", dateUtil);
	}

	@AfterEach
	public void tearDown() {

	}

	/**
	 * Case.1_1 ログインサービス試験　ログイン処理_正常終了1<br>
	 * <br>
	 * ■対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ： 
	 * 　・ログインID：任意<br>
	 *　　・パスワード：任意<br>
	 * 　・ログインユーザーDTO.パスワードロック回数:1<br>
	 * 　・ログインユーザーDTO.パスワードロック時刻:null<br>
	 * 　・ログインユーザーエンティティ.権限：受講生
	 * 　・ログインユーザーエンティティ.非表示フラグ：true
	 * ■試験観点：<br>
	 *  ・正常終了すること<br>
	 *  ・メッセージ出力：「(空文字)」であること<br>
	 *  ・ログインユーザーDTO.パスワードロック時刻:"(空文字)"であること<br>
	 * 　・ログインユーザーDTO.パスワードNGカウント：0であること<br>
	 * 
	 * */
	@Test
	public void testCase1_1() {

		// テスト対象メソッド実行に必要な引数のパラメータ設定
		String loginId = "loginId";
		String password = "password";

		// ログインユーザーDTO（セッション）に必要な値を対象メソッドのフィールドに格納
		loginUserDto.setPasswordNgCount(1);
		loginUserDto.setPasswordNgDate(null);
		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);

		// モックの戻り値となるテストデータを取得
		LoginUser loginUser = new LoginUser();
		loginUser.setRole(Constants.CODE_VAL_ROLL_STUDENT);
		loginUser.setHiddenFlg(Constants.DB_HIDDEN_FLG_FALSE);

		// モック対象メソッドの返却値を設定
		when(passwordUtil.getSaltedAndStrechedPassword(password, loginId)).thenReturn("aaa"); //　パスワードソルト処理のモック
		when(loginMapper.getLoginDetailByLoginIdAndPassword(loginId, "aaa", Constants.DB_FLG_FALSE))
				.thenReturn(loginUser); // ログインユーザー取得処理のモック

		// 期待値の設定
		String expected_message = "";
		String expected_NgDate = "";
		Integer expected_NgCount = 0;

		//　試験実行
		String actual_message = loginService.getLoginInfo(loginId, password);

		// 検証処理
		assertEquals(expected_message, actual_message);
		assertEquals(expected_NgDate, loginUserDto.getPasswordNgDate());
		assertEquals(expected_NgCount, loginUserDto.getPasswordNgCount());
	}

	/**
	 * Case.1_2 ログインサービス試験　ログイン処理_正常終了3<br>
	 * <br>
	 * ■対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ：
	 * 　・ログインID：任意<br>
	 *　　・パスワード：任意<br>
	 * 　・ログインユーザーDTO.パスワードロック回数:1<br>
	 * 　・ログインユーザーDTO.パスワードロック時刻:現在時刻-2分<br>
	 * ■試験観点：<br>
	 *  ・正常終了すること<br>
	 *  ・メッセージ出力：「(空文字)」であること<br>
	 *  ・ログインユーザーDTO.パスワードロック時刻:"(空文字)"であること<br>
	 * 　・ログインユーザーDTO.パスワードNGカウント：0であること<br>
	 * @throws ParseException <br>
	 * 
	 * */
	@Test
	public void testCase1_2() {

		// テスト対象メソッド実行に必要な引数のパラメータ設定
		String loginId = "loginId";
		String password = "password";

		// checkLockTime()がtrueとなるパラメータ設定
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.MINUTE, -2);
		Date date = dateUtil.calenderToTimestamp(calendar);
		String ngDate = dateUtil.dateToString(date);

		// ログインユーザーDTO（セッション）に必要な値を対象メソッドのフィールドに格納
		loginUserDto.setPasswordNgCount(1);
		loginUserDto.setPasswordNgDate(ngDate);

		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);

		// モックの戻り値となるテストデータを取得
		LoginUser loginUser = new LoginUser();
		loginUser.setRole(Constants.CODE_VAL_ROLL_STUDENT);
		loginUser.setHiddenFlg(Constants.DB_HIDDEN_FLG_FALSE);

		// モック対象メソッドの返却値を設定
		when(passwordUtil.getSaltedAndStrechedPassword(password, loginId)).thenReturn("aaa"); //　パスワードソルト処理のモック
		when(loginMapper.getLoginDetailByLoginIdAndPassword(loginId, "aaa", Constants.DB_FLG_FALSE))
				.thenReturn(loginUser); // ログインユーザー取得処理のモック

		// 期待値の設定
		String expected_message = "";
		String expected_NgDate = "";
		Integer expected_NgCount = 0;

		//　試験実行
		String actual_message = loginService.getLoginInfo(loginId, password);

		// 検証処理
		assertEquals(expected_message, actual_message);
		assertEquals(expected_NgDate, loginUserDto.getPasswordNgDate());
		assertEquals(expected_NgCount, loginUserDto.getPasswordNgCount());
	}

	/**
	 * Case.1_3 ログインサービス試験　ログイン処理_アカウントロック検証1<br>
	 * <br>
	 * ■対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ：
	 * 　・ログインID：任意<br>
	 *　　・パスワード：任意<br>	
	 *  ・ロック日時：現在時刻-30秒<br>
	 * ■試験観点：<br>
	 * 　・正常終了すること<br>
	 * 　・メッセージ出力：「規定の回数を超えたため、アカウントにロックがかかりました。しばらくたってから再度お試しください。」であること。<br>
	 * 
	 * */
	@Test
	public void testCase1_3() {

		// メソッド実行に必要な引数のパラメータ設定
		String loginId = "loginId";
		String password = "password";

		// checkLockTime()がfalseとなるパラメータ設定
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.add(Calendar.SECOND, -30);
		Date date = dateUtil.calenderToTimestamp(calendar);
		String ngDate = dateUtil.dateToString(date);

		// ログインユーザーDTO（セッション）に必要な値を対象メソッドのフィールドに格納
		loginUserDto.setPasswordNgDate(ngDate);
		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);

		// 期待値の設定
		String expected = messageUtil.getMessage(Constants.VALID_KEY_LOGIN_LOCK);

		// 試験実行
		String actual = loginService.getLoginInfo(loginId, password);

		//　検証処理
		assertEquals(expected, actual);
	}

	/**
	 * Case.1_4 ログインサービス試験　ログイン処理_アカウントロック検証2<br>
	 * <br>
	 * ■対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ：
	 * 　・ログインID：任意<br>
	 *　　・パスワード：任意<br>	
	 *  ・ログインユーザーDTO.パスワードNGカウント:3<br>
	 * ■試験観点：<br>
	 * 　　・正常終了すること<br>
	 * 　　・メッセージが出力：「規定の回数を超えたため、アカウントにロックがかかりました。しばらくたってから再度お試しください。」であること<br>
	 * 　　・ログインユーザーDTO.パスワードNGカウント： 4　になること<br>
	 *　　　・ログインユーザーDTO.パスワードNG日時：Nullでないこと(現在時刻が厳密に計測出来ない為)<br>
	 * 
	 * */
	@Test
	public void testCase1_4() {

		// メソッド実行に必要な引数のパラメータ設定
		String loginId = "loginFailedId";
		String password = "loginFailedPassword";

		// 設定したDtoを試験対象クラスのフィールドに設定する
		loginUserDto.setPasswordNgCount(3);
		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);

		// 期待値の設定
		String expected = messageUtil.getMessage(Constants.VALID_KEY_LOGIN_LOCK);
		Integer expectedNgCount = 4;

		// 試験実行
		String actual = loginService.getLoginInfo(loginId, password);

		//　検証処理
		assertEquals(expected, actual);
		assertEquals(expectedNgCount, loginUserDto.getPasswordNgCount());
		assertNotNull(loginUserDto.getPasswordNgDate());
	}

	/**
	 * Case.1_5 ログインサービス試験　ログイン処理_ログイン失敗<br>
	 * <br>
	 * ■対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ：
	 *  ・ログインID:任意<br>
	 *　 ・パスワード：任意<br>
	 * ■試験観点：<br>
	 * 　　・正常終了すること<br>
	 * 　　・メッセージ出力：「ログインに失敗しました。」であること<br>
	 * 　　・ログインユーザーDTO.パスワードNGカウント：1であること<br>
	 * 
	　* */
	@Test
	public void testCase1_5() {

		// 期待値の設定
		String expected_message = messageUtil.getMessage(Constants.VALID_KEY_LOGIN);
		Integer expectedNgCount = 1;

		// メソッド実行に必要な引数のパラメータ設定
		String loginId = "LoginFailedId";
		String password = "LoginFailedPassword";

		// Dtoを試験対象クラスのフィールドに設定する
		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);

		// 試験実行
		String actual = loginService.getLoginInfo(loginId, password);

		//　検証処理
		assertEquals(expected_message, actual);
		assertEquals(expectedNgCount, loginUserDto.getPasswordNgCount());
	}

	/**
	 * Case.1_6 ログインサービス試験　ログイン処理_担当会場終了<br>
	 * <br>
	 * ■対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ：
	 * 　・ログインID:任意<br>
	 *　　・パスワード：任意<br>
	 *　　・ログインユーザーエンティティ.権限：講師<br>
	 *　　・ログインユーザーエンティティ.非表示フラグ：false<br>
	 * ■試験観点：<br>
	 * 　・正常終了すること<br>
	 * 　・メッセージ出力：""(空文字)であること<br>
	 * 
	 * */
	@Test
	public void testCase1_6() {

		// 期待値の設定
		String expected = "";

		// メソッド実行に必要な引数のパラメータ設定
		String loginId = "Teacher01";
		String password = "Teacher01";

		// モックの戻り値となるテストデータを取得
		LoginUser loginUser = new LoginUser();
		loginUser.setRole(Constants.CODE_VAL_ROLL_TEACHER);
		loginUser.setHiddenFlg(Constants.DB_HIDDEN_FLG_FALSE);

		// モック対象メソッドの返却値を設定
		when(passwordUtil.getSaltedAndStrechedPassword(password, loginId)).thenReturn("aaa"); //　パスワードソルト処理のモック
		when(loginMapper.getLoginDetailByLoginIdAndPassword(loginId, "aaa", Constants.DB_FLG_FALSE))
				.thenReturn(loginUser); // ログインユーザー取得処理のモック

		// Dtoを試験対象クラスのフィールドに設定する
		loginUserDto.setPasswordNgCount(1);
		loginUserDto.setPasswordNgDate(null);
		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);

		// 試験実行
		String actual = loginService.getLoginInfo(loginId, password);

		// 検証処理
		assertEquals(expected, actual);
	}

	/**
	 * Case.1_7 ログインサービス試験　ログイン処理_担当会場終了<br>
	 * <br>
	 * ■対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ：<br>
	 * ・ログインID:任意<br>
	 *　・パスワード：任意<br>
	 *　・(モック)ログインユーザーエンティティ.権限：講師<br>
	 *　・(モック)ログインユーザーエンティティ.非表示フラグ：true<br>
	 * ■試験観点：<br>
	 * 　・正常終了すること<br>
	 * 　・メッセージ出力：「担当会場が終了しているため、ログインできません。詳しくは東京ITスクール運営事務局までお問い合わせください。」であること<br>
	 * 
	 * */
	@Test
	public void testCase1_7() {

		// 期待値の設定
		String expected = messageUtil.getMessage(Constants.VALID_KEY_LOGIN_PLACENODISPLAY);

		// メソッド実行に必要な引数のパラメータ設定
		String loginId = "Teacher01";
		String password = "Teacher01";

		// モックの戻り値となるテストデータを取得
		LoginUser loginUser = new LoginUser();
		loginUser.setRole(Constants.CODE_VAL_ROLL_TEACHER);
		loginUser.setHiddenFlg(Constants.DB_HIDDEN_FLG_TRUE);

		// モック対象メソッドの返却値を設定
		when(passwordUtil.getSaltedAndStrechedPassword(password, loginId)).thenReturn("aaa"); //　パスワードソルト処理のモック
		when(loginMapper.getLoginDetailByLoginIdAndPassword(loginId, "aaa", Constants.DB_FLG_FALSE))
				.thenReturn(loginUser); // ログインユーザー取得処理のモック

		// Dtoを試験対象クラスのフィールドに設定する
		loginUserDto.setPasswordNgCount(1);
		loginUserDto.setPasswordNgDate(null);
		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);

		// 試験実行
		String actual = loginService.getLoginInfo(loginId, password);

		// 検証処理
		assertEquals(expected, actual);
	}

	/**
	 * Case.1_8 ログインサービス試験　ログインID取得処理_ユーザー取得OK<br>
	 * <br>
	 * ■対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ： 
	 * ・ログインID:任意<br>
	 *　・(モック)ログインユーザーエンティティ.ユーザーID：1<br>
	 * ■試験観点：<br>
	 * 　・正常終了すること<br>
	 * 　・ログインユーザーDTO.パスワードNGカウント：0であること<br>
	 * 　・ログインユーザーDTO.パスワードNG日付：""であること<br>
	 * 
	 * */
	@Test
	public void testCase1_8() throws Exception {

		// 期待値の設定
		String expected_NgDate = "";
		Integer expected_NgCount = 0;

		//試験パラメータ設定
		Integer userId = 1;

		// モックの戻り値となるテストデータを取得
		LoginUser loginUser = new LoginUser();
		loginUser.setUserId(userId);

		// モック対象メソッドの返却値を設定
		when(loginMapper.getLoginDetailByUserId(userId, Constants.DB_FLG_FALSE)).thenReturn(loginUser); // ログインユーザー取得処理のモック

		// Dtoを試験対象クラスのフィールドに設定する
		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);
		// 試験実行
		loginService.getLoginId(userId);

		// 検証処理
		assertEquals(expected_NgDate, loginUserDto.getPasswordNgDate());
		assertEquals(expected_NgCount, loginUserDto.getPasswordNgCount());
	}

	/**
	 * Case.1_9 ログインサービス試験　ログインID取得処理_ユーザー取得OK<br>
	 * <br>
	 * 対象メソッド：getLoginInfo()<br>
	 * ■試験パラメータ： 
	 * ・ログインID:99999<br>
	 *　・(モック)ログインユーザーエンティティ：null<br>
	 * 試験観点：<br>
	 * 　・正常終了すること<br>
	 * 　・返却値のユーザID:""であること<br>
	 * 
	 * */
	@Test
	public void testCase1_9() {

		// 期待値の設定
		String expected = "";

		// 試験パラメータ設定
		Integer userId = 999999;

		// 試験実行
		String actual = loginService.getLoginId(userId);

		// 検証処理
		assertEquals(expected, actual);
	}

	/**
	 * Case.xx ログインサービス試験　アカウントNG回数チェック(リフレクションを用いたサンプル)<br>
	 * <br>
	 * 対象メソッド：checkLockCount() ※privateメソッド<br>
	 * 試験パラメータ：ログインユーザDto.パスワードNG回数　= 0<br>
	 * 試験観点：<br>
	 * 　・正常終了<br>
	 * 　・戻り値がtrueであること<br>
	 * <br>
	 * @throws Exception <br>
	 * <br>
	 * [補足]直接privateメソッドへアクセスする方法を「リフレクション」と呼びます。<br>
	 * 　　　　　本来の処理の流れとしてpublicメソッドからアクセスするため非推奨の方法ですが<br>
	 * 　　　　　どうしてもpublicメソッド経由からのprivateメソッドの試験が難しい場合（分岐が複雑である等）<br>
	 * 　　　　　以下の方法でのテスト実施を許可します。<br>
	 * */
	@Test
	public void testCase4_1() throws Exception {

		//試験パラメータ設定
		loginUserDto.setPasswordNgCount(0);

		// サービス内のloginUserDtoフィールドに値を設定する
		ReflectionTestUtils.setField(loginService, "loginUserDto", loginUserDto);

		// privateメソッドはリフレクションを使用することでアクセス出来る。引数がある場合、引数分の型のクラスを指定する
		Method method = LoginService.class.getDeclaredMethod("checkLockCount"); // 対象のprivateメソッドを指定
		method.setAccessible(true); // privateメソッドへのアクセスを許可
		boolean actual = (boolean) method.invoke(loginService); // 該当メソッドを実行

		// 検証処理
		assertTrue(actual);
	}
}
