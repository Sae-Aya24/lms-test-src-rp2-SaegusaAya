package jp.co.sss.lms.ct.f04_attendance;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
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
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * 結合テスト 勤怠管理機能
 * ケース11
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース11 受講生 勤怠直接編集 正常系")
public class Case11 {

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

		// 勤怠管理画面が表示されるまで待機(基準として「勤怠情報を直接編集する」リンクの表示を設定)
		visibilityTimeout(By.linkText("勤怠情報を直接編集する"), 5);

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
	@DisplayName("テスト04 「勤怠情報を直接編集する」リンクから勤怠情報直接変更画面に遷移")
	void test04() {
		// 「勤怠情報を直接編集する」リンクを押下する
		webDriver.findElement(By.linkText("勤怠情報を直接編集する")).click();

		// 勤怠情報直接変更画面が表示されるまで待機(基準として「更新」ボタンの表示を設定)
		visibilityTimeout(By.className("update-button"), 5);

		// タイトルとURLを取得
		// (勤怠管理画面と勤怠情報直接変更画面はタイトルが同じ「勤怠情報変更｜LMS」のため、URLでも画面を確認する)
		final String title = webDriver.getTitle();
		final String url = webDriver.getCurrentUrl();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertEquals("勤怠情報変更｜LMS", title, "タイトルが期待値通りであること");
		assertTrue(url.contains("/attendance/update"), "勤怠情報直接変更画面に遷移すること");
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 すべての研修日程の勤怠情報を正しく更新し勤怠管理画面に遷移")
	void test05() {
		// 「定時」ボタンをすべて押下する(各行の出勤9:00・退勤18:00が自動反映される)
		final List<WebElement> defaultButtons = webDriver.findElements(By.className("default-button"));
		for (WebElement defaultButton : defaultButtons) {
			defaultButton.click();
		}

		// 「更新」ボタンを押下する(テーブルが縦長でスクロールしないとボタンが見えないため、JavaScriptでクリックする)
		final WebElement updateButton = webDriver.findElement(By.className("update-button"));
		((JavascriptExecutor) webDriver).executeScript("arguments[0].click();", updateButton);

		// 「更新します。よろしいですか？」の確認ダイアログで「OK」を選択する
		webDriver.switchTo().alert().accept();

		// 完了メッセージが表示されるまで待機
		visibilityTimeout(By.className("alert-info"), 5);

		// 完了メッセージを取得
		final String message = webDriver.findElement(By.className("alert-info")).getText();
		// 勤怠管理画面にしかない「出勤」ボタンの有無で画面を確認
		// (更新処理はフォワードなので、URLは/attendance/updateのまま変わらない)
		final boolean isDisplayedDetailScreen = !webDriver.findElements(By.cssSelector("input[name='punchIn']"))
				.isEmpty();

		// エビデンスを取得
		getEvidence(new Object() {
		});

		// 期待値と一致するか確認
		assertTrue(message.contains("勤怠情報の登録が完了しました。"), "完了メッセージが表示されること");
		assertTrue(isDisplayedDetailScreen, "勤怠管理画面に遷移すること");
	}

}
