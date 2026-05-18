package com.hont.hont_backend.profile.entity;

import com.hont.hont_backend.auth.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_profiles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    // 앱 내 닉네임 (OAuth 닉네임 덮어씀)
    @Column(length = 30)
    private String nickname;

    // 프로필 이미지 URL
    @Column(length = 500)
    private String profileImageUrl;

    // 현재 신체 정보
    private Double height;      // 키 (cm)
    private Double weight;      // 몸무게 (kg)

    // 목표 신체 정보
    private Double targetHeight;
    private Double targetWeight;

    // 목표 칼로리 (kcal/일)
    private Integer targetCalories;

    // 보유 질환 목록 (선택, 운동/식단 경고에 활용)
    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "user_conditions",
            joinColumns = @JoinColumn(name = "user_profile_id")
    )
    @Enumerated(EnumType.STRING)
    @Column(name = "condition_type")
    @Builder.Default
    private Set<HealthCondition> conditions = new HashSet<>();

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public void update(String nickname, String profileImageUrl,
                       Double height, Double weight,
                       Double targetHeight, Double targetWeight,
                       Integer targetCalories, Set<HealthCondition> conditions) {
        if (nickname != null) this.nickname = nickname;
        if (profileImageUrl != null) this.profileImageUrl = profileImageUrl;
        if (height != null) this.height = height;
        if (weight != null) this.weight = weight;
        if (targetHeight != null) this.targetHeight = targetHeight;
        if (targetWeight != null) this.targetWeight = targetWeight;
        if (targetCalories != null) this.targetCalories = targetCalories;
        if (conditions != null) this.conditions = conditions;
    }
}
