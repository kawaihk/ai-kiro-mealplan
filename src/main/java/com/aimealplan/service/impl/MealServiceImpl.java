package com.aimealplan.service.impl;

import com.aimealplan.entity.Meal;
import com.aimealplan.entity.MealPlan;
import com.aimealplan.exception.ResourceNotFoundException;
import com.aimealplan.model.MealDto;
import com.aimealplan.repository.MealPlanRepository;
import com.aimealplan.repository.MealRepository;
import com.aimealplan.service.MealService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MealServiceImpl implements MealService {

    private final MealRepository mealRepository;
    private final MealPlanRepository mealPlanRepository;

    @Override
    @Transactional
    public MealDto createMeal(MealDto mealDto) {
        MealPlan mealPlan = mealPlanRepository.findById(mealDto.getMealPlanId())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "MealPlan not found with id: " + mealDto.getMealPlanId()));

        Meal meal = toEntity(mealDto, mealPlan);
        return toDto(mealRepository.save(meal));
    }

    /**
     * 指定したミールプランに紐づくMeal一覧を取得します。
     *
     * <p>DB呼び出しを最小化するため、まず Meal を全件取得し、結果が空の場合のみ
     * MealPlan の存在確認を行います。これにより通常ケース（Meal が1件以上）では
     * DB アクセスが1回で済みます。</p>
     *
     * <ul>
     *   <li>MealPlan が存在し Meal が1件以上の場合 → Meal の一覧を返す</li>
     *   <li>MealPlan が存在し Meal が0件の場合 → 空リストを返す</li>
     *   <li>MealPlan が存在しない場合 → {@link com.aimealplan.exception.ResourceNotFoundException} をスロー（404）</li>
     * </ul>
     *
     * @param mealPlanId ミールプランID
     * @return Meal DTOのリスト（0件の場合は空リスト）
     * @throws com.aimealplan.exception.ResourceNotFoundException 指定した mealPlanId が存在しない場合
     */
    @Override
    public List<MealDto> getMealsByMealPlanId(Long mealPlanId) {
        // findByMealPlanId の結果が空の場合、mealPlanId の存在確認を行って
        // 「MealPlan が存在しない」と「Meal が0件」を区別する。
        // existsById を別途呼ばず、空リストのときのみ存在確認を行うことで
        // 通常ケース（Meal が1件以上）の DB 呼び出しを1回に抑える。
        List<Meal> meals = mealRepository.findByMealPlanId(mealPlanId);
        if (meals.isEmpty() && !mealPlanRepository.existsById(mealPlanId)) {
            throw new ResourceNotFoundException("MealPlan not found with id: " + mealPlanId);
        }
        return meals.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public MealDto getMealById(Long mealPlanId, Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        if (!meal.getMealPlan().getId().equals(mealPlanId)) {
            throw new ResourceNotFoundException(
                    "Meal with id: " + id + " does not belong to MealPlan with id: " + mealPlanId);
        }
        return toDto(meal);
    }

    @Override
    @Transactional
    public MealDto updateMeal(Long mealPlanId, Long id, MealDto mealDto) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        if (!meal.getMealPlan().getId().equals(mealPlanId)) {
            throw new ResourceNotFoundException(
                    "Meal with id: " + id + " does not belong to MealPlan with id: " + mealPlanId);
        }

        meal.setMealDate(mealDto.getMealDate());
        meal.setMealType(mealDto.getMealType());
        meal.setRecipeName(mealDto.getRecipeName());
        meal.setInstructions(mealDto.getInstructions());
        meal.setCalories(mealDto.getCalories());
        meal.setProtein(mealDto.getProtein());

        return toDto(mealRepository.save(meal));
    }

    @Override
    @Transactional
    public void deleteMeal(Long mealPlanId, Long id) {
        Meal meal = mealRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Meal not found with id: " + id));
        if (!meal.getMealPlan().getId().equals(mealPlanId)) {
            throw new ResourceNotFoundException(
                    "Meal with id: " + id + " does not belong to MealPlan with id: " + mealPlanId);
        }
        mealRepository.delete(meal);
    }

    // --- マッピングヘルパー ---

    private Meal toEntity(MealDto dto, MealPlan mealPlan) {
        return Meal.builder()
                .mealPlan(mealPlan)
                .mealDate(dto.getMealDate())
                .mealType(dto.getMealType())
                .recipeName(dto.getRecipeName())
                .instructions(dto.getInstructions())
                .calories(dto.getCalories())
                .protein(dto.getProtein())
                .build();
    }

    private MealDto toDto(Meal meal) {
        return MealDto.builder()
                .id(meal.getId())
                .mealPlanId(meal.getMealPlan().getId())
                .mealDate(meal.getMealDate())
                .mealType(meal.getMealType())
                .recipeName(meal.getRecipeName())
                .instructions(meal.getInstructions())
                .calories(meal.getCalories())
                .protein(meal.getProtein())
                .build();
    }
}
