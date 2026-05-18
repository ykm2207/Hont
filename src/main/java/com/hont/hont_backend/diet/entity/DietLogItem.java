package com.hont.hont_backend.diet.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "diet_log_items")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class DietLogItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "diet_log_id", nullable = false)
    private DietLog dietLog;

    // 식사 시간대 (BREAKFAST, LUNCH, DINNER, SNACK)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MealType mealType;

    // 식약처 API 식품 코드
    private String foodCode;

    // 음식 이름
    @Column(nullable = false)
    private String foodName;

    // 섭취량 (g)
    private double servingSize;

    // 칼로리 (kcal)
    private double calories;

    // 탄수화물 (g)
    private double carbohydrate;

    // 단백질 (g)
    private double protein;

    // 지방 (g)
    private double fat;
}
