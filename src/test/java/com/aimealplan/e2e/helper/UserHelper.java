package com.aimealplan.e2e.helper;

import com.microsoft.playwright.APIRequestContext;

import java.util.HashMap;
import java.util.Map;

/**
 * ユーザー操作の共通ヘルパー。
 *
 * <p>E2E テストでのユーザー作成・取得・削除を簡潔に記述するためのユーティリティ。
 * 各テストの {@code beforeEach} でテストユーザーを作成し、
 * {@code afterEach} で {@link #deleteUser(Long)} によりクリーンアップする。</p>
 *
 * <h3>使用例</h3>
 * <pre>{@code
 * UserHelper userHelper = new UserHelper(context.request());
 *
 * // テストユーザー作成
 * Map<String, Object> user = userHelper.createUser("testuser", "password123", "test@example.com");
 * Long userId = ((Number) user.get("id")).longValue();
 *
 * // テスト後のクリーンアップ
 * userHelper.deleteUser(userId);
 * }</pre>
 */
public class UserHelper extends ApiHelper {

    private static final String USERS_URL = "/api/users";

    public UserHelper(APIRequestContext request) {
        super(request);
    }

    /**
     * テストユーザーを作成する。
     *
     * @param username ユーザー名
     * @param password パスワード（8文字以上）
     * @param email    メールアドレス
     * @return 作成されたユーザー情報（id・username・email・role を含む Map）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> createUser(String username, String password, String email) {
        Map<String, String> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        body.put("email", email);
        body.put("role", "USER");
        return post(USERS_URL, body, Map.class);
    }

    /**
     * テストユーザーを作成し、ユーザー ID を返す。
     *
     * @param username ユーザー名
     * @param password パスワード（8文字以上）
     * @param email    メールアドレス
     * @return 作成されたユーザーの ID
     */
    public Long createUserAndGetId(String username, String password, String email) {
        Map<String, Object> user = createUser(username, password, email);
        return ((Number) user.get("id")).longValue();
    }

    /**
     * ID を指定してユーザー情報を取得する。
     *
     * @param userId ユーザー ID
     * @return ユーザー情報（id・username・email・role を含む Map）
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getUser(Long userId) {
        return get(USERS_URL + "/" + userId, Map.class);
    }

    /**
     * ID を指定してユーザーを削除する（テスト後のクリーンアップ用）。
     * 存在しない場合は無視する。
     *
     * @param userId 削除対象のユーザー ID
     */
    public void deleteUser(Long userId) {
        if (userId == null) return;
        delete(USERS_URL + "/" + userId);
    }

    /**
     * テスト用のデフォルトユーザーを作成する。
     * ユーザー名・メールアドレスにはタイムスタンプを付与して一意性を確保する。
     *
     * @return 作成されたユーザーの ID
     */
    public Long createDefaultTestUser() {
        long ts = System.currentTimeMillis();
        return createUserAndGetId(
                "testuser_" + ts,
                "password123",
                "test_" + ts + "@example.com"
        );
    }
}
