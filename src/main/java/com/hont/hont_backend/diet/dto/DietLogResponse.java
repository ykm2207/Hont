package com.hont.hont_backend.diet.dto;

import com.hont.hont_backend.diet.entity.DietLog;
import com.hont.hont_backend.diet.entity.DietLogItem;
import com.hont.hont_backend.diet.entity.MealType;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class DietLogResponse {

    private final Long id;
    private final LocalDate dietDate;
    private final double totalCalories;
    private final List<DietLogItemResponse> items;

    public DietLogResponse(DietLog dietLog) {
        this.id = dietLog.getId();
        this.dietDate = dietLog.getDietDate();
        this.totalCalories = dietLog.getTotalCalories();
        this.items = dietLog.getItems().stream()
                .map(DietLogItemResponse::new)
                .toList();
    }

    @Getter
    public static class DietLogItemResponse {
        private final Long id;
        private final MealType mealType;
        private final String foodName;
        private final double servingSize;
        private final double calories;
        private final double carbohydrate;
        private final double protein;
        private final double fat;

        public DietLogItemResponse(DietLogItem item) {
            this.id = item.getId();
            this.mealType = item.getMealType();
            this.foodName = item.getFoodName();
            this.servingSize = item.getServingSize();
            this.calories = item.getCalories();
            this.carbohydrate = item.getCarbohydrate();
            this.protein = item.getProtein();
            this.fat = item.getFat();
        }
    }
}
