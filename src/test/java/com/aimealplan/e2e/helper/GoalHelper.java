package com.aimealplan.e2e.helper;

import com.microsoft.playwright.APIRequestContext;

import java.util.HashMap;
import java.util.Map;

/**
 * ユーザー目標栄養素（PFC）設定の共通ヘルパー。
 *
 * <p>E2E テストでの目標PFC設定・取得・削除を簡潔に記述するためのユーティリティ。</p>
 *
 * <h3>使用例</h3>
 * <pre>{@code
 * GoalHelper goalHelper = new GoalHelper(context.request());
 *
 * // 目標PFC設定（P:30% F:30% C:40%、目標カロリー2000kcal）
 * Map<String, Object> goal = goalHelper.createGoal(userId, 2000, 30, 30, 40);
 *
 * // 目標PFC取得
 * Map<String, Object> fetched = goalHelper.getGoal(userId);
 * }</pre>
 */
public class GoalHelper extends ApiHelper {

    public GoalHelper(APIRequestContext request) {
        super(request);
    }

    /**
     * ユーザーの目標栄養素設定を登録する。
     *
     * @param userId          ユーザー ID
     * @param targetCalories  目標カロリー（kcal）
     * @param proteinRatio    タンパク質比率（%）
     * @param fatRatio        脂質比率（%）
     * @param carbRatio       炭水化物比率（%）
     * @return 登録された目標栄養素情報（Map）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> createGoal(Long userId, int targetCalories,
                                          int proteinRatio, int fatRatio, int carbRatio) {
        Map<String, Object> body = buildGoalBody(userId, targetCalories, proteinRatio, fatRatio, carbRatio);
        return post(goalUrl(userId), body, Map.class);
    }

    /**
     * ユーザーの目標栄養素設定を更新する。
     *
     * @param userId          ユーザー ID
     * @param targetCalories  目標カロリー（kcal）
     * @param proteinRatio    タンパク質比率（%）
     * @param fatRatio        脂質比率（%）
     * @param carbRatio       炭水化物比率（%）
     * @return 更新された目標栄養素情報（Map）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> updateGoal(Long userId, int targetCalories,
                                          int proteinRatio, int fatRatio, int carbRatio) {
        Map<String, Object> body = buildGoalBody(userId, targetCalories, proteinRatio, fatRatio, carbRatio);
        try {
            String json = MAPPER.writeValueAsString(body);
            var response = request.put(goalUrl(userId),
                    com.microsoft.playwright.options.RequestOptions.create()
                            .setHeader("Content-Type", "application/json")
                            .setData(json));
            if (!response.ok()) {
                throw new RuntimeException("PUT " + goalUrl(userId) + " failed: HTTP " + response.status()
                        + " " + response.text());
            }
            return MAPPER.readValue(response.text(), Map.class);
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("updateGoal failed: " + e.getMessage(), e);
        }
    }

    /**
     * ユーザーの目標栄養素設定を取得する。
     *
     * @param userId ユーザー ID
     * @return 目標栄養素情報（Map）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getGoal(Long userId) {
        return get(goalUrl(userId), Map.class);
    }

    /**
     * ユーザーの目標栄養素設定を削除する（テスト後のクリーンアップ用）。
     *
     * @param userId ユーザー ID
     */
    public void deleteGoal(Long userId) {
        if (userId == null) return;
        delete(goalUrl(userId));
    }

    /**
     * デフォルトの目標PFC設定（P:30% F:30% C:40%、2000kcal）を登録する。
     *
     * @param userId ユーザー ID
     * @return 登録された目標栄養素情報（Map）
     */
    public Map<String, Object> createDefaultGoal(Long userId) {
        return createGoal(userId, 2000, 30, 30, 40);
    }

    // --- プライベートヘルパー ---

    private String goalUrl(Long userId) {
        return "/api/users/" + userId + "/goal";
    }

    private Map<String, Object> buildGoalBody(Long userId, int targetCalories,
                                               int proteinRatio, int fatRatio, int carbRatio) {
        Map<String, Object> body = new HashMap<>();
        body.put("userId", userId);
        body.put("targetCalories", targetCalories);
        body.put("proteinRatio", proteinRatio);
        body.put("fatRatio", fatRatio);
        body.put("carbohydrateRatio", carbRatio);
        return body;
    }
}
