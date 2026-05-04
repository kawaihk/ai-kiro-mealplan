package com.aimealplan.exception;

import com.aimealplan.controller.RecipeController;
import com.aimealplan.model.RecipeDto;
import com.aimealplan.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
@Import(GlobalExceptionHandler.class)
@WithMockUser
class GlobalExceptionHandlerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("ResourceNotFoundException - 404 Not Found とエラーボディを返す")
    void handleResourceNotFoundException() throws Exception {
        when(recipeService.getRecipeById(99L))
                .thenReturn(Optional.empty());

        mockMvc.perform(get("/api/recipes/99"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("DuplicateResourceException - 409 Conflict とエラーボディを返す")
    void handleDuplicateResourceException() throws Exception {
        when(recipeService.createRecipe(any(RecipeDto.class)))
                .thenThrow(new DuplicateResourceException("Duplicate resource"));

        RecipeDto request = RecipeDto.builder().title("カレー").build();

        mockMvc.perform(post("/api/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"));
    }

    @Test
    @DisplayName("MethodArgumentNotValidException - 400 Bad Request とフィールドエラー詳細を返す")
    void handleMethodArgumentNotValidException() throws Exception {
        RecipeDto invalid = RecipeDto.builder().title("").build(); // @NotBlank 違反

        mockMvc.perform(post("/api/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @DisplayName("IllegalArgumentException - 400 Bad Request とエラーボディを返す")
    void handleIllegalArgumentException() throws Exception {
        when(recipeService.updateRecipe(anyLong(), any(RecipeDto.class)))
                .thenThrow(new IllegalArgumentException("Invalid argument"));

        RecipeDto request = RecipeDto.builder().title("カレー").build();

        mockMvc.perform(put("/api/recipes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400));
    }

    @Test
    @DisplayName("Exception（予期しない例外） - 500 Internal Server Error とエラーボディを返す")
    void handleGlobalException() throws Exception {
        when(recipeService.getAllRecipes())
                .thenThrow(new RuntimeException("Unexpected error"));

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.status").value(500))
                .andExpect(jsonPath("$.message").value("An unexpected error occurred"));
    }
}
