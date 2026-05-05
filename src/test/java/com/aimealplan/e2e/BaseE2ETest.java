package com.aimealplan.e2e;

import com.aimealplan.e2e.helper.GoalHelper;
import com.aimealplan.e2e.helper.MealHelper;
import com.aimealplan.e2e.helper.UserHelper;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.Browser;
import com.microsoft.playwright.BrowserContext;
import com.microsoft.playwright.BrowserType;
import com.microsoft.playwright.Page;
import com.microsoft.playwright.Playwright;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;

/**
 * Playwright E2E テストの基底クラス。
 *
 * <p>各テストクラスはこのクラスを継承して使用する。
 * Playwright インスタンスはテストクラス単位で共有し、
 * ブラウザコンテキストとページはテストメソッド単位で生成・破棄する。</p>
 *
 * <p>ヘルパークラス（{@link UserHelper}・{@link GoalHelper}・{@link MealHelper}）は
 * テストメソッド単位で初期化され、{@link APIRequestContext} 経由で REST API を呼び出す。</p>
 *
 * <p><b>前提条件</b>: Spring Boot アプリケーションが {@code http://localhost:8080} で
 * 起動済みであること。</p>
 *
 * <p><b>実行方法</b>:
 * <pre>
 *   # 1. Spring Boot アプリを E2E プロファイルで起動（DB 不要・H2 使用）
 *   mvn spring-boot:run -Dspring-boot.run.profiles=e2e
 *
 *   # 2. 別ターミナルで E2E テストを実行
 *   mvn failsafe:integration-test failsafe:verify
 * </pre>
 * </p>
 */
public abstract class BaseE2ETest {

    /** テスト対象アプリのベース URL。システムプロパティ app.base.url で上書き可能。 */
    protected static final String BASE_URL =
            System.getProperty("app.base.url", "http://localhost:8080");

    /** デフォルトのタイムアウト（ミリ秒） */
    protected static final int DEFAULT_TIMEOUT_MS = 10_000;

    protected static Playwright playwright;
    protected static Browser browser;

    protected BrowserContext context;
    protected Page page;

    // --- ヘルパー（テストメソッド単位で初期化） ---
    protected UserHelper userHelper;
    protected GoalHelper goalHelper;
    protected MealHelper mealHelper;

    @BeforeAll
    static void launchBrowser() {
        playwright = Playwright.create();
        browser = playwright.chromium().launch(
                new BrowserType.LaunchOptions()
                        // ローカルデバッグ時は false、CI 環境では true に変更
                        .setHeadless(false)
        );
    }

    @AfterAll
    static void closeBrowser() {
        if (browser != null) {
            browser.close();
        }
        if (playwright != null) {
            playwright.close();
        }
    }

    @BeforeEach
    void createContextAndPage() {
        context = browser.newContext(
                new Browser.NewContextOptions()
                        .setBaseURL(BASE_URL)
        );
        page = context.newPage();
        page.setDefaultTimeout(DEFAULT_TIMEOUT_MS);

        // ヘルパーを APIRequestContext で初期化
        APIRequestContext apiRequest = context.request();
        userHelper = new UserHelper(apiRequest);
        goalHelper = new GoalHelper(apiRequest);
        mealHelper = new MealHelper(apiRequest);
    }

    @AfterEach
    void closeContextAndPage() {
        if (page != null) {
            page.close();
        }
        if (context != null) {
            context.close();
        }
    }
}
