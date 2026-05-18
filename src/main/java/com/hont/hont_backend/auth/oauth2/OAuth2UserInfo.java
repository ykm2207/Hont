package com.hont.hont_backend.auth.oauth2;

// 소셜 로그인 제공자별 사용자 정보 추출 인터페이스
public interface OAuth2UserInfo {
    String getProviderId();
    String getEmail();
    String getNickname();
    String getProfileImage();
}
