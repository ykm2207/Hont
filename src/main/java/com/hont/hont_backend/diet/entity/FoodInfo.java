package com.hont.hont_backend.diet.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "food_info")
@Getter
@NoArgsConstructor
public class FoodInfo {

    @Id
    @Column(name = "food_code", length = 20)
    private String foodCode;

    @Column(name = "food_name", nullable = false, length = 200)
    private String foodName;

    // 데이터구분명 (음식, 가공식품, 건강기능식품)
    @Column(name = "food_type", length = 20)
    private String foodType;

    @Column(name = "food_category", length = 50)
    private String foodCategory;

    @Column(name = "food_subcategory", length = 50)
    private String foodSubcategory;

    // 영양성분함량기준량 (예: 100g당)
    @Column(name = "serving_size", length = 30)
    private String servingSize;

    @Column(name = "calories")
    private Double calories;

    @Column(name = "moisture")
    private Double moisture;

    @Column(name = "protein")
    private Double protein;

    @Column(name = "fat")
    private Double fat;

    @Column(name = "carbohydrate")
    private Double carbohydrate;

    @Column(name = "sugar")
    private Double sugar;

    @Column(name = "dietary_fiber")
    private Double dietaryFiber;

    @Column(name = "calcium")
    private Double calcium;

    @Column(name = "iron")
    private Double iron;

    @Column(name = "sodium")
    private Double sodium;

    @Column(name = "vitamin_c")
    private Double vitaminC;

    @Column(name = "cholesterol")
    private Double cholesterol;

    @Column(name = "saturated_fat")
    private Double saturatedFat;

    @Column(name = "trans_fat")
    private Double transFat;

    @Column(name = "manufacturer", length = 100)
    private String manufacturer;

    @Column(name = "food_weight", length = 30)
    private String foodWeight;
}
