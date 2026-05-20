package com.hont.hont_backend.diet.dto;

import com.hont.hont_backend.diet.entity.FoodInfo;
import com.hont.hont_backend.diet.entity.PopularFood;
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

    // food_info 테이블 기반 생성자
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

    // popular_food 테이블 기반 생성자
    public FoodInfoDto(PopularFood food) {
        this.foodCode = null;
        this.foodName = food.getFoodName();
        this.foodType = "인기음식";
        this.foodCategory = null;
        this.servingSize = food.getServingSize();
        this.calories = food.getCalories();
        this.protein = food.getProtein();
        this.fat = food.getFat();
        this.carbohydrate = food.getCarbohydrate();
        this.sugar = null;
        this.dietaryFiber = null;
        this.sodium = null;
        this.cholesterol = null;
        this.saturatedFat = null;
        this.transFat = null;
        this.manufacturer = null;
        this.foodWeight = null;
    }

    // food_info 검색 결과를 그룹핑·평균 후 DTO로 만들 때 사용하는 생성자
    public FoodInfoDto(String foodName, String servingSize,
                       Double calories, Double carbohydrate, Double protein, Double fat) {
        this.foodCode = null;
        this.foodName = foodName;
        this.foodType = null;
        this.foodCategory = null;
        this.servingSize = servingSize;
        this.calories = calories;
        this.protein = protein;
        this.fat = fat;
        this.carbohydrate = carbohydrate;
        this.sugar = null;
        this.dietaryFiber = null;
        this.sodium = null;
        this.cholesterol = null;
        this.saturatedFat = null;
        this.transFat = null;
        this.manufacturer = null;
        this.foodWeight = null;
    }
}
