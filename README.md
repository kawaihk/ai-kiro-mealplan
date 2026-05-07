# AI Meal Planner (献立管理アプリ)

AIを活用して1週間の献立計画と栄養バランス（PFC）の管理を支援するアプリケーションです。

## 1. 開発環境・技術スタック

### バックエンド
- **Runtime**: Java / Spring Boot 3.x
- **Database**: PostgreSQL（本番）/ H2 インメモリ（E2E テスト用）
- **ORM**: Spring Data JPA

### フロントエンド
- **Language**: Vanilla JavaScript (ES6+)
- **Styling**: CSS カスタムプロパティ
- **Template Engine**: Thymeleaf (予定)

### 開発ツール
- **IDE**: Visual Studio Code
- **CLI**: Git Bash, GitHub CLI (`gh`)
- *GitHub CLI (`gh`) は、[こちら](https://github.com/cli/cli/blob/trunk/docs/install_windows.md#community-unofficials)からMSIインストーラーをダウンロードしてインストールしてください。*
- **Environment**: Windows (改行コード LF 固定設定)

---

## 2. プロジェクトの進捗状況（要件定義工程まで）

以下の作業を完了し、AI-Nativeな開発フローの土台を構築しました。

1. **プロジェクト基盤の構築**: ディレクトリ構造・`.gitattributes` の設定
2. **モック・プロトタイプ作成**: `meal-plan.json`・ワイヤーフレーム・Vanilla JS プロトタイプ
3. **アーキテクチャ設計**: DDD ドメイン定義・PostgreSQL テーブル定義 DDL
4. **要件定義の確定**: マルチユーザー・目標栄養素管理を含む要件の具体化
5. **タスク・Issue管理**: セルフレビュー (REV-20260429-01) の実施と設計反映

---

## 3. 製造・単体試験（2026-05-03 〜 2026-05-04）

### 3-1. 製造概要

Spring Boot 3.x をベースとした REST API の全レイヤーを実装しました。

#### 実装済みエンドポイント

| リソース | パス | メソッド |
|:---|:---|:---|
| レシピ | `/api/recipes` | GET / POST / PUT / DELETE / GET(search) |
| ミール | `/api/meal-plans/{mealPlanId}/meals` | GET / POST / PUT / DELETE |
| ユーザー | `/api/users` | GET / POST / PUT / DELETE |
| ユーザー目標 | `/api/users/{userId}/goal` | GET / POST / PUT / DELETE |

#### 主な実装内容

- **エンティティ層**: `User` / `MealPlan` / `Meal` / `Recipe` / `UserGoal`（JPA リレーション・タイムスタンプ自動設定）
- **DTO 層**: 各リソースの Request / Response DTO（バリデーションアノテーション付き）
- **サービス層**: 全4サービスの実装（DTO↔Entity 変換・例外処理・`@Transactional` 制御）
- **例外ハンドリング**: `GlobalExceptionHandler` による統一エラーレスポンス（404 / 409 / 400 / 500）
- **セキュリティ**: `BCryptPasswordEncoder` によるパスワードハッシュ化・`SecurityConfig` 設定
- **バリデーション**: `@PfcRatioSum` カスタムアノテーション（PFC比率合計100%チェック）

#### コードレビュー実施記録

| レビューID | 主な対応内容 |
|:---|:---|
| REV-20260503-01 | 空実装クラスの実装・コンパイルエラー解消・app.js 構造バグ修正（24件） |
| REV-20260503-02 | DTO 導入・パスワードハッシュ化・バリデーション強化（13件） |
| REV-20260504-01 | ユーザー重複チェック・Meal 所属チェック・UserGoal JPA リレーション設定（16件） |
| REV-20260504-02 | ロール変換共通化・`@ResponseStatus` 二重定義解消（12件） |

### 3-2. 単体試験概要

JUnit 5 + Mockito + MockMvc を使用した単体テストを実装しました。

| テスト種別 | クラス数 | 主な内容 |
|:---|:---:|:---|
| コントローラテスト（`@WebMvcTest`） | 4 | 正常系・異常系・バリデーション・例外ハンドリング |
| サービス実装テスト（Mockito） | 4 | CRUD・所属チェック・重複チェック・例外スロー |
| エンティティテスト | 5 | Lombok アノテーション動作・循環参照除外確認 |
| バリデーションテスト | 2 | `PfcRatioSumValidator`・`@PfcRatioSum` メタアノテーション |
| 例外ハンドラーテスト | 1 | 全ハンドラーの HTTP ステータス・レスポンスボディ検証 |

### 3-3. カバレッジ計測結果（2026-05-04 時点）

JaCoCo によるカバレッジ計測を実施（Lombok 生成コード・起動クラス除外済み）。

| 指標 | ビジネスロジック対象 | 全体 |
|:---|:---:|:---:|
| 命令カバレッジ | **98.2%** | 91.5% |
| 分岐カバレッジ | **92.0%** | 87.2% |

コントローラ全4クラス・`RecipeServiceImpl`・`UserGoalServiceImpl`・`PfcRatioSumValidator` は **100%** 達成。

---

## 4. IT試験（2026-05-05）

### 4-1. 概要

Playwright Java を使用した E2E 試験（IT試験）を実装しました。ユーザーの実際の UI 操作フローと画面遷移の正確性を最優先に検証します。

### 4-2. 環境構築

| 項目 | 内容 |
|:---|:---|
| E2E フレームワーク | Playwright Java 1.58.0 |
| テスト実行プラグイン | maven-failsafe-plugin |
| テスト用 DB | H2 インメモリ DB（PostgreSQL 不要） |
| ブラウザ | Chromium（headless 切替可能） |

#### アプリ起動方法（E2E テスト用）

```bash
# ターミナル 1: H2 で Spring Boot を起動（PostgreSQL 不要）
mvn spring-boot:run -Dspring-boot.run.profiles=e2e

# ターミナル 2: E2E テストを実行
mvn failsafe:integration-test failsafe:verify

# 特定クラスのみ実行
mvn failsafe:integration-test failsafe:verify -Dit.test=RecipeScreenTransitionIT
```

### 4-3. テスト構成

```text
src/test/java/com/aimealplan/e2e/
├── BaseE2ETest.java              # 共通基底クラス（Playwright 初期化・ヘルパー注入）
├── SmokeIT.java                  # スモークテスト（アプリ起動・接続確認）
├── helper/
│   ├── ApiHelper.java            # REST API 呼び出し共通ラッパー
│   ├── UserHelper.java           # ユーザー作成・削除
│   ├── GoalHelper.java           # 目標PFC設定・取得
│   └── MealHelper.java           # ミール登録・取得
└── navigation/
    ├── RecipeScreenTransitionIT.java  # レシピ管理画面の画面遷移・UI操作（実装済み）
    └── ScreenTransitionIT.java        # S-01〜S-04 画面遷移（画面実装後に有効化）
```

### 4-4. 実装済みテスト一覧

#### SmokeIT（6件）— アプリ起動・接続確認

| テスト | 内容 |
|:---|:---|
| トップページ接続 | HTTP 200 が返ること |
| タイトル確認 | `<title>` が正しく表示されること |
| 見出し確認 | `<h1>` が表示されること |
| フォーム表示 | 登録フォームの各要素が表示されること |
| テーブル表示 | レシピ一覧テーブルが表示されること |
| API 疎通確認 | `/api/recipes` が到達可能であること |

#### RecipeScreenTransitionIT（12件）— 画面遷移・UI操作

| カテゴリ | テスト内容 |
|:---|:---|
| 初期表示 | ページタイトル・フォーム・テーブルの表示確認 |
| 登録フロー 🔴最重要 | 登録後に一覧に料理名が表示されること・カロリー表示・フォームリセット |
| 編集フロー | 編集モード切り替え・キャンセルで登録モードに戻る・更新後に一覧が変わること |
| 削除フロー | 確認ダイアログ後に一覧から削除されること |
| 検索フロー | キーワードで絞り込み・クリアで全件表示に戻ること |

#### ScreenTransitionIT（12件・`@Disabled`）— 将来の画面実装後に有効化

S-01（ログイン）→ S-02（カレンダー）→ S-03（献立詳細）→ S-04（設定）の画面遷移テストを骨格として用意。各画面の実装後に `@Disabled` を外して有効化します。

### 4-5. 今後の IT試験拡張予定

- Step 4: 表示値の正確性検証（`display/` ディレクトリ）
- Step 5: 業務フローシナリオ（`scenarios/` ディレクトリ）
- Step 6: 異常系・バリデーション（`error/` ディレクトリ）
- S-01〜S-04 画面実装後に `ScreenTransitionIT` を有効化

---

## 5. Docker Compose によるIT試験環境（WSL2/Ubuntu）

### 5-1. 概要

毎回の環境構築を不要にするため、Docker Compose で PostgreSQL + Spring Boot アプリを一括起動できる環境を整備しています。
実行環境は **WSL2（Ubuntu）上の Docker** を前提とします（Docker Desktop は不要）。

| コンテナ | イメージ | 役割 |
|:---|:---|:---|
| `aimealplan-db` | `postgres:16-alpine` | IT試験用 PostgreSQL |
| `aimealplan-app` | `Dockerfile`（マルチステージビルド） | Spring Boot アプリ（`it` プロファイル） |

### 5-2. WSL2/Ubuntu セットアップ（初回のみ）

> **前提**: Windows 11 で WSL2 が有効化済みであること。

#### 推奨構成

```
Windows側
└── Kiro IDE（GUI・編集・エージェント）
        ↓ kiro . で起動
WSL2 Ubuntu-24.04側
├── ソースコード（/home/yourname/projects/ai-mealplan）
├── Docker（docker compose up/down）
├── Maven（mvn failsafe:integration-test）
└── ターミナル（Kiro IDEの統合ターミナル）
```

| 観点 | 理由 |
|:---|:---|
| ファイルシステム | ソースコードをWSL2側（`/home/`）に置くことでDockerのボリュームマウントが高速 |
| Docker | Windows側のDocker Desktopが不要。WSL2ネイティブのDockerで動作 |
| Kiro IDE | Windows側で動くためGUIが安定。`kiro .` でWSL2のプロジェクトをそのまま開ける |
| ターミナル | KiroのターミナルがUbuntu環境なので `docker compose` や `mvn` をそのまま実行できる |

> **注意**: ソースコードは必ずWSL2側（`/home/yourname/`）に置いてください。Windows側（`/mnt/c/`）に置くとDockerのボリュームマウントが著しく遅くなります。

#### Step 1: Ubuntu 24.04 のインストール

Kiro IDE（GUI）を使用する場合は Ubuntu 24.04 以上が必要です。既存の Ubuntu が 22.04 の場合は、新しいディストリビューションを追加インストールします。

**Windows側のPowerShellで実行:**

```powershell
# WSL を最新版に更新
wsl --update

# Ubuntu 24.04 をインストール
wsl --install -d Ubuntu-24.04

# インストール後、ユーザー名とパスワードを設定するプロンプトが表示される
# 設定完了後、以下で起動
wsl -d Ubuntu-24.04

# デフォルトのディストリビューションに設定する場合（オプション）
wsl --set-default Ubuntu-24.04
```

**インストール後、デフォルトのディストリビューションを切り替える:**

```powershell
# インストール済みディストリビューションの一覧確認（* がデフォルト）
wsl -l -v
# 出力例:
#   NAME               STATE           VERSION
# * Ubuntu-22.04LTS    Stopped         2
#   Ubuntu-24.04       Stopped         2
#   docker-desktop     Stopped         2

# デフォルトを Ubuntu-24.04 に変更
wsl --set-default Ubuntu-24.04

# 変更後の確認（* が Ubuntu-24.04 に移動していればOK）
wsl -l -v
# 出力例:
#   NAME               STATE           VERSION
# * Ubuntu-24.04       Stopped         2
#   Ubuntu-22.04LTS    Stopped         2
#   docker-desktop     Stopped         2

# Ubuntu-24.04 を起動
wsl
```

**Ubuntu 24.04 が起動したら、バージョン確認:**

```bash
lsb_release -a
# Ubuntu 24.04.x LTS が表示されればOK

ldd --version
# glibc 2.39 以上であることを確認
```

#### Step 2: Docker のインストール（WSL2 ネイティブ）

Docker Desktop は不要です。Ubuntu-24.04 内に直接 Docker をインストールします。

> **Docker Desktop を使っている場合**: Windows の「設定」→「アプリ」→「Docker Desktop」→「アンインストール」で削除してから進めてください。WSL2 側の Docker は独立して動作するため影響ありません。

**① Docker の公式リポジトリを登録:**

```bash
sudo apt-get update
sudo apt-get install -y ca-certificates curl gnupg lsb-release

sudo install -m 0755 -d /etc/apt/keyrings
curl -fsSL https://download.docker.com/linux/ubuntu/gpg \
  | sudo gpg --dearmor -o /etc/apt/keyrings/docker.gpg
sudo chmod a+r /etc/apt/keyrings/docker.gpg

echo "deb [arch=$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.gpg] \
  https://download.docker.com/linux/ubuntu $(lsb_release -cs) stable" \
  | sudo tee /etc/apt/sources.list.d/docker.list > /dev/null
```

**② Docker エンジンをインストール:**

```bash
sudo apt-get update
sudo apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin
```

**③ sudo なしで実行できるようにユーザーをグループに追加:**

```bash
sudo usermod -aG docker $USER
```

グループ変更を反映するため、一度 WSL2 を再起動します。

```powershell
# PowerShell で実行
wsl --shutdown
wsl -d Ubuntu-24.04
```

**④ Docker デーモンを起動:**

```bash
sudo service docker start
```

**⑤ 動作確認:**

```bash
docker --version
# Docker version 27.x.x 等が表示されればOK

docker compose version
# Docker Compose version v2.x.x 等が表示されればOK

# Hello World で疎通確認
docker run --rm hello-world
```

**⑥ WSL2 起動時に Docker を自動起動する設定:**

```bash
echo 'sudo service docker start > /dev/null 2>&1' >> ~/.bashrc
```

> **補足**: WSL2 では systemd がデフォルトで無効なため、`sudo service docker start` で手動起動が必要です。上記の設定でターミナルを開くたびに自動起動します。

#### Step 3: Kiro IDE のセットアップ

Kiro IDE は **Windows側にインストール**して、WSL2のターミナルから呼び出す構成が推奨です。
（VS Code の `code .` と同じ仕組みです）

**① WSL2側に誤ってインストールした場合はアンインストール:**

```bash
sudo dpkg -r kiro-ide
# または
sudo apt-get remove kiro-ide
```

**② Windows側に Kiro IDE をインストール:**

1. Windows側のブラウザで [kiro.dev/downloads](https://kiro.dev/downloads) を開く
2. **「Download for Windows (x64)」** をダウンロード
3. インストーラーを実行

**③ WSL2側にプロジェクトをクローン:**

```bash
# WSL2のホームディレクトリにプロジェクトを配置
mkdir -p ~/projects
cd ~/projects
git clone <repository-url> ai-mealplan
```

**④ Kiro IDE からプロジェクトを開く:**

```bash
# WSL2のターミナルでプロジェクトディレクトリに移動
cd ~/projects/ai-mealplan

# Kiro IDE を起動（Windows側のKiroがWSL2のパスを開く）
kiro .
```

**⑤ Kiro IDE の統合ターミナルを Ubuntu-24.04 に設定:**

Kiro IDE のターミナルを開いたときに自動で Ubuntu-24.04（WSL2）が起動するよう設定します。

**方法1: settings.json で設定（推奨）**

`Ctrl + Shift + P` → `Preferences: Open User Settings (JSON)` を開き、以下を追加します。

```json
"terminal.integrated.profiles.windows": {
  "Ubuntu-24.04 (WSL)": {
    "path": "wsl.exe",
    "args": ["-d", "Ubuntu-24.04", "--cd", "~"],
    "icon": "terminal-ubuntu"
  }
},
"terminal.integrated.defaultProfile.windows": "Ubuntu-24.04 (WSL)"
```

設定後、Kiro IDE のターミナル（`` Ctrl + ` ``）を開くと Ubuntu-24.04 のホームディレクトリ（`/home/yourname`）で起動します。

**方法2: Windows Terminal のコマンドライン設定**

Windows Terminal を使う場合は以下を設定します。

1. Windows Terminal を開く
2. タブバーの「∨」→「設定」をクリック
3. 左メニューから「Ubuntu-24.04」を選択
4. 「コマンドライン」欄に以下を入力して「保存」

```
wsl.exe -d Ubuntu-24.04 --cd ~
```

### 5-3. 環境変数の設定

```bash
# プロジェクトルートで実行（WSL2 上）
cp .env.example .env
# .env を編集して POSTGRES_PASSWORD を設定する
```

`.env` の内容例:

```env
POSTGRES_DB=aimealplan_it
POSTGRES_USER=aimealplan
POSTGRES_PASSWORD=your_password_here
```

### 5-4. コンテナ起動・IT試験実行

```bash
# 1. コンテナをバックグラウンドで起動（初回はイメージビルドが走る）
docker compose up -d

# アプリの起動ログを確認（"Started AiMealPlanApplication" が出るまで待つ）
docker compose logs -f app

# 2. IT試験を実行（Docker IT プロファイル用 URL を指定）
mvn failsafe:integration-test failsafe:verify -Dapp.base.url=http://localhost:8080

# 特定クラスのみ実行
mvn failsafe:integration-test failsafe:verify \
  -Dapp.base.url=http://localhost:8080 \
  -Dit.test=RecipeScreenTransitionIT

# 3. 試験後にコンテナを停止
docker compose down

# DB データも含めて完全リセットする場合
docker compose down -v
```

### 5-5. プロファイル対応表

| プロファイル | DB | 起動方法 | IT試験 URL |
|:---|:---|:---|:---|
| `e2e`（既存） | H2 インメモリ | `mvn spring-boot:run -Dspring-boot.run.profiles=e2e` | `http://localhost:8082` |
| `it`（新規） | PostgreSQL（Docker） | `docker compose up -d` | `http://localhost:8080` |

---

## 6. ディレクトリ構成

```text
├── .ai/                    # AIへの指示書・ログ管理
├── docs/
│   ├── architecture/       # API仕様・ドメイン定義・画面設計
│   ├── requirements/       # 要件定義
│   └── reviews/            # コードレビュー記録（REV-*.md）
├── mocks/                  # プロトタイプ・ワイヤーフレーム・モックデータ
├── src/
│   ├── main/java/com/aimealplan/
│   │   ├── config/         # Spring Security 設定
│   │   ├── controller/     # REST コントローラ
│   │   ├── entity/         # JPA エンティティ
│   │   ├── exception/      # 例外クラス・グローバルハンドラー
│   │   ├── model/          # DTO・リクエストクラス
│   │   ├── repository/     # Spring Data JPA リポジトリ
│   │   ├── service/        # サービスインターフェース・実装
│   │   └── validation/     # カスタムバリデーション
│   ├── main/resources/
│   │   ├── application.properties        # 本番設定（PostgreSQL）
│   │   ├── application-e2e.properties    # E2E テスト設定（H2）
│   │   └── static/                       # HTML・CSS・JS
│   └── test/java/com/aimealplan/
│       ├── controller/     # コントローラ単体テスト
│       ├── e2e/            # Playwright E2E テスト
│       ├── entity/         # エンティティ単体テスト
│       ├── exception/      # 例外クラス単体テスト
│       ├── model/          # DTO 単体テスト
│       ├── repository/     # リポジトリテスト
│       ├── service/        # サービス単体テスト
│       └── validation/     # バリデーション単体テスト
└── README.md
```

---

## 6. 今後の予定

- 認証・認可機能の実装（`SecurityConfig` の `TODO [SEC-01]` / `TODO [SEC-02]` 対応）
- S-01〜S-04 画面（ログイン・カレンダー・献立詳細・設定）の実装
- IT試験 Step 4〜6 の実装（表示値検証・業務フローシナリオ・異常系）
- 統合テスト・カバレッジ残課題（`UserServiceImpl` 91.5%）の解消
