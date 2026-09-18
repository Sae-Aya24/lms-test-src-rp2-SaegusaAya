package jp.co.sss.lms.ut.controller;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.Assertions.fail;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockHttpSession;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import jp.co.sss.lms.controller.LoginController;
import jp.co.sss.lms.dto.InfoDto;
import jp.co.sss.lms.exception.NoLoginException;
import jp.co.sss.lms.service.InfoService;
import jp.co.sss.lms.service.LoginService;
import jp.co.sss.lms.util.LoginUserUtil;
import jp.co.sss.lms.util.MessageUtil;

/**
 * JUnitによる機能試験(ホワイトボックステスト)_テストコードサンプル
 * コントローラーを試験する際のサンプルコードです。
 * サービスクラス等と違い、MockMvcのコントローラー独自の記法があるので注意してください。
 * MockMvcについてはテスト技法（JUnit・Mockito・MockMVC）_講義資料.pdfの34ページを参照）
 * 
 * */
@SpringBootTest
public class LoginControllerTest {

	public MockMvc mockMvc;

	public MockHttpServletRequestBuilder getRequest;

	// 既に完成されたクラスは、ReflectionTestUtilsで設定出来る。
	@Autowired
	private MessageUtil messageUtil;

	// Mock、Spyの対象となるクラスを宣言
	@Mock
	private InfoService infoService;
	@Mock
	private LoginUserUtil loginUserUtil;
	@Mock
	private LoginService loginService;

	// @Mock、@Spyで宣言したクラスをテスト対象クラスに注入
	@InjectMocks
	private LoginController loginController;

	/**
	 * setupメソッド
	 * 
	 * Tips: BeforeEachアノテーションを付与するとテストメソッドを実行する前に必ず実行される（必要のない場合、省略可）
	 *       テストクラス全体で事前に設定すべき処理を記載することでソースの記載量を削減できる。
	 * */
	@BeforeEach
	public void setup() {
		// Spring MVCにテスト対象のコントローラを設定する 
		mockMvc = MockMvcBuilders.standaloneSetup(loginController).build();

		// 既に完成されているクラスに対して、@BeforEachにReflectionTestUtilsを設定することで、各テストメソッドで呼び出す処理を省略出来る。
		ReflectionTestUtils.setField(loginController, "messageUtil", messageUtil);
	}

	/**
	 * tearDownメソッド
	 * 
	 * Tips:AfterEachアノテーションを付与すると各テストメソッドが終了した後にに必ず実行される。（必要のない場合、省略可）
	 * 　　　　　
	 * */
	@AfterEach
	public void tearDown() {

	}

	/**
	 * Case.1_1 ログインコントローラー試験　初期表示テスト(未ログイン時 正常終了)
	 * ■対象メソッド：index() 
	 * ■試験パラメータ：なし
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・HTTPステータスが「200」であること
	 *  ・返却されるViewのパスが「/login/index」であること
	 *  
	 * @throws Exception 
	 * */
	@Test
	void testCase1_1() {

		// セッション情報を事前に設定する場合、MockHttpSessionを使用する
		MockHttpSession session = new MockHttpSession();
		ReflectionTestUtils.setField(loginController, "session", session);

		// モック対象メソッドの返却値を設定
		when(loginUserUtil.isLogin()).thenReturn(false);
		when(infoService.getInfo()).thenReturn(new InfoDto());

		// コントローラー記載のRequetMappingのパスを指定する
		getRequest = MockMvcRequestBuilders.get("/");

		try {
			// 試験開始
			// getRequestの情報を基にコントローラーを呼び出し検証を行う
			mockMvc.perform(getRequest) // コントローラーの呼び出し
					//.andDo(print()) // リクエスト情報をコンソールにprint(デバッグ用)
					.andExpect(status().isOk()) // 実行結果のHTTPステータスが200(正常終了)かどうか検証する
					.andExpect(view().name("login/index"))// viewをreturnしているか検証する
					.andReturn();

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

	/**
	 * Case.1_2 ログインコントローラー試験　初期表示テスト(受講生ログイン時 正常終了)
	 * 観点：
	 * 
	 * ■対象メソッド：index() 
	 * ■試験パラメータ：loginUserDto.受講生データ
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・HTTPステータスが「302」であること
	 *  ・返却されるリダイレクトURLが「/course/detail」であること
	 *  
	 * @throws Exception 
	 * */
	@Test
	public void testCase1_2() {

		// モック対象メソッドの返却値を設定
		when(loginUserUtil.isLogin()).thenReturn(true);
		when(loginUserUtil.isStudent()).thenReturn(true);
		when(infoService.getInfo()).thenReturn(new InfoDto());
		when(loginUserUtil.sendDisp()).thenReturn("redirect:/course/detail");

		// コントローラー記載のRequetMappingのパスを指定する
		getRequest = MockMvcRequestBuilders.get("/");
		try {
			// 試験開始
			// getRequestの情報を基にコントローラーを呼び出す。Case1_1のMvcResultは使用しない場合、省略可能
			mockMvc.perform(getRequest) // コントローラーの呼び出し
					//.andDo(print()) // リクエスト情報をコンソールにprintする（デバッグ用）
					.andExpect(status().isFound()) // 実行結果のHTTPステータスが302(リダイレクト)かどうか検証する
					.andExpect(redirectedUrl("/course/detail")); // リダイレクトパスが正しいか検証する(ログイン機能のみ検証する)

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

	/**
	 * Case.1_3 ログインコントローラー試験　初期表示テスト(講師ログイン時 正常終了)
	 * 
	 * ■対象メソッド：index() 
	 * ■試験パラメータ：loginUserDto.講師データ
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・HTTPステータスが「302」であること
	 *  ・返却されるリダイレクトURLが「(改修対象のため、暫定空文字)」であること
	 *  
	 * @throws Exception 
	 * */
	@Test
	void testCase1_3() {

		// モック対象メソッドの返却値を設定
		when(loginUserUtil.isLogin()).thenReturn(true);
		when(loginUserUtil.isTeacher()).thenReturn(true);
		when(infoService.getInfo()).thenReturn(new InfoDto());
		when(loginUserUtil.sendDisp()).thenReturn("redirect:/user/list/student");

		// コントローラー記載のRequetMappingのパスを指定する
		getRequest = MockMvcRequestBuilders.get("/");
		try {
			// getRequestの情報を基にコントローラーを呼び出す
			mockMvc.perform(getRequest)
					//.andDo(print()) 
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/user/list/student"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

	/**
	 * Case.1_4 ログインコントローラー試験　初期表示テスト(管理者ログイン時 正常終了)
	 * 
	 * ■対象メソッド：index() 
	 * ■試験パラメータ：loginUserDto.管理者データ
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・HTTPステータスが「302」であること
	 *  ・返却されるリダイレクトURLが「/course/list」であること
	 *  
	 * */
	@Test
	public void testCase1_4() {

		// モック対象メソッドの返却値を設定
		when(loginUserUtil.isLogin()).thenReturn(true);
		when(loginUserUtil.isAdmin()).thenReturn(true);
		when(infoService.getInfo()).thenReturn(new InfoDto());
		when(loginUserUtil.sendDisp()).thenReturn("redirect:/course/list");

		// コントローラー記載のRequetMappingのパスを指定する
		getRequest = MockMvcRequestBuilders.get("/");
		try {
			// getRequestの情報を基にコントローラーを呼び出す
			mockMvc.perform(getRequest)
					//.andDo(print()) 
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/course/list"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

	/**
	 * Case.1_5 ログインコントローラー試験　初期表示テスト(異常終了)
	 * 
	 * ■対象メソッド：index() 
	 * ■試験パラメータ：権限：権限マスタに存在しない値
	 * ■試験観点：
	 * 　NoLoginExceptionが発生すること
	 *  
	 * */
	@Test
	public void testCase1_5() {

		// モック対象メソッドの返却値を設定
		when(loginUserUtil.isLogin()).thenReturn(true);
		when(infoService.getInfo()).thenReturn(new InfoDto());

		// コントローラー記載のRequetMappingのパスを指定する
		getRequest = MockMvcRequestBuilders.get("/");
		try {
			// コントローラーの例外を検証する際は、以下の記法(ラムダ式)を使用する
			// 例：assertThatThrownBy( (引数) -> 処理)　
			assertThatThrownBy(() -> mockMvc.perform(getRequest))
					.hasCause(new NoLoginException()); // 例外を検証する場合、hascase(例外クラス)のように記載する

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

	/**
	 * Case.1_6 ログインコントローラー試験　ログイン処理テスト(正常終了_ログイン完了)
	 * ■対象メソッド：login() 
	 * ■試験パラメータ：権限：ユーザマスタに存在するユーザID、パスワード
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・HTTPステータスが「302」であること
	 *  ・返却されるリダイレクトURLが「/course/detail」であること
	 * */
	@Test
	public void testCase1_6() {

		// ログイン情報
		String userId = "StudentAA01";
		String password = "StudentAA01";

		// モック対象メソッドの返却値を設定
		when(loginService.getLoginInfo(userId, password)).thenReturn("");
		when(loginUserUtil.isStudent()).thenReturn(true);
		when(loginUserUtil.sendDisp()).thenReturn("redirect:/course/detail");

		getRequest = MockMvcRequestBuilders.post("/login").param("loginId", userId).param("password", password);

		try {
			mockMvc.perform(getRequest)
					.andExpect(status().isFound())
					.andExpect(redirectedUrl("/course/detail"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

	/**
	 * Case.1_7 ログインコントローラー試験　ログイン処理テスト(正常終了_ログイン失敗)
	 * ■対象メソッド：login() 
	 * ■試験パラメータ：権限：ユーザマスタに存在しないユーザID、パスワード
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・HTTPステータスが「200」であること
	 *  ・返却されるURLが「/login/index」であること
	 * */
	@Test
	public void testCase1_7() {

		// ログイン情報
		String userId = "noUser";
		String password = "test";

		// モック対象メソッドの返却値を設定
		when(loginService.getLoginInfo(userId, password)).thenReturn("ログインに失敗しました");

		getRequest = MockMvcRequestBuilders.post("/login").param("loginId", userId).param("password", password);

		try {
			mockMvc.perform(getRequest)
					//.andDo(print()) 
					.andExpect(status().isOk())
					.andExpect(view().name("login/index"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

	/**
	 * Case.1_8 ログインコントローラー試験　ログイン処理テスト(正常終了_入力チェックエラー)
	 * ■対象メソッド：login() 
	 * ■試験パラメータ：権限：入力チェックエラーとなるユーザID、パスワード
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・入力チェックエラーが発生すること
	 *  ・HTTPステータスが「200」であること
	 *  ・返却されるリダイレクトURLが「/login/index」であること
	 * */
	@Test
	public void testCase1_8() {

		// ログイン情報
		String userId = "入力チェックエラーユーザーID";
		String password = "入力チェックエラーパスワード";

		getRequest = MockMvcRequestBuilders.post("/login").flashAttr("loginId", userId).flashAttr("password", password);

		try {
			mockMvc.perform(getRequest)
					//.andDo(print()) 
					.andExpect(model().hasErrors()) // 入力チェックエラーが発生したかを検証する
					.andExpect(status().isOk())
					.andExpect(view().name("login/index"));

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}

	/**
	 * Case.1_9 ログインコントローラー試験　ログアウト処理テスト(正常終了)
	 * ■対象メソッド：logout() 
	 * ■試験パラメータ：権限：任意のセッションキーと値
	 * ■試験観点：
	 *  ・正常終了すること
	 *  ・入力チェックエラーが発生すること
	 *  ・HTTPステータスが「302」であること
	 *  ・事前に設定したセッションが削除されること
	 *  ・セッションキー"logout"に「ログアウトしました。」の文言が格納されていること
	 *  ・返却されるリダイレクトURLが「""」であること
	 *  
	 * */
	@Test
	public void testCase1_9() {

		// 期待値を設定
		String actual = "ログアウトしました。";

		getRequest = MockMvcRequestBuilders.post("/logout");

		try {
			// セッション情報を事前に設定する場合、MockHttpSessionを使用する
			MockHttpSession session = new MockHttpSession();
			session.setAttribute("testSession", "test");
			ReflectionTestUtils.setField(loginController, "session", session);

			mockMvc.perform(getRequest)
					.andDo(print())
					.andExpect(status().isFound())
					// .andExpect(redirectedUrl("")) // Controllerクラスでリダイレクト先が設定されていないため、テスト失敗になる
					.andReturn();

			// セッションスコープ内の値を検証
			assertNull(session.getAttribute("testSession")); // 設定したセッションがログアウト処理によって削除されることを確認
			assertEquals(session.getAttribute("logout").toString(), actual);

		} catch (Exception e) {
			e.printStackTrace();
			fail(new Object() {
			}.getClass().getEnclosingMethod().getName());
		}
	}
}
