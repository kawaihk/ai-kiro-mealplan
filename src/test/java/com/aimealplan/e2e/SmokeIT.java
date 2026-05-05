package com.aimealplan.e2e;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Playwright E2E スモークテスト。
 *
 * <p>Spring Boot アプリが起動済みの状態で Playwright から接続できることを確認する。
 * Step 1（環境構築）のゴール検証用テストクラス。</p>
 *
 * <p><b>実行手順</b>:
 * <ol>
 *   <li>Spring Boot アプリを E2E プロファイルで起動（DB 不要）:
 *       {@code mvn spring-boot:run -Dspring-boot.run.profiles=e2e}</li>
 *   <li>別ターミナルで E2E テストを実行:
 *       {@code mvn failsafe:integration-test failsafe:verify}</li>
 * </ol>
 * </p>
 */
@DisplayName("スモークテスト: Playwright から Spring Boot アプリへの接続確認")
class SmokeIT extends BaseE2ETest {

    @Test
    @DisplayName("トップページ（/）にアクセスして 200 OK が返ること")
    void topPage_shouldReturn200() {
        // Spring Boot の static/index.html が返ること
        var response = page.navigate("/");

        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo(200);
    }

    @Test
    @DisplayName("トップページのタイトルが正しく表示されること")
    void topPage_shouldHaveCorrectTitle() {
        page.navigate("/");

        String title = page.title();
        // static/index.html の <title> タグの内容と一致すること
        assertThat(title).isEqualTo("レシピ管理 - Vanilla JS");
    }

    @Test
    @DisplayName("トップページに h1 見出しが表示されること")
    void topPage_shouldDisplayHeading() {
        page.navigate("/");

        // static/index.html の <h1>レシピ管理</h1> が表示されること
        String heading = page.locator("h1").textContent();
        assertThat(heading).isEqualTo("レシピ管理");
    }

    @Test
    @DisplayName("レシピ登録フォームが表示されること")
    void topPage_shouldDisplayRecipeForm() {
        page.navigate("/");

        // フォームの主要要素が存在すること
        assertThat(page.locator("#recipeForm").isVisible()).isTrue();
        assertThat(page.locator("#title").isVisible()).isTrue();
        assertThat(page.locator("#calories").isVisible()).isTrue();
        assertThat(page.locator("#submitBtn").isVisible()).isTrue();
    }

    @Test
    @DisplayName("レシピ一覧テーブルが表示されること")
    void topPage_shouldDisplayRecipeTable() {
        page.navigate("/");

        // テーブルヘッダーが存在すること
        assertThat(page.locator("table").isVisible()).isTrue();
        assertThat(page.locator("th").first().textContent()).isEqualTo("タイトル");
    }

    @Test
    @DisplayName("API エンドポイント（/api/recipes）が 200 または 401 を返すこと")
    void apiEndpoint_shouldBeReachable() {
        // Spring Security が有効なため 401 も許容（アプリが起動していることの確認）
        var response = page.navigate("/api/recipes");

        assertThat(response).isNotNull();
        assertThat(response.status()).isIn(200, 401, 403);
    }
}
