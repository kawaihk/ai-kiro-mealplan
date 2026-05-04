package com.aimealplan.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * ユーザー更新リクエスト用 DTO。
 * {@link UserDto} はレスポンス用途も兼ねるため、更新専用クラスとして分離する。
 */
@Data
public class UserUpdateRequest {

    @NotBlank(message = "ユーザー名は必須です")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    private String username;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    private String email;

    /** ロール文字列（USER / ADMIN）。省略時はロールを変更しない。大文字・小文字どちらも可。 */
    @Pattern(
        regexp = "(?i)USER|ADMIN",
        message = "ロールは USER または ADMIN を指定してください"
    )
    private String role;
}
