package com.hont.hont_backend.diet.repository;

import com.hont.hont_backend.diet.entity.FoodInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodInfoRepository extends JpaRepository<FoodInfo, String> {

    // 식품명 포함 검색 (건강기능식품 제외)
    List<FoodInfo> findByFoodNameContainingAndFoodTypeNot(String foodName, String foodType, Pageable pageable);
}
