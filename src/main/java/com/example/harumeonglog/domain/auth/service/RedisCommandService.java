package com.example.harumeonglog.domain.auth.service;

public interface RedisCommandService {
    void addRefreshToken(Long id, String refresh);
    void addBlackList(String token);
    void deleteRefreshToken(Long userId);
    void deleteBlackList(String accessToken);

}
