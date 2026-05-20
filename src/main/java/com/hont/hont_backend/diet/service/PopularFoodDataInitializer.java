package com.hont.hont_backend.diet.service;

import com.hont.hont_backend.diet.entity.PopularFood;
import com.hont.hont_backend.diet.repository.PopularFoodRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 앱 시작 시 인기 음식 기본 데이터 삽입 (이미 데이터가 있으면 스킵)
 * 영양정보는 100g 기준
 */
@Component
@RequiredArgsConstructor
public class PopularFoodDataInitializer implements CommandLineRunner {

    private final PopularFoodRepository popularFoodRepository;

    @Override
    public void run(String... args) {
        if (popularFoodRepository.count() > 0) return;

        List<PopularFood> foods = List.of(
            food("흰쌀밥",       "100g", 130.0, 28.1,  2.7,  0.3),
            food("잡곡밥",       "100g", 165.0, 34.4,  3.8,  0.9),
            food("현미밥",       "100g", 165.0, 34.4,  3.5,  1.0),
            food("김치찌개",     "100g",  46.0,  2.8,  4.1,  1.6),
            food("된장찌개",     "100g",  44.0,  3.4,  3.7,  1.2),
            food("순두부찌개",   "100g",  63.0,  3.1,  5.0,  3.2),
            food("부대찌개",     "100g",  89.0,  5.5,  7.0,  4.5),
            food("미역국",       "100g",  22.0,  1.6,  2.8,  0.6),
            food("콩나물국",     "100g",  17.0,  1.4,  2.2,  0.3),
            food("삼겹살",       "100g", 331.0,  0.0, 17.4, 28.4),
            food("목살",         "100g", 290.0,  0.0, 19.0, 23.0),
            food("닭가슴살",     "100g", 165.0,  0.0, 31.0,  3.6),
            food("달걀후라이",   "100g", 196.0,  0.8, 13.5, 14.8),
            food("삶은달걀",     "100g", 155.0,  1.1, 13.0, 11.0),
            food("스크램블에그", "100g", 149.0,  1.6, 10.0, 11.5),
            food("김치",         "100g",  18.0,  3.4,  1.8,  0.5),
            food("깍두기",       "100g",  24.0,  5.2,  1.0,  0.3),
            food("시금치나물",   "100g",  35.0,  3.5,  3.0,  1.0),
            food("콩나물무침",   "100g",  28.0,  3.8,  2.8,  0.3),
            food("두부조림",     "100g", 130.0,  6.2,  9.7,  7.5),
            food("고등어구이",   "100g", 177.0,  0.0, 21.5,  9.7),
            food("연어",         "100g", 206.0,  0.0, 27.3, 10.4),
            food("참치통조림",   "100g", 198.0,  0.0, 29.2,  9.0),
            food("닭볶음탕",     "100g", 166.0,  8.2, 14.3,  7.9),
            food("불고기",       "100g", 218.0,  7.5, 20.0, 11.5),
            food("비빔밥",       "100g", 143.0, 25.0,  5.6,  3.1),
            food("볶음밥",       "100g", 185.0, 30.0,  5.5,  5.2),
            food("라면",         "100g", 160.0, 24.5,  4.5,  5.0),
            food("냉면",         "100g", 108.0, 22.0,  3.4,  0.8),
            food("칼국수",       "100g", 116.0, 21.5,  4.2,  1.6),
            food("떡볶이",       "100g", 179.0, 36.0,  4.5,  2.5),
            food("순대",         "100g", 182.0, 13.4, 10.5, 10.0),
            food("떡국",         "100g", 180.0, 35.0,  6.0,  2.0),
            food("오므라이스",   "100g", 192.0, 28.0,  8.0,  6.5),
            food("짜장면",       "100g", 168.0, 29.0,  5.8,  3.8),
            food("짬뽕",         "100g",  87.0, 11.8,  6.0,  2.0),
            food("탕수육",       "100g", 245.0, 21.5, 13.5, 11.5),
            food("잡채",         "100g", 142.0, 20.5,  4.5,  5.0),
            food("족발",         "100g", 295.0,  0.5, 25.0, 20.5),
            food("보쌈",         "100g", 185.0,  1.8, 22.0, 10.0),
            food("해물파전",     "100g", 176.0, 22.0,  7.5,  6.5),
            food("닭발",         "100g", 220.0,  3.0, 21.0, 14.0),
            food("두부",         "100g",  76.0,  2.0,  8.1,  4.2),
            food("바나나",       "100g",  93.0, 23.0,  1.1,  0.3),
            food("사과",         "100g",  57.0, 15.3,  0.3,  0.1),
            food("고구마",       "100g", 128.0, 31.0,  1.5,  0.1),
            food("감자",         "100g",  73.0, 17.0,  1.7,  0.1),
            food("우유",         "100g",  61.0,  4.7,  3.2,  3.4),
            food("요거트",       "100g",  62.0,  6.5,  5.4,  1.6),
            food("오트밀",       "100g", 389.0, 66.3, 16.9,  6.9)
        );

        popularFoodRepository.saveAll(foods);
    }

    private PopularFood food(String name, String serving,
                             double cal, double carb, double protein, double fat) {
        return PopularFood.builder()
                .foodName(name)
                .servingSize(serving)
                .calories(cal)
                .carbohydrate(carb)
                .protein(protein)
                .fat(fat)
                .build();
    }
}
