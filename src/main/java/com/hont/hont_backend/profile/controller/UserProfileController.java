package com.hont.hont_backend.profile.controller;

import com.hont.hont_backend.common.response.ApiResponse;
import com.hont.hont_backend.profile.dto.UserProfileRequest;
import com.hont.hont_backend.profile.dto.UserProfileResponse;
import com.hont.hont_backend.profile.service.UserProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
public class UserProfileController {

    private final UserProfileService userProfileService;

    // 내 프로필 조회
    @GetMapping
    public ApiResponse<UserProfileResponse> getProfile(@AuthenticationPrincipal Long userId) {
        return ApiResponse.ok(userProfileService.getProfile(userId));
    }

    // 프로필 수정
    @PutMapping
    public ApiResponse<UserProfileResponse> updateProfile(
            @AuthenticationPrincipal Long userId,
            @RequestBody UserProfileRequest request) {
        return ApiResponse.ok(userProfileService.updateProfile(userId, request));
    }
}
