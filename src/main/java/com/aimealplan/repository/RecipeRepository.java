package com.aimealplan.repository;

import com.aimealplan.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    
    /**
     * タイトルに含まれるキーワードでレシピを検索します。
     * 
     * @param keyword 検索キーワード
     * @return 該当するレシピのリスト
     */
    List<Recipe> findByTitleContaining(String keyword);
}