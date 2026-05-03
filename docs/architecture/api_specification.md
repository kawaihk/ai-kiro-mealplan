# API 仕様書 (v1.0)

## 1. 共通仕様
- **Base URL**: `/api/v1`
- **Content-Type**: `application/json`
- **認証**: セッションベース (ID 11 参照)

## 2. エンドポイント
| メソッド | パス | 説明 |
|:---|:---|:---|
| POST | `/auth/login` | ログイン |
| GET | `/user/goals` | ユーザーの目標栄養素取得 |
| GET | `/meal-plans` | 期間指定での献立リスト取得 |
| POST | `/meal-plans` | 新規献立登録 |
| PUT | `/meal-plans/{id}` | 献立更新 |
| DELETE | `/meal-plans/{id}` | 献立削除 |

## 3. エラー仕様
詳細については エラー要件 を参照。