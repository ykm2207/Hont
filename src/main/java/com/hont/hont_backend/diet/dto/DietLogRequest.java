package com.hont.hont_backend.diet.dto;

import com.hont.hont_backend.diet.entity.MealType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DietLogRequest {

    @NotNull(message = "날짜를 입력해주세요")
    private LocalDate dietDate;

    @NotNull(message = "식사 시간대를 입력해주세요")
    private MealType mealType;

    private String foodCode;

    @NotBlank(message = "음식 이름을 입력해주세요")
    private String foodName;

    private String servingSize;
    private double calories;
    private double carbohydrate;
    private double protein;
    private double fat;
}
