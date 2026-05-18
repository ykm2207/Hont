package com.hont.hont_backend.profile.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

// 사용자가 보유할 수 있는 질환 목록
// 운동/식단 중 관련 경고 표시에 활용
@Getter
@RequiredArgsConstructor
public enum HealthCondition {

    DIABETES("당뇨"),
    HYPERTENSION("고혈압"),
    HYPERLIPIDEMIA("고지혈증"),
    HEART_DISEASE("심장질환"),
    KIDNEY_DISEASE("신장질환"),
    ARTHRITIS("관절염"),
    OSTEOPOROSIS("골다공증"),
    ASTHMA("천식"),
    OBESITY("비만"),
    ANEMIA("빈혈");

    private final String displayName;
}
