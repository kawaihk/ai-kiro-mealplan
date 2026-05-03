#!/bin/bash

# プロジェクトルートに移動
# cd /path/to/ai-mealplan

echo ">>> 1. ターゲットディレクトリの作成を開始します..."
mkdir -p src/main/java/com/aimealplan/{service/impl,model,validation,entity,repository,exception,controller}
mkdir -p src/test/java/com/aimealplan/{service,model,validation,entity,repository,exception,controller}
mkdir -p src/main/resources/static/{html,css,js/utils}

echo ">>> 2. プロダクトコード (src/main/java) の移動を開始します..."
# Model / Entity
[ -d src/main/java/com/aimealplan/domain/model ] && mv src/main/java/com/aimealplan/domain/model/* src/main/java/com/aimealplan/model/ 2>/dev/null
# Validation
[ -d src/main/java/com/aimealplan/domain/validation ] && mv src/main/java/com/aimealplan/domain/validation/* src/main/java/com/aimealplan/validation/ 2>/dev/null
# Repository
[ -d src/main/java/com/aimealplan/infrastructure/repository ] && mv src/main/java/com/aimealplan/infrastructure/repository/* src/main/java/com/aimealplan/repository/ 2>/dev/null
# Service
[ -d src/main/java/com/aimealplan/application/service ] && mv src/main/java/com/aimealplan/application/service/* src/main/java/com/aimealplan/service/ 2>/dev/null
# Controller
[ -d src/main/java/com/aimealplan/presentation/controller ] && mv src/main/java/com/aimealplan/presentation/controller/* src/main/java/com/aimealplan/controller/ 2>/dev/null

echo ">>> 3. テストコード (src/test/java) の階層を整理します..."
[ -d src/test/java/com/aimealplan/application/service ] && mv src/test/java/com/aimealplan/application/service/* src/test/java/com/aimealplan/service/ 2>/dev/null
[ -d src/test/java/com/aimealplan/presentation/controller ] && mv src/test/java/com/aimealplan/presentation/controller/* src/test/java/com/aimealplan/controller/ 2>/dev/null
[ -d src/test/java/com/aimealplan/domain/model ] && mv src/test/java/com/aimealplan/domain/model/* src/test/java/com/aimealplan/model/ 2>/dev/null

echo ">>> 4. Javaファイル内の package および import 文を更新します..."
# src 配下の全Javaファイルを対象に、古いパッケージパスを置換
# 注意: macOS の場合は sed -i '' とする必要があります
find src -name "*.java" -type f | xargs sed -i 's/com\.aimealplan\.domain\.model/com\.aimealplan\.model/g'
find src -name "*.java" -type f | xargs sed -i 's/com\.aimealplan\.domain\.validation/com\.aimealplan\.validation/g'
find src -name "*.java" -type f | xargs sed -i 's/com\.aimealplan\.infrastructure\.repository/com\.aimealplan\.repository/g'
find src -name "*.java" -type f | xargs sed -i 's/com\.aimealplan\.presentation\.controller/com\.aimealplan\.controller/g'
find src -name "*.java" -type f | xargs sed -i 's/com\.aimealplan\.application\.service/com\.aimealplan\.service/g'

echo ">>> 5. 旧ディレクトリのクリーンアップ..."
rm -rf src/main/java/com/aimealplan/domain
rm -rf src/main/java/com/aimealplan/infrastructure
rm -rf src/main/java/com/aimealplan/presentation
rm -rf src/main/java/com/aimealplan/application
rm -rf src/test/java/com/aimealplan/application
rm -rf src/test/java/com/aimealplan/presentation
rm -rf src/test/java/com/aimealplan/domain

echo ">>> 再配置が完了しました。"
