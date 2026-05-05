package com.aimealplan.e2e.navigation;

import com.aimealplan.e2e.BaseE2ETest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * 画面遷移図（screen_transition_diagram.md）に基づく E2E 画面遷移テスト。
 *
 * <p>S-01（ログイン）→ S-02（カレンダー）→ S-03（献立詳細）→ S-04（設定）の
 * 遷移パターンを検証する。</p>
 *
 * <p><b>現在の実装状況</b>: S-01〜S-04 の画面は未実装のため、
 * 各テストは {@code @Disabled} で無効化している。
 * 画面実装後に {@code @Disabled} を外して有効化すること。</p>
 *
 * <pre>
 * 遷移フロー:
 * [S-01 ログイン] -- 認証成功 --> [S-02 カレンダー]
 *                -- 認証失敗 --> [S-01 ログイン]（エラーメッセージ表示）
 *                                    |
 *                                    +-- 日付選択 --> [S-03 献立詳細・編集]
 *                                    |
 *                                    +-- 設定アイコン --> [S-04 設定・目標画面]
 * </pre>
 */
@DisplayName("画面遷移シナリオ（S-01〜S-04）")
class ScreenTransitionIT extends BaseE2ETest {

    // =========================================================
    // S-01 ログイン画面
    // =========================================================

    @Nested
    @DisplayName("S-01: ログイン画面")
    class LoginScreen {

        @Test
        @Disabled("TODO: S-01 ログイン画面の実装後に有効化する")
        @DisplayName("S-01 初期表示 - ログイン画面が表示されること")
        void loginPage_displayed() {
            // TODO: ログイン画面の URL に遷移
            // page.navigate("/login");
            // assertThat(page.title()).contains("ログイン");
            // assertThat(page.locator("#username")).isVisible();
            // assertThat(page.locator("#password")).isVisible();
            // assertThat(page.locator("#loginBtn")).isVisible();
        }

        @Test
        @Disabled("TODO: S-01 → S-02 遷移の実装後に有効化する")
        @DisplayName("S-01 → S-02: 正しい認証情報でログインするとカレンダー画面に遷移すること")
        void login_success_navigatesToCalendar() {
            // TODO: ログイン操作
            // page.navigate("/login");
            // page.locator("#username").fill("testuser");
            // page.locator("#password").fill("password123");
            // page.locator("#loginBtn").click();
            //
            // // S-02 カレンダー画面に遷移すること
            // page.waitForURL("**/calendar");
            // assertThat(page.url()).contains("/calendar");
            // assertThat(page.locator(".calendar-grid")).isVisible();
        }

        @Test
        @Disabled("TODO: S-01 認証失敗の実装後に有効化する")
        @DisplayName("S-01: 誤った認証情報ではログイン画面に留まりエラーが表示されること")
        void login_failure_staysOnLoginPage() {
            // TODO: 誤った認証情報でログイン
            // page.navigate("/login");
            // page.locator("#username").fill("wronguser");
            // page.locator("#password").fill("wrongpass");
            // page.locator("#loginBtn").click();
            //
            // // S-01 に留まること
            // assertThat(page.url()).contains("/login");
            // assertThat(page.locator(".error-message")).isVisible();
        }
    }

    // =========================================================
    // S-02 カレンダー画面
    // =========================================================

    @Nested
    @DisplayName("S-02: カレンダー画面")
    class CalendarScreen {

        @Test
        @Disabled("TODO: S-02 カレンダー画面の実装後に有効化する")
        @DisplayName("S-02 初期表示 - 週間カレンダーグリッドが表示されること")
        void calendarPage_weeklyGridDisplayed() {
            // TODO: ログイン後にカレンダー画面を表示
            // page.navigate("/calendar");
            //
            // // 7列のグリッドが表示されること
            // assertThat(page.locator(".calendar-grid .day-column")).hasCount(7);
            // // ヘッダーに現在の週が表示されること
            // assertThat(page.locator(".week-header")).isVisible();
        }

        @Test
        @Disabled("TODO: S-02 → S-03 遷移の実装後に有効化する")
        @DisplayName("S-02 → S-03: 日付セルをクリックすると献立詳細画面に遷移すること")
        void calendarPage_clickDate_navigatesToMealDetail() {
            // TODO: 日付セルをクリック
            // page.navigate("/calendar");
            // page.locator(".day-column").first().click();
            //
            // // S-03 献立詳細画面に遷移すること
            // page.waitForURL("**/meal/**");
            // assertThat(page.locator(".meal-detail")).isVisible();
            // // 朝食・昼食・夕食のセクションが表示されること
            // assertThat(page.locator(".meal-section-breakfast")).isVisible();
            // assertThat(page.locator(".meal-section-lunch")).isVisible();
            // assertThat(page.locator(".meal-section-dinner")).isVisible();
        }

        @Test
        @Disabled("TODO: S-02 → S-04 遷移の実装後に有効化する")
        @DisplayName("S-02 → S-04: 設定アイコンをクリックすると設定画面に遷移すること")
        void calendarPage_clickSettings_navigatesToSettings() {
            // TODO: 設定アイコンをクリック
            // page.navigate("/calendar");
            // page.locator(".settings-icon").click();
            //
            // // S-04 設定・目標画面に遷移すること
            // page.waitForURL("**/settings");
            // assertThat(page.locator(".settings-form")).isVisible();
            // assertThat(page.locator("#targetCalories")).isVisible();
        }
    }

    // =========================================================
    // S-03 献立詳細・編集画面
    // =========================================================

    @Nested
    @DisplayName("S-03: 献立詳細・編集画面")
    class MealDetailScreen {

        @Test
        @Disabled("TODO: S-03 献立詳細画面の実装後に有効化する")
        @DisplayName("S-03 → S-02: 献立を登録・保存後にカレンダー画面に戻ること")
        void mealDetail_save_navigatesBackToCalendar() {
            // TODO: 献立を登録して保存
            // page.navigate("/meal/2026-05-05");
            // page.locator(".meal-section-lunch .add-btn").click();
            // page.locator("#recipeName").fill("チキンカレー");
            // page.locator("#calories").fill("500");
            // page.locator("#saveBtn").click();
            //
            // // S-02 カレンダー画面に戻ること
            // page.waitForURL("**/calendar");
            // // カレンダー上の該当日付に料理名が表示されること ← 最重要
            // assertThat(page.locator(".day-column:has-text('5/5')").textContent())
            //         .contains("チキンカレー");
        }

        @Test
        @Disabled("TODO: S-03 献立詳細画面の実装後に有効化する")
        @DisplayName("S-03: 登録した献立のカロリーがカードに表示されること")
        void mealDetail_caloriesDisplayedOnCard() {
            // TODO: カロリー表示の確認
            // page.navigate("/meal/2026-05-05");
            // page.locator(".meal-section-lunch .add-btn").click();
            // page.locator("#recipeName").fill("チキンカレー");
            // page.locator("#calories").fill("500");
            // page.locator("#saveBtn").click();
            //
            // // 献立カードに「500 kcal」が表示されること
            // assertThat(page.locator(".meal-card:has-text('チキンカレー') .calories-badge")
            //         .textContent()).contains("500 kcal");
        }
    }

    // =========================================================
    // S-04 設定・目標画面
    // =========================================================

    @Nested
    @DisplayName("S-04: 設定・目標画面")
    class SettingsScreen {

        @Test
        @Disabled("TODO: S-04 設定画面の実装後に有効化する")
        @DisplayName("S-04: 目標PFCを設定・保存すると値が反映されること")
        void settingsPage_savePfcGoal_valueReflected() {
            // TODO: 目標PFC設定
            // page.navigate("/settings");
            // page.locator("#targetCalories").fill("2000");
            // page.locator("#proteinRatio").fill("30");
            // page.locator("#fatRatio").fill("30");
            // page.locator("#carbRatio").fill("40");
            // page.locator("#saveBtn").click();
            //
            // // 保存後に値が反映されること
            // page.reload();
            // assertThat(page.locator("#targetCalories").inputValue()).isEqualTo("2000");
            // assertThat(page.locator("#proteinRatio").inputValue()).isEqualTo("30");
        }

        @Test
        @Disabled("TODO: S-04 → S-02 遷移の実装後に有効化する")
        @DisplayName("S-04 → S-02: 保存後にカレンダー画面に戻ること")
        void settingsPage_save_navigatesBackToCalendar() {
            // TODO: 設定保存後の遷移確認
            // page.navigate("/settings");
            // page.locator("#saveBtn").click();
            //
            // page.waitForURL("**/calendar");
            // assertThat(page.url()).contains("/calendar");
        }
    }

    // =========================================================
    // 未認証アクセス
    // =========================================================

    @Test
    @Disabled("TODO: 認証機能の実装後に有効化する（SecurityConfig の TODO [SEC-02] 対応後）")
    @DisplayName("未認証状態で保護されたページにアクセスするとログイン画面にリダイレクトされること")
    void unauthenticated_redirectsToLogin() {
        // TODO: 認証機能実装後に有効化
        // page.navigate("/calendar");
        // page.waitForURL("**/login");
        // assertThat(page.url()).contains("/login");
    }
}
