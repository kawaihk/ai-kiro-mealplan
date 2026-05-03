package com.aimealplan.service;

import com.aimealplan.model.RecipeDto;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    RecipeDto createRecipe(RecipeDto recipeDto);

    List<RecipeDto> getAllRecipes();

    Optional<RecipeDto> getRecipeById(Long id);

    RecipeDto updateRecipe(Long id, RecipeDto recipeDto);

    void deleteRecipe(Long id);

    List<RecipeDto> searchRecipesByTitle(String keyword);
}
