#!/bin/bash

echo "=== フォルダ構成ルール準拠チェック ==="

# 1. 期待されるディレクトリの存在確認
check_dirs=(
  "src/main/java/com/aimealplan/service/impl"
  "src/main/java/com/aimealplan/model"
  "src/main/java/com/aimealplan/repository"
  "src/main/java/com/aimealplan/service"
  "src/main/java/com/aimealplan/controller"
  "src/main/java/com/aimealplan/validation"
  "src/main/resources/static/css"
  "src/main/resources/static/js/utils"
  "src/test/java/com/aimealplan/service/impl"
)

for d in "${check_dirs[@]}"; do
  if [ -d "$d" ]; then
    echo "[PASS] ディレクトリ存在確認: $d"
  else
    echo "[FAIL] ディレクトリ欠落: $d"
  fi
done

# 2. 個別ファイルの存在確認 (project_folder_rule.md に基づく)
check_files=(
  # src/main/java
  "src/main/java/com/aimealplan/service/MealService.java"
  "src/main/java/com/aimealplan/service/RecipeService.java"
  "src/main/java/com/aimealplan/service/UserGoalService.java"
  "src/main/java/com/aimealplan/service/UserService.java"
  "src/main/java/com/aimealplan/service/impl/MealServiceImpl.java"
  "src/main/java/com/aimealplan/service/impl/RecipeServiceImpl.java"
  "src/main/java/com/aimealplan/service/impl/UserGoalServiceImpl.java"
  "src/main/java/com/aimealplan/service/impl/UserServiceImpl.java"
  "src/main/java/com/aimealplan/model/MealDto.java"
  "src/main/java/com/aimealplan/model/UserDto.java"
  "src/main/java/com/aimealplan/model/UserGoalDto.java"
  "src/main/java/com/aimealplan/validation/PfcRatioSum.java"
  "src/main/java/com/aimealplan/validation/PfcRatioSumValidator.java"
  "src/main/java/com/aimealplan/entity/Meal.java"
  "src/main/java/com/aimealplan/entity/MealPlan.java"
  "src/main/java/com/aimealplan/entity/Recipe.java"
  "src/main/java/com/aimealplan/entity/User.java"
  "src/main/java/com/aimealplan/repository/MealRepository.java"
  "src/main/java/com/aimealplan/repository/UserGoalRepository.java"
  "src/main/java/com/aimealplan/repository/UserRepository.java"
  "src/main/java/com/aimealplan/exception/ErrorResponse.java"
  "src/main/java/com/aimealplan/exception/GlobalExceptionHandler.java"
  "src/main/java/com/aimealplan/exception/ResourceNotFoundException.java"
  "src/main/java/com/aimealplan/controller/MealController.java"
  "src/main/java/com/aimealplan/controller/UserGoalController.java"
  "src/main/java/com/aimealplan/controller/UserController.java"
  # src/main/resources
  "src/main/resources/static/index.html"
  "src/main/resources/static/css/style.css"
  "src/main/resources/static/js/constants.js"
  "src/main/resources/static/js/utils/apiClient.js"
  # src/test/java
  "src/test/java/com/aimealplan/service/MealServiceTest.java"
  "src/test/java/com/aimealplan/service/RecipeServiceTest.java"
  "src/test/java/com/aimealplan/service/UserGoalServiceTest.java"
  "src/test/java/com/aimealplan/service/UserServiceTest.java"
  "src/test/java/com/aimealplan/service/impl/MealServiceImplTest.java"
  "src/test/java/com/aimealplan/service/impl/RecipeServiceImplTest.java"
  "src/test/java/com/aimealplan/service/impl/UserGoalServiceImplTest.java"
  "src/test/java/com/aimealplan/service/impl/UserServiceImplTest.java"
  "src/test/java/com/aimealplan/model/MealDtoTest.java"
  "src/test/java/com/aimealplan/model/UserDtoTest.java"
  "src/test/java/com/aimealplan/model/UserGoalDtoTest.java"
  "src/test/java/com/aimealplan/validation/PfcRatioSumTest.java"
  "src/test/java/com/aimealplan/validation/PfcRatioSumValidatorTest.java"
  "src/test/java/com/aimealplan/entity/MealTest.java"
  "src/test/java/com/aimealplan/entity/MealPlanTest.java"
  "src/test/java/com/aimealplan/entity/RecipeTest.java"
  "src/test/java/com/aimealplan/entity/UserTest.java"
  "src/test/java/com/aimealplan/repository/MealRepositoryTest.java"
  "src/test/java/com/aimealplan/repository/UserGoalRepositoryTest.java"
  "src/test/java/com/aimealplan/repository/UserRepositoryTest.java"
  "src/test/java/com/aimealplan/exception/ErrorResponseTest.java"
  "src/test/java/com/aimealplan/exception/GlobalExceptionHandlerTest.java"
  "src/test/java/com/aimealplan/exception/ResourceNotFoundExceptionTest.java"
  "src/test/java/com/aimealplan/controller/MealControllerTest.java"
  "src/test/java/com/aimealplan/controller/UserGoalControllerTest.java"
  "src/test/java/com/aimealplan/controller/UserControllerTest.java"
)

has_error=0
for f in "${check_files[@]}"; do
  if [ ! -f "$f" ]; then
    echo "[FAIL] ファイル欠落: $f"
    has_error=1
  elif [ ! -s "$f" ]; then
    echo "[FAIL] ファイルが空です: $f"
    has_error=1
  else
    echo "[PASS] ファイル存在確認: $f"
  fi
done

if [ $has_error -eq 1 ]; then
    echo "[ERROR] 一部のファイルが不足しているか、内容が空です。物理構成を確認してください。"
fi

# 3. 旧中間層ディレクトリの消滅確認
if [ ! -d "src/main/java/com/aimealplan/domain" ] && [ ! -d "src/main/java/com/aimealplan/infrastructure" ]; then
    echo "[PASS] 旧中間層 (domain/infrastructure) の削除"
else
    echo "[FAIL] 旧中間層ディレクトリが残存しています"
fi

echo "=== チェック終了 ==="
