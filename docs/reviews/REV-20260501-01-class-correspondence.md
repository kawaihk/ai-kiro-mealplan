# クラス対応状況レビュー記録 (ID: REV-20260501-01)

## 1. レビュー概要
- **目的**: MUTルール（`.ai\instructions\mut_rule.md`）に基づく、プロダクト/テストコードの完全分離、パッケージ階層の整合性、およびクラス対応状況の確認
- **対象範囲**: `src/main/java` および `src/test/java` 配下の全Javaファイル、`src/main/resources/static` 配下のJSファイル
- **実施日**: 2026-05-01
- **参照ガイドライン**: `.ai\instructions\mut_rule.md`

## 2. チェック結果一覧

### 2.1. MUTルール準拠状況 (分離と階層)

| ID | チェック項目 | 状況 | 判定 |
|:---|:---|:---|:---|
| R1 | `src/main/java` 配下にテストコード (`*Test.java`) が混入していないか | 混入なし | **OK** |
| R2 | `src/test/java` 配下にプロダクトコードが混入していないか | `UserGoal.java`, `PfcRatioSum.java`, `PfcRatioSumValidator.java`, `UserGoalRepository.java` が混入していたため、`src/main/java` へ移動済み | **OK** (是正後) |
| R3 | 各Javaファイルの `package` 宣言と物理フォルダ階層が完全に一致しているか | 全てのクラスで一致を確認 | **OK** |
| R4 | 静的リソースが `src/main/resources/static` 配下に配置されているか | `apiClient.js` は `src/main/resources/static/js/utils/` に、`constants.js` は `src/main/resources/static/js/` に配置済み | **OK** |

### 2.2. クラス対応状況 (Service / Controller レイヤー)

| テストクラス (src/test/java) | 対応する実装クラス (src/main/java) | 状況 | 備考 |
|:---|:---|:---|:---|
| `UserServiceTest` | `UserService` | **良好** | 対応する実装クラスが存在し、配置も適切 |
| `MealServiceTest` | `MealService` | **良好** | 対応する実装クラスが存在し、配置も適切 |
| `UserGoalServiceTest` | `UserGoalService` | **良好** | 対応する実装クラスが存在し、配置も適切 |
| `UserControllerTest` | `UserController` | **良好** | 対応する実装クラスが存在し、配置も適切 |
| `MealControllerTest` | `MealController` | **良好** | 対応する実装クラスが存在し、配置も適切 |
| `UserGoalControllerTest` | `UserGoalController` | **良好** | 対応する実装クラスが存在し、配置も適切 |

### 2.3. クラス対応状況 (Entity / Repository / Validation)

| クラス名 | src/main/java (正) | src/test/java (誤/重複) | 判定 |
|:---|:---:|:---:|:---|
| `UserGoal` | 存在 | 不在 | OK |
| `UserGoalRepository` | 存在 | 不在 | **OK** |
| `PfcRatioSum` | 存在 | 不在 |

## 3. 指摘事項および改善アクション

1. **実装クラスの作成/復旧**:
   - テストが参照している `UserService`, `MealService`, `UserGoalService` および各 Controller クラスを `src/main/java` 配下の適切なパッケージに作成してください。
2. **テストディレクトリのクリーンアップ**:
   - `src/test/java/com/aimealplan/application/service/` 配下に混入している以下のファイルを削除してください：
     - `PfcRatioSum.java`, `PfcRatioSumValidator.java`, `UserGoalRepository.java`

## 4. 総評
テストコードの先行実装は進んでいますが、それに対応する本体コードが `src/main` 側に不足しています。また、前回の移動作業の残骸がテストディレクトリに残っており、ビルド時のクラス競合や混乱を招く恐れがあります。速やかな整理を推奨します。

---