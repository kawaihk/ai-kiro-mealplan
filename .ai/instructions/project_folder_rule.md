# プロジェクトフォルダ構成ルール

## src/main 配下のフォルダとファイル一覧

```text
src/main/
├── java/
│   └── com/
│       └── aimealplan/
│           ├─── service/
│           │       ├── MealService.java
│           │       ├── RecipeService.java
│           │       ├── UserGoalService.java
│           │       ├── UserService.java
│           │       └── impl/
│           │             ├── MealServiceImpl.java
│           │             ├── RecipeServiceImpl.java
│           │             ├── UserGoalServiceImpl.java
│           │             └── UserServiceImpl.java
│           ├─── model/
│           │      ├── MealDto.java
│           │      ├── UserDto.java
│           │      └── UserGoalDto.java
│           ├───── validation/
│           │       ├── PfcRatioSum.java
│           │       └── PfcRatioSumValidator.java
│           ├────── entity/
│           │       ├── Meal.java
│           │       ├── MealPlan.java
│           │       ├── Recipe.java
│           │       └── User.java
│           ├────── repository/
│           │       ├── MealRepository.java
│           │       ├── UserGoalRepository.java
│           │       └── UserRepository.java
│           ├────── exception/
│           │       ├── ErrorResponse.java
│           │       ├── GlobalExceptionHandler.java
│           │       └── ResourceNotFoundException.java
│           └───controller/
│                   ├── MealController.java
│                   ├── UserGoalController.java
│                   └── UserController.java
└── resources/
    └── static/
        ├── index.html
        ├─── css/    
        │     ├── style.css
        └── js/
            ├── constants.js
            └── utils/
                └── apiClient.js
```

## src/test 配下のフォルダとファイル一覧

```text
src/test
└── java/
   └── com/
       └── aimealplan/
           ├─── service/
           │       ├── MealServiceTest.java
           │       ├── RecipeServiceTest.java
           │       ├── UserGoalServiceTest.java
           │       ├── UserServiceTest.java
           │       └── impl/
           │             ├── MealServiceImplTest.java
           │             ├── RecipeServiceImplTest.java
           │             ├── UserGoalServiceImplTest.java
           │             └── UserServiceImplTest.java
           ├─── model/
           │      ├── MealDtoTest.java
           │      ├── UserDtoTest.java
           │      └── UserGoalDtoTest.java
           ├───── validation/
           │       ├── PfcRatioSumTest.java
           │       └── PfcRatioSumValidatorTest.java
           ├────── entity/
           │       ├── MealTest.java
           │       ├── MealPlanTest.java
           │       ├── RecipeTest.java
           │       └── UserTest.java
           ├────── repository/
           │       ├── MealRepositoryTest.java
           │       ├── UserGoalRepositoryTest.java
           │       └── UserRepositoryTest.java
           ├────── exception/
           │       ├── ErrorResponseTest.java
           │       ├── GlobalExceptionHandlerTest.java
           │       └── ResourceNotFoundExceptionTest.java
           └───controller/
                   ├── MealControllerTest.java
                   ├── UserGoalControllerTest.java
                   └── UserControllerTest.java


```