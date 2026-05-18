package com.hont.hont_backend.diet.repository;

import com.hont.hont_backend.diet.entity.FoodInfo;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FoodInfoRepository extends JpaRepository<FoodInfo, String> {

    // 식품명 포함 검색 (최대 20건)
    List<FoodInfo> findByFoodNameContaining(String foodName, Pageable pageable);
}
