package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Set;

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
 * 結合テスト よくある質問機能
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		// 上部メニューの「機能」をクリックしてドロップダウンメニューを開く
		webDriver.findElement(By.linkText("機能")).click();

		// 「ヘルプ」リンクが表示されるまで待機
		visibilityTimeout(By.linkText("ヘルプ"), 5);

		// 「ヘルプ」リンクを押下する
		webDriver.findElement(By.linkText("ヘルプ")).click();

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("ヘルプ | LMS", title, "ヘルプ画面に遷移すること");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		// 遷移前(ヘルプ画面)のウィンドウハンドルを取得
		final String helpWindow = webDriver.getWindowHandle();

		// 「よくある質問」リンクを押下(target="_blank")
		webDriver.findElement(By.linkText("よくある質問")).click();

		// 開いている全てのタブのハンドルを取得
		final Set<String> allWindows = webDriver.getWindowHandles();

		// 取得した全てのタブのハンドルを1つずつ確認
		for (String windowHandle : allWindows) {
			// 元のタブ(ヘルプ画面)のハンドルと一致しない場合に、新しく開いたタブとして判断
			if (!windowHandle.equals(helpWindow)) {
				// 操作対象のウィンドウを新しく開いたタブへと切り替える
				webDriver.switchTo().window(windowHandle);
				// 切り替え後、ループを抜ける
				break;
			}
		}

		// 別タブの画面(よくある質問画面)が表示されるまで待機(基準としてh2要素の表示を設定)
		visibilityTimeout(By.tagName("h2"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("よくある質問 | LMS", title, "よくある質問画面が別タブで開くこと");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		// 「【研修関係】」カテゴリのリンクを押下する
		webDriver.findElement(By.linkText("【研修関係】")).click();

		// 検索結果が表示されるまで待機
		visibilityTimeout(By.cssSelector("[id^='question-h']"), 5);

		// 検索結果として表示されている質問の一覧を取得
		// idが「question-h」から始まる要素(検索結果一覧に表示されている各質問のdl要素)をすべて取得
		final List<WebElement> resultList = webDriver.findElements(By.cssSelector("[id^='question-h']"));

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals(2, resultList.size(), "「【研修関係】」カテゴリの検索結果のみ2件表示されること");
		assertTrue(resultList.get(0).getText().contains("キャンセル料・途中退校について"), "1件目の質問が表示されること");
		assertTrue(resultList.get(1).getText().contains("研修の申し込みはどのようにすれば良いですか？"), "2件目の質問が表示されること");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		// 検索結果1件目の質問をクリックする
		// (補足)findElementは最初に見つかった1つの要素のみを返す
		webDriver.findElement(By.cssSelector("[id^='question-h']")).click();

		// 対応する回答を取得
		final WebElement answerElement = webDriver.findElement(By.cssSelector("[id^='answer-h']"));

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertTrue(answerElement.getText().contains("受講者の退職や解雇等"), "質問をクリックすると回答が表示されること");
	}

}
