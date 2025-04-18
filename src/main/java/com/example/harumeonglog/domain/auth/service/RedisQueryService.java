package com.example.harumeonglog.domain.auth.service;

import org.springframework.security.core.userdetails.UserDetails;

public interface RedisQueryService {
    boolean isBlackList(String token);
    String getRefreshToken(Long id);
}
