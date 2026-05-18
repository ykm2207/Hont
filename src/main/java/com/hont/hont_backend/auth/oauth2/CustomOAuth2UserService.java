package com.hont.hont_backend.auth.oauth2;

import com.hont.hont_backend.auth.entity.User;
import com.hont.hont_backend.auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(userRequest);

        String provider = userRequest.getClientRegistration().getRegistrationId(); // google, kakao
        Map<String, Object> attributes = oAuth2User.getAttributes();

        OAuth2UserInfo userInfo = switch (provider) {
            case "google" -> new GoogleUserInfo(attributes);
            case "kakao" -> new KakaoUserInfo(attributes);
            default -> throw new OAuth2AuthenticationException("지원하지 않는 소셜 로그인: " + provider);
        };

        // DB에 없으면 자동 회원가입
        User user = userRepository.findByProviderAndProviderId(provider, userInfo.getProviderId())
                .orElseGet(() -> userRepository.save(User.builder()
                        .provider(provider)
                        .providerId(userInfo.getProviderId())
                        .email(userInfo.getEmail())
                        .nickname(userInfo.getNickname())
                        .profileImage(userInfo.getProfileImage())
                        .build()));

        // 프로필 정보 최신화
        user.updateProfile(userInfo.getNickname(), userInfo.getProfileImage());

        return new CustomOAuth2User(user, attributes);
    }
}
