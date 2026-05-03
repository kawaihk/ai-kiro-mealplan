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

## 4. 今後の予定
- Phase 1: Spring Boot Entity および Repository 層の実装。
