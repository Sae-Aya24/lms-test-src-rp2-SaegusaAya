package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// トップページURLにアクセス
		goTo("http://localhost:8080/lms/");

		// タイトルを取得
		String title = webDriver.getTitle();

		// ログインID欄、パスワード欄の要素を取得
		WebElement loginIdElement = webDriver.findElement(By.id("loginId"));
		WebElement passwordElement = webDriver.findElement(By.id("password"));

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("ログイン | LMS", title, "タイトルが期待値通りであること");
		assertEquals("", loginIdElement.getAttribute("value"), "ログインID欄が空欄であること");
		assertEquals("", passwordElement.getAttribute("value"), "パスワード欄が空欄であること");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// DBに登録されていないログインID・パスワードを入力
		WebElement loginIdElement = webDriver.findElement(By.id("loginId"));
		WebElement passwordElement = webDriver.findElement(By.id("password"));
		loginIdElement.sendKeys("unknownUser01");
		passwordElement.sendKeys("unknownUser01");

		// ログインボタンを押下する
		webDriver.findElement(By.className("btn-primary")).click();

		// 他の画面に遷移していないことを確認するため、タイトルを取得
		String title = webDriver.getTitle();

		// 表示されるエラーメッセージを取得
		WebElement errorElement = webDriver.findElement(By.className("help-inline"));

		// 再表示されたログインID欄の要素を取得
		WebElement reloadedLoginIdElement = webDriver.findElement(By.id("loginId"));

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("ログイン | LMS", title, "画面遷移せず、ログイン画面のままであること");
		assertTrue(errorElement.getText().contains("ログインに失敗しました。"), "エラーメッセージ「ログインに失敗しました。」が表示されること");
		assertEquals("unknownUser01", reloadedLoginIdElement.getAttribute("value"), "ログインID欄に入力した値がそのまま表示されていること");
	}

}
