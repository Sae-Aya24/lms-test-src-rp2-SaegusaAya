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
 * ケース03
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース03 受講生 ログイン 正常系")
public class Case03 {

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
		// ログインID欄・パスワード欄の要素を取得
		final WebElement loginIdElement = webDriver.findElement(By.id("loginId"));
		final WebElement passwordElement = webDriver.findElement(By.id("password"));

		// エビデンス取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("ログイン | LMS", title, "タイトルが期待値通りであること");
		assertEquals("", loginIdElement.getAttribute("value"), "ログインID欄が空欄であること");
		assertEquals("", passwordElement.getAttribute("value"), "パスワード欄が空欄であること");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// 初回ログイン済みの受講生ユーザーでログイン
		final WebElement loginIdElement = webDriver.findElement(By.id("loginId"));
		final WebElement passwordElement = webDriver.findElement(By.id("password"));
		loginIdElement.sendKeys("StudentAA01");
		passwordElement.sendKeys("StudentAA01test");

		// ログインボタンを押下する
		webDriver.findElement(By.className("btn")).click();

		// コース詳細画面が表示されるまで待機(基準として「すべて開く」ボタンの表示を設定)
		visibilityTimeout(By.id("open-all-panel"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("コース詳細 | LMS", title, "コース詳細画面に遷移すること");

	}

}
