package com.aimealplan.controller;

import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.RecipeDto;
import com.aimealplan.service.RecipeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RecipeController.class)
@WithMockUser
class RecipeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RecipeService recipeService;

    @Autowired
    private ObjectMapper objectMapper;

    // --- POST /api/recipes ---

    @Test
    @DisplayName("POST /api/recipes - レシピ登録 正常系: 201 Created を返す")
    void createRecipe_success() throws Exception {
        RecipeDto request = RecipeDto.builder().title("チキンカレー").calories(500).build();
        RecipeDto response = RecipeDto.builder().id(1L).title("チキンカレー").calories(500).build();
        when(recipeService.createRecipe(any(RecipeDto.class))).thenReturn(response);

        mockMvc.perform(post("/api/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("チキンカレー"));
    }

    @Test
    @DisplayName("POST /api/recipes - レシピ登録 異常系: タイトル未入力で 400 Bad Request を返す")
    void createRecipe_validationError() throws Exception {
        RecipeDto request = RecipeDto.builder().title("").calories(500).build();

        mockMvc.perform(post("/api/recipes")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    // --- GET /api/recipes ---

    @Test
    @DisplayName("GET /api/recipes - 全レシピ取得 正常系: 200 OK とリストを返す")
    void getAllRecipes_success() throws Exception {
        List<RecipeDto> list = List.of(
                RecipeDto.builder().id(1L).title("カレー").build(),
                RecipeDto.builder().id(2L).title("ラーメン").build()
        );
        when(recipeService.getAllRecipes()).thenReturn(list);

        mockMvc.perform(get("/api/recipes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].title").value("カレー"));
    }

    // --- GET /api/recipes/{id} ---

    @Test
    @DisplayName("GET /api/recipes/{id} - ID指定取得 正常系: 200 OK を返す")
    void getRecipeById_success() throws Exception {
        RecipeDto dto = RecipeDto.builder().id(1L).title("カレー").build();
        when(recipeService.getRecipeById(1L)).thenReturn(Optional.of(dto));

        mockMvc.perform(get("/api/recipes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("カレー"));
    }

    @Test
    @DisplayName("GET /api/recipes/{id} - ID指定取得 異常系: 存在しない場合 404 を返す")
    void getRecipeById_notFound() throws Exception {
        when(recipeService.getRecipeById(99L)).thenReturn(Optional.empty());

        mockMvc.perform(get("/api/recipes/99"))
                .andExpect(status().isNotFound());
    }

    // --- PUT /api/recipes/{id} ---

    @Test
    @DisplayName("PUT /api/recipes/{id} - レシピ更新 正常系: 200 OK を返す")
    void updateRecipe_success() throws Exception {
        RecipeDto request = RecipeDto.builder().title("更新カレー").calories(600).build();
        RecipeDto response = RecipeDto.builder().id(1L).title("更新カレー").calories(600).build();
        when(recipeService.updateRecipe(eq(1L), any(RecipeDto.class))).thenReturn(response);

        mockMvc.perform(put("/api/recipes/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("更新カレー"));
    }

    @Test
    @DisplayName("PUT /api/recipes/{id} - レシピ更新 異常系: 存在しない場合 404 を返す")
    void updateRecipe_notFound() throws Exception {
        RecipeDto request = RecipeDto.builder().title("更新カレー").calories(600).build();
        when(recipeService.updateRecipe(eq(99L), any(RecipeDto.class)))
                .thenThrow(new ResourceNotFoundException("Recipe not found with id: 99"));

        mockMvc.perform(put("/api/recipes/99")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }

    // --- DELETE /api/recipes/{id} ---

    @Test
    @DisplayName("DELETE /api/recipes/{id} - レシピ削除 正常系: 204 No Content を返す")
    void deleteRecipe_success() throws Exception {
        doNothing().when(recipeService).deleteRecipe(1L);

        mockMvc.perform(delete("/api/recipes/1").with(csrf()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("DELETE /api/recipes/{id} - レシピ削除 異常系: 存在しない場合 404 を返す")
    void deleteRecipe_notFound() throws Exception {
        doThrow(new ResourceNotFoundException("Recipe not found with id: 99"))
                .when(recipeService).deleteRecipe(99L);

        mockMvc.perform(delete("/api/recipes/99").with(csrf()))
                .andExpect(status().isNotFound());
    }

    // --- GET /api/recipes/search ---

    @Test
    @DisplayName("GET /api/recipes/search - キーワード検索 正常系: 200 OK とリストを返す")
    void searchRecipes_success() throws Exception {
        List<RecipeDto> result = List.of(RecipeDto.builder().id(1L).title("カレー").build());
        when(recipeService.searchRecipesByTitle("カレー")).thenReturn(result);

        mockMvc.perform(get("/api/recipes/search").param("keyword", "カレー"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1));
    }

    @Test
    @DisplayName("GET /api/recipes/search - キーワード検索 異常系: keyword パラメータ未指定で 400 を返す")
    void searchRecipes_missingParam() throws Exception {
        mockMvc.perform(get("/api/recipes/search"))
                .andExpect(status().isBadRequest());
    }
}
