package com.hont.hont_backend.profile.service;

import com.hont.hont_backend.auth.entity.User;
import com.hont.hont_backend.auth.repository.UserRepository;
import com.hont.hont_backend.profile.dto.UserProfileRequest;
import com.hont.hont_backend.profile.dto.UserProfileResponse;
import com.hont.hont_backend.profile.entity.UserProfile;
import com.hont.hont_backend.profile.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserProfileService {

    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;

    // 프로필 조회 (UserProfile이 없어도 User 기본 정보 반환)
    public UserProfileResponse getProfile(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));
        UserProfile profile = userProfileRepository.findByUserId(userId).orElse(null);
        return new UserProfileResponse(user, profile);
    }

    // 프로필 수정 (UserProfile이 없으면 새로 생성)
    @Transactional
    public UserProfileResponse updateProfile(Long userId, UserProfileRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("유저를 찾을 수 없습니다"));

        UserProfile profile = userProfileRepository.findByUserId(userId)
                .orElseGet(() -> userProfileRepository.save(
                        UserProfile.builder().user(user).build()
                ));

        profile.update(
                request.getNickname(),
                request.getProfileImageUrl(),
                request.getHeight(),
                request.getWeight(),
                request.getTargetHeight(),
                request.getTargetWeight(),
                request.getTargetCalories(),
                request.getConditions()
        );

        return new UserProfileResponse(user, profile);
    }
}
