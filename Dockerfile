# ============================================================
# マルチステージビルド
#   Stage 1 (builder): Maven でアプリをビルド
#   Stage 2 (runtime): JRE のみの軽量イメージで実行
# ============================================================

# ------ Stage 1: ビルド ----------------------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS builder

WORKDIR /workspace

# 依存解決を先にキャッシュ（pom.xml が変わらない限り再ダウンロードしない）
COPY pom.xml .
RUN mvn dependency:go-offline -q

# ソースをコピーしてビルド（単体テストはスキップ、IT試験はコンテナ起動後に別途実行）
COPY src ./src
RUN mvn package -DskipTests -q

# ------ Stage 2: 実行 -----------------------------------------
FROM eclipse-temurin:17-jre-jammy AS runtime

WORKDIR /app

# ビルド成果物をコピー
COPY --from=builder /workspace/target/*.jar app.jar

# アプリが使用するポート
EXPOSE 8080

# Spring プロファイルは docker-compose.yml の環境変数で切り替える
ENTRYPOINT ["java", "-jar", "app.jar"]
