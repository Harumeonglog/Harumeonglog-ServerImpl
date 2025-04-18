package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.domain.auth.dto.request.AuthRequest;
import com.example.harumeonglog.domain.auth.dto.response.AuthResponse;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import com.example.harumeonglog.global.error.code.AuthErrorCode;
import com.example.harumeonglog.global.error.exception.AuthException;
import com.example.harumeonglog.global.security.domain.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthCommandServiceImpl implements AuthCommandService {

    private final TokenCommandService tokenCommandService;
    private final OAuth2Service appleOAuth2Service;
    private final OAuth2Service kakaoOAuth2Service;

    @Override
    public AuthResponse.AuthLoginResponse login(String provider, AuthRequest.AuthLoginRequest request) {
        CustomUserDetails userDetails;
        if (provider.equalsIgnoreCase(SocialType.KAKAO.name())) {
            userDetails = kakaoOAuth2Service.login(request.getIdToken());
        }
        else if (provider.equalsIgnoreCase(SocialType.APPLE.name())) {
            userDetails = appleOAuth2Service.login(request.getIdToken());
        }
        else {
            throw new AuthException(AuthErrorCode.UNSUPPORTED_PROVIDER);
        }
        return tokenCommandService.createToken(userDetails);
    }

    @Override
    public AuthResponse.AuthLogoutResponse logout(Member member) {
        return null;
    }
}
