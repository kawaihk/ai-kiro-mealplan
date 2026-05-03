# ドメイン定義 (Domain Definition)

## 1. 境界づけられたコンテキスト: 献立管理

### エンティティ (Entities)
- **User (ユーザー)**
    - 識別子: `user_id`
    - 属性: `username`, `email`, `target_nutrients` (目標栄養素)

- **MealPlan (週間献立)**
    - 識別子: `plan_id`
    - 属性: `user_id`, `date` (日付)
    - 制約: 1日に対して1つのMealPlanが存在する。
    - 備考: 曜日は `date` から算出する。

- **Meal (食事内容)**
    - 識別子: `meal_id`
    - 属性: `meal_type` (朝食/昼食/夕食), `title` (メイン料理名)
    - 振る舞い: 合計栄養素の算出。

### 値オブジェクト (Value Objects)
- **Nutrients (栄養素バランス)**
    - 属性: `calories`, `protein`, `fat`, `carbohydrates`
    - 属性: `additional_nutrients` (Map形式の拡張属性)
    - 振る舞い: 
        - 合計エネルギー計算: `(protein * 4) + (fat * 9) + (carbohydrates * 4)`
        - PFC比率計算: 各栄養素のエネルギー寄与率を算出する。

- **MealItem (構成メニュー)**
    - 属性: `name` (料理を構成する品目、例: 納豆、味噌汁)

### 列挙型 (Enums)
- **MealType**: `BREAKFAST` (朝), `LUNCH` (昼), `DINNER` (夕), `SNACK` (間食)

## 2. 業務ルール
1. 1つの `MealPlan` は最大3つの `Meal` (朝・昼・夕) を持つことができる。
2. `SNACK` は随時追加可能とする。
3. 各 `Meal` には複数の `MealItem` と `Tag` が紐づく。
4. 栄養素は `Meal` 単位で保持し、`MealPlan` (1日合計) で集計し、ユーザーの目標値と比較可能とする。