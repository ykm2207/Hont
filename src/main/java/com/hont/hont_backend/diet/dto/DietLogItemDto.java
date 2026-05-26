package com.hont.hont_backend.diet.dto;

import com.hont.hont_backend.diet.entity.DietLogItem;
import lombok.Getter;

@Getter
public class DietLogItemDto {

    private final Long id;
    private final String mealType;
    private final String foodName;
    private final double amountG;
    private final double calories;
    private final double carbohydrate;
    private final double protein;
    private final double fat;

    public DietLogItemDto(DietLogItem item) {
        this.id = item.getId();
        this.mealType = item.getMealType().name();
        this.foodName = item.getFoodName();
        this.amountG = item.getServingSize();
        this.calories = item.getCalories();
        this.carbohydrate = item.getCarbohydrate();
        this.protein = item.getProtein();
        this.fat = item.getFat();
    }
}
