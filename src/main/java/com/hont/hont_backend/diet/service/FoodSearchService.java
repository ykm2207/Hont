package com.hont.hont_backend.diet.service;

import com.hont.hont_backend.diet.dto.FoodInfoDto;
import com.hont.hont_backend.diet.entity.FoodInfo;
import com.hont.hont_backend.diet.repository.FoodInfoRepository;
import com.hont.hont_backend.diet.repository.PopularFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FoodSearchService {

    private final PopularFoodRepository popularFoodRepository;
    private final FoodInfoRepository foodInfoRepository;

    // 제거할 수식어 패턴 (대표 음식명 추출에 사용)
    private static final List<String> MODIFIERS = List.of(
        "저지방", "무가당", "저당", "무설탕", "유기농", "국산", "냉동",
        "레토르트", "즉석", "간편", "저염", "저칼로리", "고단백", "무첨가",
        "혼합", "통밀", "영양강화", "기능성"
    );

    // 허용 문자 외 특수문자 포함 여부 (한글·영문·숫자·공백·괄호·하이픈·슬래시·점·% 는 허용)
    private static final Pattern SPECIAL_CHAR = Pattern.compile("[^가-힣a-zA-Z0-9\\s\\(\\)\\-\\./%]");

    // 최대 음식명 길이
    private static final int MAX_NAME_LENGTH = 20;

    /**
     * 음식 검색
     * 1단계: 인기 음식 DB (popular_food) 우선 검색
     * 2단계: 결과가 5개 미만이면 식약처 데이터 (food_info) 추가 검색 (필터링 적용)
     */
    public List<FoodInfoDto> searchFood(String query) {
        // 1단계: 인기 음식 DB 검색
        List<FoodInfoDto> popularResults = popularFoodRepository
                .findByFoodNameContaining(query)
                .stream()
                .map(FoodInfoDto::new)
                .toList();

        if (popularResults.size() >= 5) {
            return popularResults;
        }

        // 2단계: food_info에서 추가 검색
        List<FoodInfoDto> fallbackResults = searchFromFoodInfo(query);

        // 합치기 (인기 음식 먼저, 중복 음식명 제외)
        Set<String> popularNames = popularResults.stream()
                .map(FoodInfoDto::getFoodName)
                .collect(Collectors.toSet());

        List<FoodInfoDto> combined = new ArrayList<>(popularResults);
        fallbackResults.stream()
                .filter(f -> !popularNames.contains(f.getFoodName()))
                .limit(20 - popularResults.size())
                .forEach(combined::add);

        return combined;
    }

    /**
     * food_info 테이블에서 검색 후 필터링·그룹핑 적용
     */
    private List<FoodInfoDto> searchFromFoodInfo(String query) {
        // 후보를 넉넉히 가져온 뒤 Java에서 필터링
        List<FoodInfo> raw = foodInfoRepository.findByFoodNameContaining(query, PageRequest.of(0, 300));

        // 1. 이름 길이 초과 제거
        // 2. 특수문자 포함 제거
        // 3. 대표 음식명(수식어 제거)으로 그룹핑 → 영양정보 평균
        // 4. 이름 길이 오름차순 정렬
        Map<String, List<FoodInfo>> grouped = raw.stream()
                .filter(f -> f.getFoodName().length() <= MAX_NAME_LENGTH)
                .filter(f -> !SPECIAL_CHAR.matcher(f.getFoodName()).find())
                .collect(Collectors.groupingBy(f -> normalize(f.getFoodName())));

        return grouped.entrySet().stream()
                .map(e -> toAveragedDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(f -> f.getFoodName().length()))
                .limit(20)
                .toList();
    }

    /**
     * 수식어를 제거하여 대표 음식명 반환 (그룹핑 키로 사용)
     */
    private String normalize(String name) {
        String result = name;
        for (String mod : MODIFIERS) {
            result = result.replace(mod, "").trim();
        }
        // 공백 여러 개 → 하나로 정리
        return result.replaceAll("\\s+", " ").trim();
    }

    /**
     * 같은 음식명 그룹의 영양정보 평균값으로 DTO 생성
     */
    private FoodInfoDto toAveragedDto(String representativeName, List<FoodInfo> foods) {
        OptionalDouble cal  = foods.stream().filter(f -> f.getCalories()     != null).mapToDouble(FoodInfo::getCalories).average();
        OptionalDouble carb = foods.stream().filter(f -> f.getCarbohydrate() != null).mapToDouble(FoodInfo::getCarbohydrate).average();
        OptionalDouble prot = foods.stream().filter(f -> f.getProtein()      != null).mapToDouble(FoodInfo::getProtein).average();
        OptionalDouble fat  = foods.stream().filter(f -> f.getFat()          != null).mapToDouble(FoodInfo::getFat).average();

        // 기준량은 그룹 내 첫 번째 항목 사용
        String servingSize = foods.get(0).getServingSize();

        return new FoodInfoDto(
                representativeName,
                servingSize,
                cal.isPresent()  ? Math.round(cal.getAsDouble()  * 10) / 10.0 : null,
                carb.isPresent() ? Math.round(carb.getAsDouble() * 10) / 10.0 : null,
                prot.isPresent() ? Math.round(prot.getAsDouble() * 10) / 10.0 : null,
                fat.isPresent()  ? Math.round(fat.getAsDouble()  * 10) / 10.0 : null
        );
    }
}
