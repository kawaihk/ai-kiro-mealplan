package com.aimealplan.e2e.helper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.microsoft.playwright.APIRequestContext;
import com.microsoft.playwright.APIResponse;
import com.microsoft.playwright.options.RequestOptions;

import java.util.Map;

/**
 * Playwright の {@link APIRequestContext} を使った REST API 呼び出しの共通ラッパー。
 *
 * <p>各ヘルパークラス（{@link UserHelper}・{@link GoalHelper}・{@link MealHelper}）の
 * 基底となるユーティリティクラス。JSON のシリアライズ・デシリアライズを一元管理する。</p>
 */
public class ApiHelper {

    protected final APIRequestContext request;
    protected static final ObjectMapper MAPPER = new ObjectMapper()
            .registerModule(new JavaTimeModule())
            .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

    public ApiHelper(APIRequestContext request) {
        this.request = request;
    }

    /**
     * POST リクエストを送信し、レスポンスボディを指定型にデシリアライズして返す。
     *
     * @param url    リクエスト先 URL
     * @param body   リクエストボディ（Object → JSON に変換）
     * @param type   レスポンスの型
     * @param <T>    レスポンスの型パラメータ
     * @return デシリアライズされたレスポンスオブジェクト
     */
    public <T> T post(String url, Object body, Class<T> type) {
        try {
            String json = MAPPER.writeValueAsString(body);
            APIResponse response = request.post(url, RequestOptions.create()
                    .setHeader("Content-Type", "application/json")
                    .setData(json));
            assertSuccess(response, url);
            return MAPPER.readValue(response.text(), type);
        } catch (Exception e) {
            throw new RuntimeException("POST " + url + " failed: " + e.getMessage(), e);
        }
    }

    /**
     * GET リクエストを送信し、レスポンスボディを指定型にデシリアライズして返す。
     *
     * @param url  リクエスト先 URL
     * @param type レスポンスの型
     * @param <T>  レスポンスの型パラメータ
     * @return デシリアライズされたレスポンスオブジェクト
     */
    public <T> T get(String url, Class<T> type) {
        try {
            APIResponse response = request.get(url);
            assertSuccess(response, url);
            return MAPPER.readValue(response.text(), type);
        } catch (Exception e) {
            throw new RuntimeException("GET " + url + " failed: " + e.getMessage(), e);
        }
    }

    /**
     * GET リクエストを送信し、レスポンスボディを TypeReference で指定した型にデシリアライズして返す。
     * List 等のジェネリクス型に使用する。
     *
     * @param url      リクエスト先 URL
     * @param typeRef  レスポンスの型参照
     * @param <T>      レスポンスの型パラメータ
     * @return デシリアライズされたレスポンスオブジェクト
     */
    public <T> T get(String url, TypeReference<T> typeRef) {
        try {
            APIResponse response = request.get(url);
            assertSuccess(response, url);
            return MAPPER.readValue(response.text(), typeRef);
        } catch (Exception e) {
            throw new RuntimeException("GET " + url + " failed: " + e.getMessage(), e);
        }
    }

    /**
     * DELETE リクエストを送信する。
     *
     * @param url リクエスト先 URL
     */
    public void delete(String url) {
        try {
            APIResponse response = request.delete(url);
            if (!response.ok() && response.status() != 404) {
                throw new RuntimeException("DELETE " + url + " failed: HTTP " + response.status());
            }
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException("DELETE " + url + " failed: " + e.getMessage(), e);
        }
    }

    /**
     * レスポンスが成功（2xx）でない場合に例外をスローする。
     *
     * @param response レスポンス
     * @param url      リクエスト先 URL（エラーメッセージ用）
     */
    private void assertSuccess(APIResponse response, String url) {
        if (!response.ok()) {
            throw new RuntimeException(
                    "Request failed [" + response.status() + "] " + url + ": " + response.text());
        }
    }

    /**
     * オブジェクトを Map に変換する（デバッグ・ログ用途）。
     *
     * @param obj 変換対象オブジェクト
     * @return Map 表現
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> toMap(Object obj) {
        return MAPPER.convertValue(obj, Map.class);
    }
}
