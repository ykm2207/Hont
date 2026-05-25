package com.hont.hont_backend.diet.repository;

import com.hont.hont_backend.diet.entity.PopularFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PopularFoodRepository extends JpaRepository<PopularFood, Long> {

    List<PopularFood> findByFoodNameContaining(String foodName);

    java.util.Optional<PopularFood> findByFoodName(String foodName);
}
