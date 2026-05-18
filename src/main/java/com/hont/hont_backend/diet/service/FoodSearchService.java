package com.hont.hont_backend.diet.service;

import com.hont.hont_backend.diet.dto.FoodInfoDto;
import com.hont.hont_backend.diet.repository.FoodInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodSearchService {

    private final FoodInfoRepository foodInfoRepository;

    // 로컬 DB에서 식품명 검색 (최대 20건)
    public List<FoodInfoDto> searchFood(String query) {
        return foodInfoRepository.findByFoodNameContaining(query, PageRequest.of(0, 20))
                .stream()
                .map(FoodInfoDto::new)
                .toList();
    }
}
