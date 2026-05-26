package com.hont.hont_backend.diet.service;

import com.hont.hont_backend.auth.entity.User;
import com.hont.hont_backend.auth.repository.UserRepository;
import com.hont.hont_backend.diet.dto.DietLogItemDto;
import com.hont.hont_backend.diet.dto.DietLogRequest;
import com.hont.hont_backend.diet.dto.DietLogResponse;
import com.hont.hont_backend.diet.dto.FoodInfoDto;
import com.hont.hont_backend.diet.entity.DietLog;
import com.hont.hont_backend.diet.entity.DietLogItem;
import com.hont.hont_backend.diet.repository.DietLogItemRepository;
import com.hont.hont_backend.diet.repository.DietLogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class DietService {

    private final UserRepository userRepository;
    private final DietLogRepository dietLogRepository;
    private final DietLogItemRepository dietLogItemRepository;
    private final FoodSearchService foodSearchService;

    // 음식 검색 (로컬 DB)
    public List<FoodInfoDto> searchFood(String query) {
        return foodSearchService.searchFood(query);
    }

    // 식단 항목 추가 (날짜별 DietLog가 없으면 자동 생성)
    @Transactional
    public DietLogResponse addDietItem(Long userId, DietLogRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        // 해당 날짜 식단 기록이 없으면 새로 생성
        DietLog dietLog = dietLogRepository.findByUserIdAndDietDate(userId, request.getDietDate())
                .orElseGet(() -> dietLogRepository.save(DietLog.builder()
                        .user(user)
                        .dietDate(request.getDietDate())
                        .build()));

        DietLogItem item = DietLogItem.builder()
                .dietLog(dietLog)
                .mealType(request.getMealType())
                .foodCode(request.getFoodCode())
                .foodName(request.getFoodName())
                .servingSize(parseServingSize(request.getServingSize()))
                .calories(request.getCalories())
                .carbohydrate(request.getCarbohydrate())
                .protein(request.getProtein())
                .fat(request.getFat())
                .build();

        dietLog.getItems().add(dietLogItemRepository.save(item));
        return new DietLogResponse(dietLog);
    }

    // 특정 날짜 식단 조회 (Android: flat 아이템 리스트 반환)
    public List<DietLogItemDto> getDietLog(Long userId, LocalDate date) {
        return dietLogRepository.findByUserIdAndDietDate(userId, date)
                .map(log -> log.getItems().stream()
                        .map(DietLogItemDto::new)
                        .toList())
                .orElse(List.of());
    }

    // "150g" → 150.0 파싱
    private double parseServingSize(String servingSize) {
        if (servingSize == null || servingSize.isBlank()) return 0;
        return Double.parseDouble(servingSize.replaceAll("[^0-9.]", ""));
    }

    // 월별 식단 조회
    public List<DietLogResponse> getMonthlyDietLogs(Long userId, int year, int month) {
        LocalDate start = LocalDate.of(year, month, 1);
        LocalDate end = start.withDayOfMonth(start.lengthOfMonth());
        return dietLogRepository.findByUserIdAndDietDateBetween(userId, start, end).stream()
                .map(DietLogResponse::new)
                .toList();
    }

    // 식단 항목 삭제
    @Transactional
    public void deleteDietItem(Long userId, Long itemId) {
        DietLogItem item = dietLogItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("식단 항목을 찾을 수 없습니다"));
        if (!item.getDietLog().getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("접근 권한이 없습니다");
        }
        dietLogItemRepository.delete(item);
    }
}
