package com.hont.hont_backend.diet.controller;

import com.hont.hont_backend.common.response.ApiResponse;
import com.hont.hont_backend.diet.dto.DietLogRequest;
import com.hont.hont_backend.diet.dto.DietLogResponse;
import com.hont.hont_backend.diet.dto.FoodInfoDto;
import com.hont.hont_backend.diet.service.DietService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/diet")
@RequiredArgsConstructor
public class DietController {

    private final DietService dietService;

    // 음식 검색 (로컬 DB)
    @GetMapping("/foods/search")
    public ApiResponse<List<FoodInfoDto>> searchFood(@RequestParam String query) {
        return ApiResponse.ok(dietService.searchFood(query));
    }

    // 식단 항목 추가
    @PostMapping("/logs")
    public ApiResponse<DietLogResponse> addDietItem(
            @AuthenticationPrincipal Long userId,
            @Valid @RequestBody DietLogRequest request) {
        return ApiResponse.ok(dietService.addDietItem(userId, request));
    }

    // 특정 날짜 식단 조회
    @GetMapping("/logs/{date}")
    public ApiResponse<DietLogResponse> getDietLog(
            @AuthenticationPrincipal Long userId,
            @PathVariable LocalDate date) {
        return ApiResponse.ok(dietService.getDietLog(userId, date));
    }

    // 월별 식단 조회
    @GetMapping("/logs")
    public ApiResponse<List<DietLogResponse>> getMonthlyDietLogs(
            @AuthenticationPrincipal Long userId,
            @RequestParam int year,
            @RequestParam int month) {
        return ApiResponse.ok(dietService.getMonthlyDietLogs(userId, year, month));
    }

    // 식단 항목 삭제
    @DeleteMapping("/logs/items/{itemId}")
    public ApiResponse<Void> deleteDietItem(
            @AuthenticationPrincipal Long userId,
            @PathVariable Long itemId) {
        dietService.deleteDietItem(userId, itemId);
        return ApiResponse.ok("식단 항목이 삭제되었습니다", null);
    }
}
