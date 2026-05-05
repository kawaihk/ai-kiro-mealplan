package com.aimealplan.e2e.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.microsoft.playwright.APIRequestContext;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ミール（食事）操作の共通ヘルパー。
 *
 * <p>E2E テストでのミール登録・取得・削除を簡潔に記述するためのユーティリティ。
 * ミールは必ず MealPlan に紐づくため、{@code mealPlanId} を引数に取る設計になっている。</p>
 *
 * <p><b>注意</b>: 現在の実装では MealPlan の作成 API が未実装のため、
 * MealPlan は DB に直接データが存在する前提か、将来の MealPlan API 実装後に
 * {@code createMealPlan} メソッドを追加する。</p>
 *
 * <h3>使用例</h3>
 * <pre>{@code
 * MealHelper mealHelper = new MealHelper(context.request());
 *
 * // ミール登録
 * Map<String, Object> meal = mealHelper.createMeal(
 *         mealPlanId, LocalDate.now(), "LUNCH", "チキンカレー", 500, 30);
 * Long mealId = ((Number) meal.get("id")).longValue();
 *
 * // ミール一覧取得
 * List<Map<String, Object>> meals = mealHelper.getMeals(mealPlanId);
 * }</pre>
 */
public class MealHelper extends ApiHelper {

    public MealHelper(APIRequestContext request) {
        super(request);
    }

    /**
     * 指定ミールプランにミールを登録する。
     *
     * @param mealPlanId  ミールプラン ID
     * @param mealDate    食事日
     * @param mealType    食事タイプ（BREAKFAST / LUNCH / DINNER / SNACK）
     * @param recipeName  料理名
     * @param calories    カロリー（kcal）
     * @param protein     タンパク質（g）
     * @return 登録されたミール情報（Map）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> createMeal(Long mealPlanId, LocalDate mealDate,
                                          String mealType, String recipeName,
                                          int calories, int protein) {
        Map<String, Object> body = buildMealBody(mealPlanId, mealDate, mealType, recipeName, calories, protein);
        return post(mealsUrl(mealPlanId), body, Map.class);
    }

    /**
     * 指定ミールプランのミール一覧を取得する。
     *
     * @param mealPlanId ミールプラン ID
     * @return ミール情報のリスト
     */
    public List<Map<String, Object>> getMeals(Long mealPlanId) {
        return get(mealsUrl(mealPlanId), new TypeReference<List<Map<String, Object>>>() {});
    }

    /**
     * 指定ミールプランの特定ミールを取得する。
     *
     * @param mealPlanId ミールプラン ID
     * @param mealId     ミール ID
     * @return ミール情報（Map）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getMeal(Long mealPlanId, Long mealId) {
        return get(mealsUrl(mealPlanId) + "/" + mealId, Map.class);
    }

    /**
     * 指定ミールプランの特定ミールを削除する（テスト後のクリーンアップ用）。
     *
     * @param mealPlanId ミールプラン ID
     * @param mealId     ミール ID
     */
    public void deleteMeal(Long mealPlanId, Long mealId) {
        if (mealPlanId == null || mealId == null) return;
        delete(mealsUrl(mealPlanId) + "/" + mealId);
    }

    /**
     * 本日の昼食としてデフォルトのミールを登録する。
     *
     * @param mealPlanId ミールプラン ID
     * @param recipeName 料理名
     * @return 登録されたミール情報（Map）
     */
    public Map<String, Object> createDefaultMeal(Long mealPlanId, String recipeName) {
        return createMeal(mealPlanId, LocalDate.now(), "LUNCH", recipeName, 500, 30);
    }

    // --- プライベートヘルパー ---

    private String mealsUrl(Long mealPlanId) {
        return "/api/meal-plans/" + mealPlanId + "/meals";
    }

    private Map<String, Object> buildMealBody(Long mealPlanId, LocalDate mealDate,
                                               String mealType, String recipeName,
                                               int calories, int protein) {
        Map<String, Object> body = new HashMap<>();
        body.put("mealPlanId", mealPlanId);
        body.put("mealDate", mealDate.toString());   // "2026-05-05" 形式
        body.put("mealType", mealType);
        body.put("recipeName", recipeName);
        body.put("calories", calories);
        body.put("protein", protein);
        return body;
    }
}
