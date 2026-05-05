package com.aimealplan.e2e.navigation;

import com.aimealplan.e2e.BaseE2ETest;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * レシピ管理画面の画面遷移・UI 操作シナリオテスト。
 *
 * <p>現在実装済みの画面（{@code /}）に対して、以下の遷移パターンを検証する。</p>
 * <ul>
 *   <li>初期表示 → レシピ一覧が表示されること</li>
 *   <li>レシピ登録 → 登録後に一覧に反映されること（最重要）</li>
 *   <li>編集ボタン → フォームが編集モードに切り替わること</li>
 *   <li>キャンセルボタン → フォームが登録モードに戻ること</li>
 *   <li>削除ボタン → 一覧から削除されること</li>
 *   <li>検索 → 結果が絞り込まれること</li>
 * </ul>
 *
 * <p><b>テストデータ管理方針</b>: 各テストは API 経由でデータを作成し、
 * {@code @AfterEach} で必ずクリーンアップする。テスト間のデータ混在を防ぐため、
 * タイムスタンプを付与して一意なタイトルを使用する。</p>
 */
@DisplayName("レシピ管理画面 - 画面遷移・UI操作シナリオ")
class RecipeScreenTransitionIT extends BaseE2ETest {

    /** テスト中に作成したレシピの ID（クリーンアップ用） */
    private Long createdRecipeId;
    private Long createdRecipeId2;

    @BeforeEach
    void openTopPage() {
        page.navigate("/");
        page.waitForLoadState();
    }

    @AfterEach
    void cleanupRecipes() {
        for (Long id : new Long[]{createdRecipeId, createdRecipeId2}) {
            if (id != null) {
                try {
                    context.request().delete("/api/recipes/" + id);
                } catch (Exception ignored) {}
            }
        }
        createdRecipeId = null;
        createdRecipeId2 = null;
    }

    // =========================================================
    // 初期表示
    // =========================================================

    @Test
    @DisplayName("初期表示 - レシピ管理ページが表示されること")
    void initialDisplay_pageLoaded() {
        assertThat(page.title()).isEqualTo("レシピ管理 - Vanilla JS");
        assertThat(page.locator("h1").textContent()).isEqualTo("レシピ管理");
    }

    @Test
    @DisplayName("初期表示 - 登録フォームが表示されること")
    void initialDisplay_formVisible() {
        assertThat(page.locator("#recipeForm").isVisible()).isTrue();
        // HTML の初期 h3 テキスト
        assertThat(page.locator("#formTitle").textContent()).isEqualTo("新しいレシピを登録");
        assertThat(page.locator("#submitBtn").textContent()).isEqualTo("登録");
        // キャンセルボタンは初期状態では非表示
        assertThat(page.locator("#cancelBtn").isVisible()).isFalse();
    }

    @Test
    @DisplayName("初期表示 - レシピ一覧テーブルが表示されること")
    void initialDisplay_tableVisible() {
        assertThat(page.locator("table").isVisible()).isTrue();
        assertThat(page.locator("th").nth(0).textContent()).isEqualTo("タイトル");
        assertThat(page.locator("th").nth(1).textContent()).isEqualTo("カロリー");
        assertThat(page.locator("th").nth(2).textContent()).isEqualTo("操作");
    }

    // =========================================================
    // レシピ登録フロー（最重要）
    // =========================================================

    @Test
    @DisplayName("レシピ登録 → 登録後に一覧に料理名が表示されること（最重要）")
    void registerRecipe_appearsInList() {
        String recipeName = "テスト用チキンカレー_" + System.currentTimeMillis();

        page.locator("#title").fill(recipeName);
        page.locator("#description").fill("テスト用の説明");
        page.locator("#calories").fill("500");
        page.locator("#submitBtn").click();

        // 登録後フォームがリセットされること
        // app.js の resetForm() は "レシピを登録" をセットする
        page.waitForFunction("document.getElementById('formTitle').textContent === 'レシピを登録'");
        assertThat(page.locator("#formTitle").textContent()).isEqualTo("レシピを登録");
        assertThat(page.locator("#title").inputValue()).isEmpty();

        // 一覧に登録した料理名が表示されること ← 最重要
        page.waitForFunction(
                "() => document.getElementById('recipeTableBody').textContent.includes('" + recipeName + "')");
        assertThat(page.locator("#recipeTableBody").textContent()).contains(recipeName);

        createdRecipeId = getRecipeIdByTitle(recipeName);
    }

    @Test
    @DisplayName("レシピ登録 → 登録後にカロリーが一覧に表示されること")
    void registerRecipe_caloriesAppearsInList() {
        String recipeName = "カロリー確認レシピ_" + System.currentTimeMillis();

        page.locator("#title").fill(recipeName);
        page.locator("#calories").fill("750");
        page.locator("#submitBtn").click();

        page.waitForFunction(
                "() => document.getElementById('recipeTableBody').textContent.includes('" + recipeName + "')");
        assertThat(page.locator("#recipeTableBody").textContent()).contains("750 kcal");

        createdRecipeId = getRecipeIdByTitle(recipeName);
    }

    @Test
    @DisplayName("レシピ登録 → 登録後にフォームがリセットされること")
    void registerRecipe_formResetAfterSubmit() {
        String recipeName = "フォームリセット確認_" + System.currentTimeMillis();

        page.locator("#title").fill(recipeName);
        page.locator("#description").fill("説明テキスト");
        page.locator("#calories").fill("300");
        page.locator("#submitBtn").click();

        // app.js の resetForm() は "レシピを登録" をセットする
        page.waitForFunction("document.getElementById('formTitle').textContent === 'レシピを登録'");
        assertThat(page.locator("#title").inputValue()).isEmpty();
        assertThat(page.locator("#description").inputValue()).isEmpty();
        assertThat(page.locator("#submitBtn").textContent()).isEqualTo("登録");
        assertThat(page.locator("#cancelBtn").isVisible()).isFalse();

        createdRecipeId = getRecipeIdByTitle(recipeName);
    }

    // =========================================================
    // 編集フロー
    // =========================================================

    @Test
    @DisplayName("編集ボタンクリック → フォームが編集モードに切り替わること")
    void editButton_switchesToEditMode() {
        String recipeName = "編集テスト用レシピ_" + System.currentTimeMillis();
        createdRecipeId = createRecipeViaApi(recipeName, "説明", 400);

        page.reload();
        page.waitForLoadState();

        // タイトルテキストで対象行を特定して編集ボタンをクリック
        page.locator("#recipeTableBody tr:has-text('" + recipeName + "')")
                .locator("button:has-text('編集')")
                .click();

        page.waitForFunction("document.getElementById('formTitle').textContent === 'レシピを編集'");
        assertThat(page.locator("#formTitle").textContent()).isEqualTo("レシピを編集");
        assertThat(page.locator("#submitBtn").textContent()).isEqualTo("更新");
        assertThat(page.locator("#cancelBtn").isVisible()).isTrue();
        assertThat(page.locator("#title").inputValue()).isEqualTo(recipeName);
        assertThat(page.locator("#calories").inputValue()).isEqualTo("400");
    }

    @Test
    @DisplayName("キャンセルボタンクリック → フォームが登録モードに戻ること")
    void cancelButton_switchesBackToRegisterMode() {
        String recipeName = "キャンセルテスト用_" + System.currentTimeMillis();
        createdRecipeId = createRecipeViaApi(recipeName, "説明", 300);

        page.reload();
        page.waitForLoadState();

        // 対象行の編集ボタンをクリック
        page.locator("#recipeTableBody tr:has-text('" + recipeName + "')")
                .locator("button:has-text('編集')")
                .click();

        page.waitForFunction("document.getElementById('formTitle').textContent === 'レシピを編集'");

        // キャンセルボタンをクリック
        page.locator("#cancelBtn").click();

        // app.js の resetForm() は "レシピを登録" をセットする
        page.waitForFunction("document.getElementById('formTitle').textContent === 'レシピを登録'");
        assertThat(page.locator("#formTitle").textContent()).isEqualTo("レシピを登録");
        assertThat(page.locator("#submitBtn").textContent()).isEqualTo("登録");
        assertThat(page.locator("#cancelBtn").isVisible()).isFalse();
        assertThat(page.locator("#title").inputValue()).isEmpty();
    }

    @Test
    @DisplayName("レシピ更新 → 更新後に一覧の表示が変わること")
    void updateRecipe_updatedValueAppearsInList() {
        String originalName = "更新前レシピ_" + System.currentTimeMillis();
        String updatedName  = "更新後レシピ_" + System.currentTimeMillis();
        createdRecipeId = createRecipeViaApi(originalName, "説明", 300);

        page.reload();
        page.waitForLoadState();

        // 対象行の編集ボタンをクリック
        page.locator("#recipeTableBody tr:has-text('" + originalName + "')")
                .locator("button:has-text('編集')")
                .click();

        page.waitForFunction("document.getElementById('formTitle').textContent === 'レシピを編集'");

        // タイトルを変更して更新
        page.locator("#title").fill(updatedName);
        page.locator("#submitBtn").click();

        // 更新後に一覧に新しいタイトルが表示されること ← 最重要
        page.waitForFunction(
                "() => document.getElementById('recipeTableBody').textContent.includes('" + updatedName + "')");
        assertThat(page.locator("#recipeTableBody").textContent()).contains(updatedName);
        assertThat(page.locator("#recipeTableBody").textContent()).doesNotContain(originalName);
    }

    // =========================================================
    // 削除フロー
    // =========================================================

    @Test
    @DisplayName("削除ボタンクリック → 確認ダイアログ後に一覧から削除されること")
    void deleteButton_removedFromList() {
        String recipeName = "削除テスト用レシピ_" + System.currentTimeMillis();
        createRecipeViaApi(recipeName, "説明", 200);
        // 削除後は ID 不要
        createdRecipeId = null;

        page.reload();
        page.waitForLoadState();

        assertThat(page.locator("#recipeTableBody").textContent()).contains(recipeName);

        // ダイアログ承認をクリック前に登録する（タイミング問題を回避）
        page.onDialog(dialog -> dialog.accept());

        // 対象行の削除ボタンをクリック
        page.locator("#recipeTableBody tr:has-text('" + recipeName + "')")
                .locator("button:has-text('削除')")
                .click();

        // 一覧から削除されること
        page.waitForFunction(
                "() => !document.getElementById('recipeTableBody').textContent.includes('" + recipeName + "')",
                null,
                new com.microsoft.playwright.Page.WaitForFunctionOptions().setTimeout(15000));
        assertThat(page.locator("#recipeTableBody").textContent()).doesNotContain(recipeName);
    }

    // =========================================================
    // 検索フロー
    // =========================================================

    @Test
    @DisplayName("キーワード検索 → 一致するレシピのみ表示されること")
    void search_filteredByKeyword() {
        long ts = System.currentTimeMillis();
        createdRecipeId  = createRecipeViaApi("カレーライス_" + ts, "説明", 600);
        createdRecipeId2 = createRecipeViaApi("ラーメン_" + ts, "説明", 500);

        page.reload();
        page.waitForLoadState();

        page.locator("#searchKeyword").fill("カレーライス_" + ts);
        page.locator("#searchBtn").click();

        page.waitForFunction(
                "() => document.getElementById('recipeTableBody').textContent.includes('カレーライス_" + ts + "')");
        assertThat(page.locator("#recipeTableBody").textContent()).contains("カレーライス_" + ts);
        assertThat(page.locator("#recipeTableBody").textContent()).doesNotContain("ラーメン_" + ts);
    }

    @Test
    @DisplayName("検索キーワードをクリア → 全件表示に戻ること")
    void search_clearKeyword_showsAll() {
        long ts = System.currentTimeMillis();
        createdRecipeId  = createRecipeViaApi("カレー_" + ts, "説明", 600);
        createdRecipeId2 = createRecipeViaApi("ラーメン_" + ts, "説明", 500);

        page.reload();
        page.waitForLoadState();

        // 「カレー」で検索
        page.locator("#searchKeyword").fill("カレー_" + ts);
        page.locator("#searchBtn").click();
        page.waitForFunction(
                "() => !document.getElementById('recipeTableBody').textContent.includes('ラーメン_" + ts + "')");

        // キーワードをクリアして再検索 → 全件表示
        page.locator("#searchKeyword").fill("");
        page.locator("#searchBtn").click();
        page.waitForFunction(
                "() => document.getElementById('recipeTableBody').textContent.includes('ラーメン_" + ts + "')");

        assertThat(page.locator("#recipeTableBody").textContent()).contains("カレー_" + ts);
        assertThat(page.locator("#recipeTableBody").textContent()).contains("ラーメン_" + ts);
    }

    // =========================================================
    // プライベートヘルパー
    // =========================================================

    private Long createRecipeViaApi(String title, String description, int calories) {
        try {
            String body = String.format(
                    "{\"title\":\"%s\",\"description\":\"%s\",\"calories\":%d}",
                    title, description, calories);
            var response = context.request().post("/api/recipes",
                    com.microsoft.playwright.options.RequestOptions.create()
                            .setHeader("Content-Type", "application/json")
                            .setData(body));
            var map = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(response.text(), java.util.Map.class);
            return ((Number) map.get("id")).longValue();
        } catch (Exception e) {
            throw new RuntimeException("レシピ作成失敗: " + e.getMessage(), e);
        }
    }

    private Long getRecipeIdByTitle(String title) {
        try {
            var response = context.request().get(
                    "/api/recipes/search?keyword=" + java.net.URLEncoder.encode(title, "UTF-8"));
            var list = new com.fasterxml.jackson.databind.ObjectMapper()
                    .readValue(response.text(), java.util.List.class);
            if (list.isEmpty()) return null;
            return ((Number) ((java.util.Map<?, ?>) list.get(0)).get("id")).longValue();
        } catch (Exception e) {
            return null;
        }
    }
}
