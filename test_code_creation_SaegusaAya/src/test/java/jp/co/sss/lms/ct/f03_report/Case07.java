package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

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
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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

	@Test
	@Order(3)
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		// コース詳細画面に表示されている研修日の行を全て取得
		final List<WebElement> rows = webDriver.findElements(By.cssSelector(".sctionList tr"));

		// 「未提出」の表示がある行から「詳細」ボタンを取得する
		// 「詳細」ボタンを入れておく変数としてdetailButtonを用意
		WebElement detailButton = null;
		for (WebElement row : rows) {
			if (row.getText().contains("未提出")) {
				detailButton = row.findElement(By.cssSelector("input[value='詳細']"));
				break;
			}
		}

		// 「詳細」ボタンを押下する
		detailButton.click();

		// セクション詳細画面が表示されるまで待機(基準としてsection要素の表示を設定)
		visibilityTimeout(By.id("section"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("セクション詳細 | LMS", title, "セクション詳細画面に遷移すること");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// 「(レポート名)を提出する」ボタンを取得
		final WebElement submitButton = webDriver.findElement(By.cssSelector("input[value*='を提出する']"));

		// 「(レポート名)を提出する」ボタンを押下する
		submitButton.click();

		// レポート登録画面が表示されるまで待機(基準として報告内容欄の表示を設定)
		visibilityTimeout(By.cssSelector("[id^='content_']"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("レポート登録 | LMS", title, "レポート登録画面に遷移すること");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		// 報告内容欄に入力値を入力する
		// 入力値：本日はテストコード作成演習を行いました。
		webDriver.findElement(By.cssSelector("[id^='content_']")).sendKeys("本日はテストコード作成演習を行いました。");

		// 「提出する」ボタンを押下する
		webDriver.findElement(By.className("btn-primary")).click();

		// セクション詳細画面に戻るまで待機(基準としてsection要素の表示を設定)
		visibilityTimeout(By.id("section"), 5);

		// 更新後の「本日のレポート」欄のボタンを取得
		final WebElement submitButton = webDriver.findElement(By.cssSelector("input[value*='を確認する']"));

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertTrue(submitButton.getAttribute("value").startsWith("提出済み"), "ボタン名が「提出済み(レポート名)を確認する」に更新されること");
	}

}
