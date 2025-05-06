package com.example.harumeonglog.domain.auth.converter;

import com.example.harumeonglog.domain.auth.dto.request.OAuth2Request;
import com.example.harumeonglog.domain.member.entity.Member;
import com.example.harumeonglog.domain.member.entity.enums.SocialType;

public class OAuth2Converter {

    public static OAuth2Request.OAuth2LoginRequest toOAuth2LoginRequest(String email, String nickname, SocialType socialType, String image, String providerId) {
        return OAuth2Request.OAuth2LoginRequest.builder()
                .email(email)
                .nickname(nickname)
                .socialType(socialType)
                .image(image)
                .providerId(providerId)
                .build();
    }

    public static Member toMember(OAuth2Request.OAuth2LoginRequest request) {
        return Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .socialType(request.getSocialType())
                .image(request.getImage())
                .providerId(request.getProviderId())
                .build();
    }
}
