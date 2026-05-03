# 実装ガイドライン

## 1. 認証・認可 (ID 11)
- **方式**: Spring Security によるフォーム認証およびセッション管理。
- **パスワード**: `BCryptPasswordEncoder` を使用。

## 2. 入力バリデーション (ID 12)
- **サーバー側**: JSR-303 (Hibernate Validator) を使用。
    - 例: `@NotBlank`, `@Size(max=255)`, `@Min(0)`
- **フロント側**: Vanilla JS によるリアルタイムバリデーション（API送信前チェック）。
