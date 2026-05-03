# メッセージ一覧 (ID 15)

## 1. 管理方針
- **外部化**: `src/main/resources/messages.properties` で一括管理する。
- **命名規則**: `[prefix].[category].[reason]`
    - `error`: バリデーションエラー、業務エラー
    - `info`: 操作成功通知

## 2. メッセージ定義（抜粋）
| キー | 内容 | 備考 |
|:---|:---|:---|
| `error.meal.not_found` | 指定された献立が見つかりません。 | 404 Not Found |
| `error.validation.range` | {0}は{1}から{2}の間で入力してください。 | バリデーション |
| `info.meal.save_success` | 献立を保存しました。 | 登録・更新成功 |
| `info.auth.login_success` | ログインしました。 | 認証成功 |