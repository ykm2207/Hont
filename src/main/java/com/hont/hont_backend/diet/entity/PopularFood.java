package com.hont.hont_backend.diet.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "popular_food")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PopularFood {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 음식명
    @Column(nullable = false, length = 100)
    private String foodName;

    // 기준량 (예: 100g, 1인분(200g))
    @Column(length = 30)
    private String servingSize;

    private Double calories;
    private Double carbohydrate;
    private Double protein;
    private Double fat;
}
