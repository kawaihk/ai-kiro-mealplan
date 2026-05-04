package com.aimealplan.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ErrorResponseTest {

    @Test
    @DisplayName("Builder - 全フィールドを正しく設定できること")
    void builder_allFields() {
        LocalDateTime now = LocalDateTime.now();

        ErrorResponse response = ErrorResponse.builder()
                .timestamp(now)
                .status(404)
                .error("Not Found")
                .message("Resource not found")
                .path("/api/recipes/99")
                .build();

        assertThat(response.getTimestamp()).isEqualTo(now);
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getError()).isEqualTo("Not Found");
        assertThat(response.getMessage()).isEqualTo("Resource not found");
        assertThat(response.getPath()).isEqualTo("/api/recipes/99");
        assertThat(response.getDetails()).isNull();
    }

    @Test
    @DisplayName("Builder - details フィールドを設定できること")
    void builder_withDetails() {
        ErrorResponse.ErrorDetail detail = ErrorResponse.ErrorDetail.builder()
                .field("title")
                .message("タイトルは必須です")
                .build();

        ErrorResponse response = ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .details(List.of(detail))
                .build();

        assertThat(response.getDetails()).hasSize(1);
        assertThat(response.getDetails().get(0).getField()).isEqualTo("title");
        assertThat(response.getDetails().get(0).getMessage()).isEqualTo("タイトルは必須です");
    }

    @Test
    @DisplayName("Builder - details が複数件設定できること")
    void builder_withMultipleDetails() {
        List<ErrorResponse.ErrorDetail> details = List.of(
                ErrorResponse.ErrorDetail.builder().field("title").message("必須です").build(),
                ErrorResponse.ErrorDetail.builder().field("calories").message("0以上の値を入力してください").build()
        );

        ErrorResponse response = ErrorResponse.builder()
                .status(400)
                .error("Bad Request")
                .message("Validation failed")
                .details(details)
                .build();

        assertThat(response.getDetails()).hasSize(2);
        assertThat(response.getDetails()).extracting(ErrorResponse.ErrorDetail::getField)
                .containsExactly("title", "calories");
    }

    @Test
    @DisplayName("ErrorDetail.Builder - フィールドとメッセージを正しく設定できること")
    void errorDetail_builder() {
        ErrorResponse.ErrorDetail detail = ErrorResponse.ErrorDetail.builder()
                .field("email")
                .message("メールアドレスの形式が正しくありません")
                .build();

        assertThat(detail.getField()).isEqualTo("email");
        assertThat(detail.getMessage()).isEqualTo("メールアドレスの形式が正しくありません");
    }
}
