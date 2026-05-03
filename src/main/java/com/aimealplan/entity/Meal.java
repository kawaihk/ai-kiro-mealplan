package com.aimealplan.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@Entity
@Table(name = "meals")
@Getter  
@Setter  
@ToString(exclude = "mealPlan")  
@EqualsAndHashCode(exclude = "mealPlan")  
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "meal_plan_id", nullable = false)
    private MealPlan mealPlan;

    @Column(name = "meal_date")
    private LocalDate mealDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_type")
    private MealType mealType;

    @Column(name = "recipe_name")
    private String recipeName;

    @Column(columnDefinition = "TEXT")
    private String instructions;

    @Column(name = "total_calories")
    private Integer calories;

    @Column(name = "protein")
    private Integer protein;

    public enum MealType {
        BREAKFAST, LUNCH, DINNER, SNACK
    }
}