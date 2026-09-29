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
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

/**
 * 結合テスト レポート機能
 * ケース09
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース09 受講生 レポート登録 入力チェック")
public class Case09 {

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
	@DisplayName("テスト03 上部メニューの「ようこそ○○さん」リンクからユーザー詳細画面に遷移")
	void test03() {
		// 上部メニューの「ようこそ○○さん」リンクを押下する
		webDriver.findElement(By.cssSelector("a[href='/lms/user/detail']")).click();

		// ユーザー詳細画面が表示されるまで待機(基準として表が表示されるタイミングを設定)
		visibilityTimeout(By.cssSelector(".table-hover"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("ユーザー詳細", title, "ユーザー詳細画面に遷移すること");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 該当レポートの「修正する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		// ユーザー詳細画面に表示されている表を全て取得(「レポート」表は一覧の一番最後に表示される)
		final List<WebElement> tables = webDriver.findElements(By.cssSelector(".table-hover"));
		final WebElement reportListTable = tables.get(tables.size() - 1);

		// 「レポート」表の行を全て取得
		final List<WebElement> rows = reportListTable.findElements(By.cssSelector("tr"));

		// 対象の研修日(2022年10月2日)かつ週報の行から「修正する」ボタンを取得する
		// (同じ日付に日報の行がある場合と区別するため、レポート名の列に「週報」を含む行に絞り込む)
		WebElement editButton = null;
		for (WebElement row : rows) {
			if (row.getText().contains("2022年10月2日") && row.getText().contains("週報")) {
				editButton = row.findElement(By.cssSelector("input[value='修正する']"));
				break;
			}
		}

		// 「修正する」ボタンを押下する
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", editButton);

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
	@DisplayName("テスト05 報告内容を修正して「提出する」ボタンを押下しエラー表示：学習項目が未入力")
	void test05() {
		// 「学習項目」欄を空欄にする
		final WebElement intFieldNameElement = webDriver.findElement(By.id("intFieldName_0"));
		intFieldNameElement.clear();

		// 「理解度」欄で「1」を選択する
		final WebElement intFieldValueElement = webDriver.findElement(By.id("intFieldValue_0"));
		new Select(intFieldValueElement).selectByValue("1");

		// 「提出する」ボタンを押下する
		final WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// レポート登録画面が再表示されるまで待機
		visibilityTimeout(By.cssSelector("[id^='content_']"), 5);

		// タイトルと「学習項目」欄のクラス属性を取得
		final String title = webDriver.getTitle();
		final String intFieldNameClass = webDriver.findElement(By.id("intFieldName_0")).getAttribute("class");

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("レポート登録 | LMS", title, "レポート登録画面が再表示されること");
		assertTrue(intFieldNameClass.contains("errorInput"), "学習項目欄がエラー表示になること");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：理解度が未入力")
	void test06() {
		// 「学習項目」欄に文字列を入力する
		final WebElement intFieldNameElement = webDriver.findElement(By.id("intFieldName_0"));
		intFieldNameElement.clear();
		intFieldNameElement.sendKeys("Java研修");

		// 「理解度」欄を未選択の状態にする
		final WebElement intFieldValueElement = webDriver.findElement(By.id("intFieldValue_0"));
		new Select(intFieldValueElement).selectByValue("");

		// 「提出する」ボタンを押下する
		final WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// レポート登録画面が再表示されるまで待機
		visibilityTimeout(By.cssSelector("[id^='content_']"), 5);

		// タイトルと「理解度」欄のクラス属性を取得
		final String title = webDriver.getTitle();
		final String intFieldValueClass = webDriver.findElement(By.id("intFieldValue_0")).getAttribute("class");

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("レポート登録 | LMS", title, "レポート登録画面が再表示されること");
		assertTrue(intFieldValueClass.contains("errorInput"), "理解度欄がエラー表示になること");
	}

	@Test
	@Order(7)
	@DisplayName("テスト07 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が数値以外")
	void test07() {
		// 「学習項目」「理解度」を正しく入力
		final WebElement intFieldNameElement = webDriver.findElement(By.id("intFieldName_0"));
		intFieldNameElement.clear();
		intFieldNameElement.sendKeys("Java研修");
		new Select(webDriver.findElement(By.id("intFieldValue_0"))).selectByValue("1");

		// 「目標の達成度」欄に数値以外の文字列を入力する
		final WebElement achievementElement = webDriver.findElement(By.id("content_0"));
		achievementElement.clear();
		achievementElement.sendKeys("たくさん");

		// 「所感」欄に文字列を入力する
		final WebElement impressionElement = webDriver.findElement(By.id("content_1"));
		impressionElement.clear();
		impressionElement.sendKeys("本日はテストコード作成演習を行いました。");

		// 「提出する」ボタンを押下する
		final WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// レポート登録画面が再表示されるまで待機
		visibilityTimeout(By.cssSelector("[id^='content_']"), 5);

		// タイトルと「目標の達成度」欄のクラス属性を取得
		final String title = webDriver.getTitle();
		final String achievementClass = webDriver.findElement(By.id("content_0")).getAttribute("class");

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("レポート登録 | LMS", title, "レポート登録画面が再表示されること");
		assertTrue(achievementClass.contains("errorInput"), "目標の達成度欄がエラー表示になること");
	}

	@Test
	@Order(8)
	@DisplayName("テスト08 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度が範囲外")
	void test08() {
		// 「目標の達成度」欄に範囲外(1～10の範囲に対し「11」)の数値を入力する
		final WebElement achievementElement = webDriver.findElement(By.id("content_0"));
		achievementElement.clear();
		achievementElement.sendKeys("11");

		// 「所感」欄に文字列を入力する
		final WebElement impressionElement = webDriver.findElement(By.id("content_1"));
		impressionElement.clear();
		impressionElement.sendKeys("本日はテストコード作成演習を行いました。");

		// 「提出する」ボタンを押下する
		final WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// レポート登録画面が再表示されるまで待機
		visibilityTimeout(By.cssSelector("[id^='content_']"), 5);

		// タイトルと「目標の達成度」欄のクラス属性を取得
		final String title = webDriver.getTitle();
		final String achievementClass = webDriver.findElement(By.id("content_0")).getAttribute("class");

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("レポート登録 | LMS", title, "レポート登録画面が再表示されること");
		assertTrue(achievementClass.contains("errorInput"), "目標の達成度欄がエラー表示になること");
	}

	@Test
	@Order(9)
	@DisplayName("テスト09 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：目標の達成度・所感が未入力")
	void test09() {
		// 「目標の達成度」「所感」欄を空欄にする
		final WebElement achievementElement = webDriver.findElement(By.id("content_0"));
		achievementElement.clear();

		final WebElement impressionElement = webDriver.findElement(By.id("content_1"));
		impressionElement.clear();

		// 「提出する」ボタンを押下する
		final WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// レポート登録画面が再表示されるまで待機
		visibilityTimeout(By.cssSelector("[id^='content_']"), 5);

		// タイトルと各欄のクラス属性を取得
		final String title = webDriver.getTitle();
		final String achievementClass = webDriver.findElement(By.id("content_0")).getAttribute("class");
		final String impressionClass = webDriver.findElement(By.id("content_1")).getAttribute("class");

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("レポート登録 | LMS", title, "レポート登録画面が再表示されること");
		assertTrue(achievementClass.contains("errorInput"), "目標の達成度欄がエラー表示になること");
		assertTrue(impressionClass.contains("errorInput"), "所感欄がエラー表示になること");
	}

	@Test
	@Order(10)
	@DisplayName("テスト10 不適切な内容で修正して「提出する」ボタンを押下しエラー表示：所感・一週間の振り返りが2000文字超")
	void test10() {
		// 2001文字の文字列を用意する
		final String overLengthText = "あ".repeat(2001);

		// 「目標の達成度」欄に正しい数値を入力
		final WebElement achievementElement = webDriver.findElement(By.id("content_0"));
		achievementElement.clear();
		achievementElement.sendKeys("5");

		// 「所感」欄に2001文字を入力
		final WebElement impressionElement = webDriver.findElement(By.id("content_1"));
		impressionElement.clear();
		impressionElement.sendKeys(overLengthText);

		// 「一週間の振り返り」欄に2001文字を入力
		final WebElement reflectionElement = webDriver.findElement(By.id("content_2"));
		reflectionElement.clear();
		reflectionElement.sendKeys(overLengthText);

		// 「提出する」ボタンを押下する
		final WebElement submitButton = webDriver.findElement(By.className("btn-primary"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", submitButton);

		// レポート登録画面が再表示されるまで待機
		visibilityTimeout(By.cssSelector("[id^='content_']"), 5);

		// タイトルと各欄のクラス属性を取得
		final String title = webDriver.getTitle();
		final String impressionClass = webDriver.findElement(By.id("content_1")).getAttribute("class");
		final String reflectionClass = webDriver.findElement(By.id("content_2")).getAttribute("class");

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("レポート登録 | LMS", title, "レポート登録画面が再表示されること");
		assertTrue(impressionClass.contains("errorInput"), "所感欄がエラー表示になること");
		assertTrue(reflectionClass.contains("errorInput"), "一週間の振り返り欄がエラー表示になること");
	}

}
