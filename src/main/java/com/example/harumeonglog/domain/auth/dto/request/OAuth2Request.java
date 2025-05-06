package com.example.harumeonglog.domain.auth.dto.request;

import com.example.harumeonglog.domain.member.entity.enums.SocialType;
import lombok.Builder;
import lombok.Getter;

public class OAuth2Request {

    @Getter
    @Builder
    public static class OAuth2LoginRequest {
        private String email;
        private String nickname;
        private SocialType socialType;
        private String image;
        private String providerId;
    }
}
