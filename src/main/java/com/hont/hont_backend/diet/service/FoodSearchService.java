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

    // 건강기능식품 타입 (식단 검색에서 제외)
    private static final String EXCLUDED_FOOD_TYPE = "건강기능식품";

    // 한국어 동의어 맵 (검색어 → 대체 검색어)
    private static final Map<String, String> SYNONYMS = Map.of(
        "계란", "달걀",
        "달걀", "계란",
        "쇠고기", "소고기",
        "소고기", "쇠고기",
        "요구르트", "요거트",
        "요거트", "요구르트",
        "귀리", "오트밀",
        "오트밀", "귀리",
        "튜나", "참치"
    );

    // 제거할 수식어 패턴 (대표 음식명 추출에 사용)
    private static final List<String> MODIFIERS = List.of(
        // 영양 관련
        "저지방", "무가당", "저당", "무설탕", "저염", "저칼로리", "고단백", "무첨가",
        // 원산지·인증
        "유기농", "국산", "무농약", "친환경",
        // 형태·조리
        "냉동", "레토르트", "즉석", "간편", "혼합", "통밀", "영양강화", "기능성",
        // 마케팅 수식어
        "맛있는", "리얼", "진한", "더", "수제", "프리미엄", "특제", "고급",
        "신선한", "건강한", "웰빙", "생생", "담백한", "깔끔한"
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
        String synQuery = synonymQuery(query);

        // 1단계: 인기 음식 DB 검색 (원본 + 동의어 쿼리 합산)
        List<FoodInfoDto> popularResults = new ArrayList<>(
                popularFoodRepository.findByFoodNameContaining(query)
                        .stream().map(FoodInfoDto::new).toList());
        if (synQuery != null) {
            Set<String> found = popularResults.stream()
                    .map(FoodInfoDto::getFoodName).collect(Collectors.toSet());
            popularFoodRepository.findByFoodNameContaining(synQuery)
                    .stream().map(FoodInfoDto::new)
                    .filter(f -> !found.contains(f.getFoodName()))
                    .forEach(popularResults::add);
        }

        if (popularResults.size() >= 5) {
            return popularResults;
        }

        // 2단계: food_info에서 추가 검색 (원본 + 동의어 쿼리 합산)
        List<FoodInfoDto> fallbackResults = new ArrayList<>(searchFromFoodInfo(query));
        if (synQuery != null) {
            Set<String> found = fallbackResults.stream()
                    .map(FoodInfoDto::getFoodName).collect(Collectors.toSet());
            searchFromFoodInfo(synQuery).stream()
                    .filter(f -> !found.contains(f.getFoodName()))
                    .forEach(fallbackResults::add);
        }

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

    // 동의어 치환 쿼리 반환 (없으면 null)
    private String synonymQuery(String query) {
        for (Map.Entry<String, String> entry : SYNONYMS.entrySet()) {
            if (query.contains(entry.getKey())) {
                return query.replace(entry.getKey(), entry.getValue());
            }
        }
        return null;
    }

    /**
     * food_info 테이블에서 검색 후 필터링·그룹핑 적용
     */
    private List<FoodInfoDto> searchFromFoodInfo(String query) {
        // 후보를 넉넉히 가져온 뒤 Java에서 필터링 (건강기능식품 DB 레벨에서 제외)
        List<FoodInfo> raw = foodInfoRepository.findByFoodNameContainingAndFoodTypeNot(
                query, EXCLUDED_FOOD_TYPE, PageRequest.of(0, 300));

        // 1. 이름 길이 초과 제거
        // 2. 특수문자 포함 제거
        // 3. 대표 음식명(수식어 제거)으로 그룹핑 → 영양정보 평균
        // 4. 이름 길이 오름차순 정렬
        Map<String, List<FoodInfo>> grouped = raw.stream()
                .filter(f -> f.getFoodName().length() <= MAX_NAME_LENGTH)
                .filter(f -> !SPECIAL_CHAR.matcher(f.getFoodName()).find())
                .filter(this::isValidNutrition)
                .collect(Collectors.groupingBy(f -> normalize(f.getFoodName())))
                .entrySet().stream()
                // 정규화 후 이름이 너무 짧으면 제외 (수식어만 있던 경우)
                .filter(e -> e.getKey().length() >= 2)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

        return grouped.entrySet().stream()
                .map(e -> toAveragedDto(e.getKey(), e.getValue()))
                .sorted(Comparator.comparingInt(f -> f.getFoodName().length()))
                .limit(20)
                .toList();
    }

    /**
     * 물리적으로 불가능한 영양소 수치 필터링
     * - 칼로리 900kcal/100g 초과: 순수 지방도 ~900kcal 수준이므로 초과 시 데이터 오류
     * - 개별 매크로 100g/100g 초과: 물리적으로 불가능
     * - 매크로 합계 105g/100g 초과: 수분·회분 등을 감안해도 불가능
     */
    private boolean isValidNutrition(FoodInfo f) {
        if (f.getCalories() != null && f.getCalories() > 900) return false;
        if (f.getCarbohydrate() != null && f.getCarbohydrate() > 100) return false;
        if (f.getFat() != null && f.getFat() > 100) return false;
        if (f.getProtein() != null && f.getProtein() > 100) return false;
        if (f.getProtein() != null && f.getFat() != null && f.getCarbohydrate() != null
                && f.getProtein() + f.getFat() + f.getCarbohydrate() > 105) return false;
        return true;
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
