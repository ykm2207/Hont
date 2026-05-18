package com.hont.hont_backend.diet.dto;

import com.hont.hont_backend.diet.entity.FoodInfo;
import lombok.Getter;

@Getter
public class FoodInfoDto {

    private final String foodCode;
    private final String foodName;
    private final String foodType;
    private final String foodCategory;
    private final String servingSize;
    private final Double calories;
    private final Double protein;
    private final Double fat;
    private final Double carbohydrate;
    private final Double sugar;
    private final Double dietaryFiber;
    private final Double sodium;
    private final Double cholesterol;
    private final Double saturatedFat;
    private final Double transFat;
    private final String manufacturer;
    private final String foodWeight;

    public FoodInfoDto(FoodInfo food) {
        this.foodCode = food.getFoodCode();
        this.foodName = food.getFoodName();
        this.foodType = food.getFoodType();
        this.foodCategory = food.getFoodCategory();
        this.servingSize = food.getServingSize();
        this.calories = food.getCalories();
        this.protein = food.getProtein();
        this.fat = food.getFat();
        this.carbohydrate = food.getCarbohydrate();
        this.sugar = food.getSugar();
        this.dietaryFiber = food.getDietaryFiber();
        this.sodium = food.getSodium();
        this.cholesterol = food.getCholesterol();
        this.saturatedFat = food.getSaturatedFat();
        this.transFat = food.getTransFat();
        this.manufacturer = food.getManufacturer();
        this.foodWeight = food.getFoodWeight();
    }
}
