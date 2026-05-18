package com.hont.hont_backend.profile.dto;

import com.hont.hont_backend.auth.entity.User;
import com.hont.hont_backend.profile.entity.HealthCondition;
import com.hont.hont_backend.profile.entity.UserProfile;
import lombok.Getter;

import java.util.Set;

@Getter
public class UserProfileResponse {

    private final Long userId;
    private final String email;

    // 프로필에 닉네임이 있으면 우선 사용, 없으면 OAuth 닉네임
    private final String nickname;
    private final String profileImageUrl;

    private final Double height;
    private final Double weight;
    private final Double targetHeight;
    private final Double targetWeight;
    private final Integer targetCalories;
    private final Set<HealthCondition> conditions;

    // BMI 자동 계산 (키/몸무게 있을 때만)
    private final Double bmi;

    public UserProfileResponse(User user, UserProfile profile) {
        this.userId = user.getId();
        this.email = user.getEmail();

        if (profile != null) {
            this.nickname = profile.getNickname() != null ? profile.getNickname() : user.getNickname();
            this.profileImageUrl = profile.getProfileImageUrl() != null
                    ? profile.getProfileImageUrl() : user.getProfileImage();
            this.height = profile.getHeight();
            this.weight = profile.getWeight();
            this.targetHeight = profile.getTargetHeight();
            this.targetWeight = profile.getTargetWeight();
            this.targetCalories = profile.getTargetCalories();
            this.conditions = profile.getConditions();
            this.bmi = calcBmi(profile.getHeight(), profile.getWeight());
        } else {
            this.nickname = user.getNickname();
            this.profileImageUrl = user.getProfileImage();
            this.height = null;
            this.weight = null;
            this.targetHeight = null;
            this.targetWeight = null;
            this.targetCalories = null;
            this.conditions = Set.of();
            this.bmi = null;
        }
    }

    private Double calcBmi(Double height, Double weight) {
        if (height == null || weight == null || height == 0) return null;
        double heightM = height / 100.0;
        return Math.round((weight / (heightM * heightM)) * 10.0) / 10.0;
    }
}
