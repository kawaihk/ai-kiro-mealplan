/**
 * fetch の共通ラッパー
 * - Content-Type: application/json を自動付与
 * - HTTP エラー（4xx / 5xx）を例外としてスロー
 */

/**
 * GET リクエストを送信し、レスポンス JSON を返す。
 *
 * @param {string} url - リクエスト先 URL
 * @returns {Promise<any>} レスポンス JSON
 */
export async function get(url) {
    const response = await fetch(url);
    if (!response.ok) {
        const error = await response.json().catch(() => ({}));
        throw Object.assign(new Error(error.message || 'リクエストに失敗しました'), { data: error });
    }
    return response.json();
}

/**
 * POST リクエストを送信し、レスポンス JSON を返す。
 *
 * @param {string} url - リクエスト先 URL
 * @param {object} body - リクエストボディ
 * @returns {Promise<any>} レスポンス JSON
 */
export async function post(url, body) {
    const response = await fetch(url, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
    });
    if (!response.ok) {
        const error = await response.json().catch(() => ({}));
        throw Object.assign(new Error(error.message || 'リクエストに失敗しました'), { data: error });
    }
    return response.json();
}

/**
 * PUT リクエストを送信し、レスポンス JSON を返す。
 *
 * @param {string} url - リクエスト先 URL
 * @param {object} body - リクエストボディ
 * @returns {Promise<any>} レスポンス JSON
 */
export async function put(url, body) {
    const response = await fetch(url, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(body),
    });
    if (!response.ok) {
        const error = await response.json().catch(() => ({}));
        throw Object.assign(new Error(error.message || 'リクエストに失敗しました'), { data: error });
    }
    return response.json();
}

/**
 * DELETE リクエストを送信する。
 *
 * @param {string} url - リクエスト先 URL
 * @returns {Promise<void>}
 */
export async function del(url) {
    const response = await fetch(url, { method: 'DELETE' });
    if (!response.ok) {
        const error = await response.json().catch(() => ({}));
        throw Object.assign(new Error(error.message || 'リクエストに失敗しました'), { data: error });
    }
}
