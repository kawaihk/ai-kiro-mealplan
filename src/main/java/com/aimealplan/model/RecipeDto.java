package com.aimealplan.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * レシピ情報を転送するためのDTO。
 * サービス層とコントローラ層の入出力に使用し、Recipe エンティティへの直接依存を排除する。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RecipeDto {

    private Long id;

    @NotBlank
    @Size(max = 100)
    private String title;

    private String description;

    @PositiveOrZero
    private Integer calories;
}
