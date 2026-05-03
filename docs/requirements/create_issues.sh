#!/bin/bash

# 1. ユーザー管理の導入
gh issue create --title "ユーザー管理の導入" --body "課題: meal_plans テーブルにユーザーを識別するカラムがなく、複数人での利用が考慮されていない。対策: users テーブルを作成し、各 meal_plans に user_id を追加することを検討。" --label "enhancement" --label "backend"

# 2. MealItemの扱い（VO vs Entity）
gh issue create --title "MealItemの扱い（VO vs Entity）" --body "課題: domain_definition.md では MealItem を値オブジェクトとしているが、table_definition.sql では SERIAL PRIMARY KEY を付与している。対策: 単なる文字列のリストとして扱うなら CollectionTable を検討し、ライフサイクルを個別に管理するならドメイン定義をEntityに修正する。" --label "bug" --label "domain"

# 3. ドラッグ＆ドロップのAPI設計
gh issue create --title "ドラッグ＆ドロップのAPI設計" --body "課題: フロントエンドでの入れ替え操作を永続化するための「並べ替え用シーケンス」や「一括更新API」の定義が不足している。対策: meals テーブルに sort_order を追加するか、meal_type を動的に変更するロジックを要件に追加する。" --label "enhancement" --label "api"

# 4. 栄養素の精度とバリデーション
gh issue create --title "栄養素の精度とバリデーション" --body "課題: DECIMAL(5, 2) の範囲（最大999.99）で、カロリーや炭水化物の合計値が溢れる可能性はないか。対策: 特にカロリー（total_calories）については、将来的な集計機能を考慮して余裕のある桁数を再検討する。" --label "discussion" --label "database"

# 5. 静的ファイルの配信構成
gh issue create --title "静的ファイルの配信構成" --body "課題: Reactを使わない Vanilla JS 構成において、app.js などのモジュール分割やビルドプロセスの有無が不明確。対策: Spring Bootの static フォルダへの配置ルール、または現代的なES Modulesの使用要否を決定する。" --label "infrastructure" --label "frontend"

echo "すべてのIssueの登録が完了しました。"