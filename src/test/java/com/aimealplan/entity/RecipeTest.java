package com.aimealplan.entity;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class RecipeTest {

    private Recipe buildRecipe(Long id) {
        return Recipe.builder()
                .id(id)
                .title("チキンカレー")
                .description("スパイシーなカレー")
                .calories(500)
                .build();
    }

    @Test
    @DisplayName("@Builder - 全フィールドを指定してオブジェクトを生成できること")
    void builder_allFields() {
        Recipe recipe = buildRecipe(1L);

        assertThat(recipe.getId()).isEqualTo(1L);
        assertThat(recipe.getTitle()).isEqualTo("チキンカレー");
        assertThat(recipe.getDescription()).isEqualTo("スパイシーなカレー");
        assertThat(recipe.getCalories()).isEqualTo(500);
    }

    @Test
    @DisplayName("@NoArgsConstructor - 引数なしコンストラクタでオブジェクトを生成できること")
    void noArgsConstructor() {
        Recipe recipe = new Recipe();
        assertThat(recipe).isNotNull();
    }

    @Test
    @DisplayName("@Getter / @Setter - フィールドの取得・設定ができること")
    void getterSetter() {
        Recipe recipe = new Recipe();
        recipe.setTitle("ラーメン");
        recipe.setCalories(700);

        assertThat(recipe.getTitle()).isEqualTo("ラーメン");
        assertThat(recipe.getCalories()).isEqualTo(700);
    }

    @Test
    @DisplayName("@EqualsAndHashCode(exclude = {id, createdAt, updatedAt}) - id・タイムスタンプを除くフィールドで等価比較されること")
    void equalsAndHashCode_excludesIdAndTimestamps() {
        Recipe recipe1 = Recipe.builder().id(1L).title("カレー").calories(500).build();
        Recipe recipe2 = Recipe.builder().id(2L).title("カレー").calories(500).build();

        // id が異なっても title・calories が同じなら等価
        assertThat(recipe1).isEqualTo(recipe2);
        assertThat(recipe1.hashCode()).isEqualTo(recipe2.hashCode());
    }

    @Test
    @DisplayName("@EqualsAndHashCode - title が異なる場合は等価でないこと")
    void equalsAndHashCode_differentTitle_notEqual() {
        Recipe recipe1 = Recipe.builder().id(1L).title("カレー").calories(500).build();
        Recipe recipe2 = Recipe.builder().id(1L).title("ラーメン").calories(500).build();

        assertThat(recipe1).isNotEqualTo(recipe2);
    }

    @Test
    @DisplayName("@ToString - toString にタイトルとカロリーが含まれること")
    void toString_containsFields() {
        Recipe recipe = buildRecipe(1L);
        String str = recipe.toString();

        assertThat(str).contains("title=チキンカレー");
        assertThat(str).contains("calories=500");
    }
}
