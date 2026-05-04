package com.aimealplan.service.impl;

import com.aimealplan.entity.Recipe;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.RecipeDto;
import com.aimealplan.repository.RecipeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeServiceImplTest {

    @Mock
    private RecipeRepository recipeRepository;

    @InjectMocks
    private RecipeServiceImpl recipeService;

    private Recipe recipe;
    private RecipeDto recipeDto;

    @BeforeEach
    void setUp() {
        recipe = Recipe.builder().id(1L).title("カレー").description("説明").calories(500).build();
        recipeDto = RecipeDto.builder().title("カレー").description("説明").calories(500).build();
    }

    // --- createRecipe ---

    @Test
    @DisplayName("createRecipe - 正常系: レシピを保存して DTO を返す")
    void createRecipe_success() {
        when(recipeRepository.save(any(Recipe.class))).thenReturn(recipe);

        RecipeDto result = recipeService.createRecipe(recipeDto);

        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("カレー");
        verify(recipeRepository).save(any(Recipe.class));
    }

    // --- getAllRecipes ---

    @Test
    @DisplayName("getAllRecipes - 正常系: 全レシピのリストを返す")
    void getAllRecipes_success() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        List<RecipeDto> result = recipeService.getAllRecipes();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("カレー");
    }

    // --- getRecipeById ---

    @Test
    @DisplayName("getRecipeById - 正常系: 存在するIDで Optional に包まれた DTO を返す")
    void getRecipeById_success() {
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));

        Optional<RecipeDto> result = recipeService.getRecipeById(1L);

        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("カレー");
    }

    @Test
    @DisplayName("getRecipeById - 異常系: 存在しないIDで Optional.empty を返す")
    void getRecipeById_notFound() {
        when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

        Optional<RecipeDto> result = recipeService.getRecipeById(99L);

        assertThat(result).isEmpty();
    }

    // --- updateRecipe ---

    @Test
    @DisplayName("updateRecipe - 正常系: レシピを更新して DTO を返す")
    void updateRecipe_success() {
        RecipeDto updateDto = RecipeDto.builder().title("更新カレー").calories(600).build();
        Recipe updated = Recipe.builder().id(1L).title("更新カレー").calories(600).build();
        when(recipeRepository.findById(1L)).thenReturn(Optional.of(recipe));
        when(recipeRepository.save(any(Recipe.class))).thenReturn(updated);

        RecipeDto result = recipeService.updateRecipe(1L, updateDto);

        assertThat(result.getTitle()).isEqualTo("更新カレー");
        assertThat(result.getCalories()).isEqualTo(600);
    }

    @Test
    @DisplayName("updateRecipe - 異常系: 存在しないIDで ResourceNotFoundException をスロー")
    void updateRecipe_notFound() {
        when(recipeRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> recipeService.updateRecipe(99L, recipeDto))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- deleteRecipe ---

    @Test
    @DisplayName("deleteRecipe - 正常系: 存在するIDで削除を実行する")
    void deleteRecipe_success() {
        when(recipeRepository.existsById(1L)).thenReturn(true);

        recipeService.deleteRecipe(1L);

        verify(recipeRepository).deleteById(1L);
    }

    @Test
    @DisplayName("deleteRecipe - 異常系: 存在しないIDで ResourceNotFoundException をスロー")
    void deleteRecipe_notFound() {
        when(recipeRepository.existsById(99L)).thenReturn(false);

        assertThatThrownBy(() -> recipeService.deleteRecipe(99L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining("99");
    }

    // --- searchRecipesByTitle ---

    @Test
    @DisplayName("searchRecipesByTitle - 正常系: キーワードに一致するレシピを返す")
    void searchRecipesByTitle_success() {
        when(recipeRepository.findByTitleContaining("カレー")).thenReturn(List.of(recipe));

        List<RecipeDto> result = recipeService.searchRecipesByTitle("カレー");

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getTitle()).isEqualTo("カレー");
    }

    @Test
    @DisplayName("searchRecipesByTitle - 正常系: 空文字の場合は全件取得にフォールバックする")
    void searchRecipesByTitle_emptyKeyword_fallbackToAll() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        List<RecipeDto> result = recipeService.searchRecipesByTitle("");

        assertThat(result).hasSize(1);
        verify(recipeRepository).findAll();
        verify(recipeRepository, never()).findByTitleContaining(any());
    }

    @Test
    @DisplayName("searchRecipesByTitle - 正常系: null の場合も全件取得にフォールバックする")
    void searchRecipesByTitle_nullKeyword_fallbackToAll() {
        when(recipeRepository.findAll()).thenReturn(List.of(recipe));

        List<RecipeDto> result = recipeService.searchRecipesByTitle(null);

        assertThat(result).hasSize(1);
        verify(recipeRepository).findAll();
    }
}
