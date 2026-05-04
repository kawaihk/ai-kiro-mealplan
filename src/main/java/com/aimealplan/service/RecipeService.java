package com.aimealplan.service;

import com.aimealplan.model.RecipeDto;

import java.util.List;
import java.util.Optional;

public interface RecipeService {

    /**
     * 新規レシピを登録する。
     *
     * @param recipeDto 登録するレシピ情報
     * @return 登録後のレシピ情報
     */
    RecipeDto createRecipe(RecipeDto recipeDto);

    /**
     * 全レシピの一覧を取得する。
     *
     * @return レシピ DTO のリスト
     */
    List<RecipeDto> getAllRecipes();

    /**
     * 指定 ID のレシピを取得する。
     *
     * @param id レシピ ID
     * @return レシピ情報（存在しない場合は空）
     */
    Optional<RecipeDto> getRecipeById(Long id);

    /**
     * 指定 ID のレシピ情報を更新する。
     *
     * @param id        更新対象のレシピ ID
     * @param recipeDto 更新するレシピ情報
     * @return 更新後のレシピ情報
     */
    RecipeDto updateRecipe(Long id, RecipeDto recipeDto);

    /**
     * 指定 ID のレシピを削除する。
     *
     * @param id 削除対象のレシピ ID
     */
    void deleteRecipe(Long id);

    /**
     * タイトルにキーワードを含むレシピを検索する。
     *
     * @param keyword 検索キーワード
     * @return 該当するレシピ DTO のリスト
     */
    List<RecipeDto> searchRecipesByTitle(String keyword);
}
