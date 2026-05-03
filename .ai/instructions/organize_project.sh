#!/bin/bash

# 1. ディレクトリ階層の作成
echo "Creating directory hierarchy..."
mkdir -p frontend/src
mkdir -p src/main/java/com/aimealplan/controller
mkdir -p src/main/java/com/aimealplan/service/impl
mkdir -p src/main/java/com/aimealplan/repository
mkdir -p src/main/java/com/aimealplan/entity
mkdir -p src/main/java/com/aimealplan/exception

# 2. フロントエンドファイルの移動
echo "Moving frontend files..."
[ -f index.html ] && mv index.html frontend/src/
[ -f app.js ] && mv app.js frontend/src/
[ -f style.css ] && mv style.css frontend/src/

# 3. バックエンドファイルの移動（ルート直下に存在する場合のみ）
echo "Moving backend files to correct packages..."
[ -f RecipeController.java ] && mv RecipeController.java src/main/java/com/aimealplan/controller/
[ -f RecipeService.java ] && mv RecipeService.java src/main/java/com/aimealplan/service/
[ -f RecipeServiceImpl.java ] && mv RecipeServiceImpl.java src/main/java/com/aimealplan/service/impl/
[ -f RecipeRepository.java ] && mv RecipeRepository.java src/main/java/com/aimealplan/repository/
[ -f Recipe.java ] && mv Recipe.java src/main/java/com/aimealplan/entity/
[ -f GlobalExceptionHandler.java ] && mv GlobalExceptionHandler.java src/main/java/com/aimealplan/exception/
[ -f ErrorResponse.java ] && mv ErrorResponse.java src/main/java/com/aimealplan/exception/
[ -f ResourceNotFoundException.java ] && mv ResourceNotFoundException.java src/main/java/com/aimealplan/exception/

# 4. 誤った場所に配置された重複ファイルのクリーンアップ
echo "Cleaning up misplaced files..."
rm -f src/main/java/com/aimealplan/entity/RecipeServiceImpl.java

echo "--------------------------------------------------"
echo "Project structure has been organized successfully!"
echo "Frontend: frontend/src/"
echo "Backend:  src/main/java/com/aimealplan/"
echo "--------------------------------------------------"

ls -R frontend/src