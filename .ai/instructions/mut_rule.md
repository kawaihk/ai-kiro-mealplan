# MUT (Module Under Test) 配置ルールおよびフォルダ階層チェック

## 1. 目的
プロダクトコードとテストコードの明確な分離、およびJavaの標準的なパッケージ命名規則とファイルシステム上のフォルダ階層の一致を保証する。これにより、プロジェクトの可読性、保守性、ビルドの安定性を向上させる。

## 2. チェック項目

### 2.1. プロダクトコードとテストコードの分離
- **ルール**: プロダクトコードは `src/main/java` 配下に、テストコードは `src/test/java` 配下にそれぞれ配置されていること。
- **詳細**:
    - `src/main/java` 配下にテストコード（`*Test.java`）が存在しないこと。
    - `src/test/java` 配下にプロダクトコード（`@Entity`, `@Service`, `@Repository`, `@RestController`, `@Component` などが付与されたクラス、またはドメインモデル、バリデーション定義など）が存在しないこと。

### 2.2. パッケージとフォルダ階層の一致
- **ルール**: 各Javaファイルの `package` 宣言が、ファイルシステム上のフォルダ階層と完全に一致していること。
- **詳細**:
    - 例: `package com.aimealplan.domain.model;` と宣言されたファイルは、`.../com/aimealplan/domain/model/` フォルダ内に存在すること。
    - ルートパッケージ (`com.aimealplan`) からのパスが、`src/main/java` または `src/test/java` の直下から始まること。

### 2.3. JS/CSS/HTMLなどの静的リソースの配置
- **ルール**: 静的リソースは `src/main/resources/static` 配下に配置されていること。
- **詳細**:
    - JavaScriptファイルは `src/main/resources/static/js` 配下、CSSファイルは `src/main/resources/static/css` 配下など、種類に応じたサブディレクトリに整理されていること。