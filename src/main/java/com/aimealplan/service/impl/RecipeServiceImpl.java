package com.aimealplan.service.impl;

import com.aimealplan.entity.Recipe;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.RecipeDto;
import com.aimealplan.repository.RecipeRepository;
import com.aimealplan.service.RecipeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class RecipeServiceImpl implements RecipeService {

    private final RecipeRepository recipeRepository;

    @Override
    @Transactional
    public RecipeDto createRecipe(RecipeDto recipeDto) {
        Recipe recipe = toEntity(recipeDto);
        return toDto(recipeRepository.save(recipe));
    }

    @Override
    public List<RecipeDto> getAllRecipes() {
        return recipeRepository.findAll().stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<RecipeDto> getRecipeById(Long id) {
        return recipeRepository.findById(id).map(this::toDto);
    }

    @Override
    @Transactional
    public RecipeDto updateRecipe(Long id, RecipeDto recipeDto) {
        Recipe recipe = recipeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + id));
        recipe.setTitle(recipeDto.getTitle());
        recipe.setDescription(recipeDto.getDescription());
        recipe.setCalories(recipeDto.getCalories());
        return toDto(recipeRepository.save(recipe));
    }

    @Override
    @Transactional
    public void deleteRecipe(Long id) {
        if (!recipeRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recipe not found with id: " + id);
        }
        recipeRepository.deleteById(id);
    }

    @Override
    public List<RecipeDto> searchRecipesByTitle(String keyword) {
        if (keyword == null || keyword.isBlank()) {
            return getAllRecipes();
        }
        return recipeRepository.findByTitleContaining(keyword).stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    // --- マッピングヘルパー ---

    private RecipeDto toDto(Recipe recipe) {
        return RecipeDto.builder()
                .id(recipe.getId())
                .title(recipe.getTitle())
                .description(recipe.getDescription())
                .calories(recipe.getCalories())
                .build();
    }

    private Recipe toEntity(RecipeDto dto) {
        return Recipe.builder()
                .title(dto.getTitle())
                .description(dto.getDescription())
                .calories(dto.getCalories())
                .build();
    }
}
