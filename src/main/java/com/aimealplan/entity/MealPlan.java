package com.aimealplan.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "meal_plans")
@Getter  
@Setter  
@ToString(exclude = {"user", "meals"})  
@EqualsAndHashCode(exclude = {"user", "meals"})  
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MealPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "title")
    private String title;

    @Column(name = "goal") // 例: ダイエット、筋肥大など
    private String goal;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "mealPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Meal> meals;
}