package com.example.harumeonglog.domain.auth.service;

import com.example.harumeonglog.global.security.domain.CustomUserDetails;

public interface OAuth2Service {
    CustomUserDetails login(String idToken);
}
