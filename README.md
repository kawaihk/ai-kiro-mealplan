# AI Meal Planner (献立管理アプリ)

AIを活用して1週間の献立計画と栄養バランス（PFC）の管理を支援するアプリケーションです。

## 1. 開発環境・技術スタック

### バックエンド
- **Runtime**: Java / Spring Boot 3.x
- **Database**: PostgreSQL
- **ORM**: Spring Data JPA

### フロントエンド
- **Language**: Vanilla JavaScript (ES6+)
- **Styling**: Tailwind CSS (CDN/Utility-first)
- **Template Engine**: Thymeleaf (予定)

### 開発ツール
- **IDE**: Visual Studio Code
- **CLI**: Git Bash, GitHub CLI (`gh`)
- *GitHub CLI (`gh`) は、[こちら](https://github.com/cli/cli/blob/trunk/docs/install_windows.md#community-unofficials)からMSIインストーラーをダウンロードしてインストールしてください。*
- **Environment**: Windows (改行コード LF 固定設定)

## 2. プロジェクトの進捗状況（要件定義工程まで）

以下の作業を完了し、AI-Nativeな開発フローの土台を構築しました。

1.  **プロジェクト基盤の構築**:
    - AI-Native開発に適したディレクトリ構造（`docs`, `mocks`, `src`, `tests`, `.ai`）の作成。
    - 改行コード（LF）をプロジェクト全体で統一するための `.gitattributes` の設定。
2.  **モック・プロトタイプ作成**:
    - 献立データの基本構造を定義した `meal-plan.json` の作成。
    - UIの骨組みを定義したワイヤーフレーム (`wireframe.md`) の作成。
    - Vanilla JS による動的な週間ビュー・プロトタイプの作成。
3.  **アーキテクチャ設計**:
    - DDD（ドメイン駆動設計）に基づくドメイン定義の洗練（マルチユーザー・目標管理対応）。
    - PostgreSQL 用のテーブル定義 DDL の作成（CHECK制約、自動更新トリガー適用済み）。
4.  **要件定義の確定**:
    - マルチユーザーおよび目標栄養素管理を含む要件の具体化。
5.  **タスク・Issue管理の自動化**:
    - セルフレビュー (REV-20260429-01) の実施と、指摘事項（ID 01-09）の設計反映完了。

**ディレクトリ構成ルールの遵守確認**:
- すべてのレビューファイル (`REV-*.md`) を `docs/reviews/` に集約しました。
- ルート直下および `.ai/instructions/` 配下の誤配置ファイルを削除しました。
 - 誤った階層に作成されたエンティティおよびリポジトリファイルを正規のディレクトリ構成に再配置しました。
 - プロジェクト構成定義の監査と修正を実施。DTOの命名明確化、テスト階層の対称化、および静的リソース配置の適正化を完了しました。

## 3. ディレクトリ構成
Spring Bootアプリケーションの標準的なディレクトリ構成に準拠し、エンティティおよびリポジトリを適切なパッケージに配置しました。
 
```text
├── .ai/                # AIへの指示書・ログ管理
├── docs/               # 要件定義・アーキテクチャ設計・Issue管理
├── mocks/              # プロトタイプ、ワイヤーフレーム、モックデータ
├── src/                # 実装コード
│   └── main/java/com/aimealplan/
│       ├── entity/     # エンティティクラス
│       └── repository/ # リポジトリインターフェース
├── scripts/            # 開発支援スクリプト
└── README.md           # プロジェクト概要（本ファイル）
```

## 4. 製造・単体試験（2026-05-03 〜 2026-05-04）

### 4-1. 製造概要

Spring Boot 3.x をベースとした REST API の全レイヤーを実装しました。

#### 実装済みエンドポイント

| リソース | パス | メソッド |
|:---|:---|:---|
| レシピ | `/api/recipes` | GET / POST / PUT / DELETE / GET(search) |
| ミール | `/api/meal-plans/{mealPlanId}/meals` | GET / POST / PUT / DELETE |
| ユーザー | `/api/users` | GET / POST / PUT / DELETE |
| ユーザー目標 | `/api/users/{userId}/goal` | GET / POST / PUT / DELETE |

#### 主な実装内容

- **エンティティ層**: `User` / `MealPlan` / `Meal` / `Recipe` / `UserGoal`（JPA リレーション・`@CreationTimestamp` / `@UpdateTimestamp` 設定済み）
- **DTO 層**: `RecipeDto` / `MealDto` / `UserDto` / `UserGoalDto` / `UserCreateRequest` / `UserUpdateRequest`（バリデーションアノテーション付き）
- **サービス層**: 全4サービスの実装（DTO↔Entity 変換・`ResourceNotFoundException` / `DuplicateResourceException` による例外処理・`@Transactional` 制御）
- **例外ハンドリング**: `GlobalExceptionHandler` による統一エラーレスポンス（404 / 409 / 400 / 500）
- **セキュリティ**: `BCryptPasswordEncoder` によるパスワードハッシュ化・`SecurityConfig` 設定（TODO コメントによる本番前対応事項の明示）
- **バリデーション**: `@PfcRatioSum` カスタムアノテーション（PFC比率合計100%チェック）・`UserUpdateRequest` / `UserCreateRequest` の `@Pattern` によるロール値検証
- **フロントエンド**: ES Modules 構成（`constants.js` / `apiClient.js` / `app.js`）・XSS 対策（`escapeHtml`）・null ガード

#### コードレビュー実施記録

| レビューID | 対象 | 主な指摘・対応内容 |
|:---|:---|:---|
| REV-20260503-01 | src/main 初回レビュー | 空実装クラスの実装・コンパイルエラー解消・app.js 構造バグ修正（24件） |
| REV-20260503-02 | src/main 再レビュー | DTO 導入・パスワードハッシュ化・バリデーション強化・フロントエンドモジュール化（13件） |
| REV-20260504-01 | src/main 第3回レビュー | ユーザー重複チェック・Meal 所属チェック・UserGoal JPA リレーション設定（16件） |
| REV-20260504-02 | src/main 第4回レビュー | ロール変換共通化・`@ResponseStatus` 二重定義解消・`@Pattern` 追加（12件） |

---

### 4-2. 単体試験概要

JUnit 5 + Mockito + MockMvc を使用した単体テストを実装しました。

#### テスト構成

| テスト種別 | クラス数 | 主な内容 |
|:---|:---:|:---|
| コントローラテスト（`@WebMvcTest`） | 4 | 正常系・異常系・バリデーション・例外ハンドリング |
| サービス実装テスト（Mockito） | 4 | CRUD・所属チェック・重複チェック・例外スロー |
| エンティティテスト | 5 | Lombok アノテーション動作・循環参照除外確認 |
| バリデーションテスト | 2 | `PfcRatioSumValidator`・`@PfcRatioSum` メタアノテーション |
| 例外ハンドラーテスト | 1 | 全ハンドラーの HTTP ステータス・レスポンスボディ検証 |

#### テストレビュー実施記録

| レビューID | 対象 | 主な指摘・対応内容 |
|:---|:---|:---|
| REV-20260504-03 | src/test レビュー | コンパイルエラー解消・空テストクラスへの実装・スタブ検証強化（15件） |

---

### 4-3. カバレッジ計測結果（2026-05-04 時点）

JaCoCo によるカバレッジ計測を実施しました（`lombok.config` による Lombok 生成コード除外・`AiMealPlanApplication` 除外設定済み）。

| 指標 | ビジネスロジック対象 | 全体（除外設定対象含む） |
|:---|:---:|:---:|
| 命令カバレッジ | **98.2%** | 91.5% |
| 分岐カバレッジ | **92.0%** | 87.2% |
| 行カバレッジ | — | 92.0% |
| メソッドカバレッジ | — | 92.5% |

コントローラ全4クラス・`RecipeServiceImpl`・`UserGoalServiceImpl`・`PfcRatioSumValidator` は **100%** を達成。

カバレッジ改善計画は `docs/reviews/REV-20260504-04.md` で管理しています。



## 5. 今後の予定

- 認証・認可機能の実装（`SecurityConfig` の `TODO [SEC-01]` / `TODO [SEC-02]` 対応）
- `MealPlan` コントローラ・サービスの実装
- 統合テスト・E2E テストの追加
- カバレッジ残課題（`UserServiceImpl` 91.5%）の解消
