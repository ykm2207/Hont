package com.hont.hont_backend.profile.dto;

import com.hont.hont_backend.profile.entity.HealthCondition;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserProfileRequest {

    private String nickname;
    private String profileImageUrl;
    private Double height;
    private Double weight;
    private Double targetHeight;
    private Double targetWeight;
    private Integer targetCalories;

    // null이면 질환 목록 변경 없음, 빈 Set이면 전체 삭제
    private Set<HealthCondition> conditions;
}
