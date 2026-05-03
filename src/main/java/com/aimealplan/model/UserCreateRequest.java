package com.aimealplan.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * ユーザー登録リクエスト用 DTO。
 * パスワードを含むため {@link UserDto} とは分離して管理する。
 */
@Data
public class UserCreateRequest {

    @NotBlank(message = "ユーザー名は必須です")
    @Size(max = 50, message = "ユーザー名は50文字以内で入力してください")
    private String username;

    @NotBlank(message = "パスワードは必須です")
    @Size(min = 8, message = "パスワードは8文字以上で入力してください")
    private String password;

    @NotBlank(message = "メールアドレスは必須です")
    @Email(message = "メールアドレスの形式が正しくありません")
    private String email;

    /** ロール文字列（USER / ADMIN）。省略時は USER が適用される。大文字・小文字どちらも可。 */
    @Pattern(
        regexp = "(?i)USER|ADMIN",
        message = "ロールは USER または ADMIN を指定してください"
    )
    private String role;
}
