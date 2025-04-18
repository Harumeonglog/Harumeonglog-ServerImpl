package com.example.harumeonglog.domain.auth.service;

public interface TokenQueryService {
    boolean isValid(String token);
    Long getUserId(String token);
}
