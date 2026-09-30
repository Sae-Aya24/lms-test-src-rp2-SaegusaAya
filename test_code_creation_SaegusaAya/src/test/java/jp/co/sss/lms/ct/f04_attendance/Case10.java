package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト 勤怠管理機能
 * ケース10
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース10 受講生 勤怠登録 正常系")
public class Case10 {

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
	@DisplayName("テスト03 上部メニューの「勤怠」リンクから勤怠管理画面に遷移")
	void test03() {
		// 上部メニューの「勤怠」リンクを押下する
		webDriver.findElement(By.linkText("勤怠")).click();

		// 過去日の勤怠未入力がある場合、画面表示時に警告アラートが出るため
		// 表示されていれば閉じる
		try {
			new WebDriverWait(webDriver, Duration.ofSeconds(3)).until(ExpectedConditions.alertIsPresent());
			webDriver.switchTo().alert().accept();
		} catch (TimeoutException e) {
			// アラートが表示されない場合は何もしない
		}

		// 勤怠管理画面が表示されるまで待機(基準として「出勤」ボタンの表示を設定)
		visibilityTimeout(By.cssSelector("input[name='punchIn']"), 5);

		// タイトルを取得
		final String title = webDriver.getTitle();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("勤怠情報変更｜LMS", title, "勤怠管理画面に遷移すること");

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「出勤」ボタンを押下し出勤時間を登録")
	void test04() {
		// 「出勤」ボタンを押下する
		webDriver.findElement(By.cssSelector("input[name='punchIn']")).click();

		// 「打刻します。よろしいですか？」の確認ダイアログで「OK」を選択する
		webDriver.switchTo().alert().accept();

		// 出勤登録後、画面再表示時に過去日の勤怠未入力の警告アラートが出ることがあるため、表示されていれば閉じる
		try {
			new WebDriverWait(webDriver, Duration.ofSeconds(3)).until(ExpectedConditions.alertIsPresent());
			webDriver.switchTo().alert().accept();
		} catch (TimeoutException e) {
			// アラートが表示されない場合は何もしない
		}

		// 完了メッセージが表示されるまで待機
		visibilityTimeout(By.className("alert-info"), 5);

		// 完了メッセージと、本日の行の出勤時刻を取得
		final String message = webDriver.findElement(By.className("alert-info")).getText();
		final String startTime = webDriver.findElement(By.cssSelector("tr.info td:nth-child(3)")).getText();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertTrue(message.contains("勤怠情報の登録が完了しました。"), "完了メッセージが表示されること");
		assertTrue(!startTime.isEmpty(), "本日の行に出勤時刻が登録されること");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 「退勤」ボタンを押下し退勤時間を登録")
	void test05() {
		// 「退勤」ボタンを押下する
		webDriver.findElement(By.cssSelector("input[name='punchOut']")).click();

		// 「打刻します。よろしいですか？」の確認ダイアログで「OK」を選択する
		webDriver.switchTo().alert().accept();

		// 退勤登録後、画面再表示時に過去日の勤怠未入力の警告アラートが出ることがあるため、表示されていれば閉じる
		try {
			new WebDriverWait(webDriver, Duration.ofSeconds(3)).until(ExpectedConditions.alertIsPresent());
			webDriver.switchTo().alert().accept();
		} catch (TimeoutException e) {
			// アラートが表示されない場合は何もしない
		}

		// 完了メッセージが表示されるまで待機
		visibilityTimeout(By.className("alert-info"), 5);

		// 完了メッセージと、本日の行の退勤時刻を取得
		final String message = webDriver.findElement(By.className("alert-info")).getText();
		final String endTime = webDriver.findElement(By.cssSelector("tr.info td:nth-child(4)")).getText();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertTrue(message.contains("勤怠情報の登録が完了しました。"), "完了メッセージが表示されること");
		assertTrue(!endTime.isEmpty(), "本日の行に退勤時刻が登録されること");
	}

}
