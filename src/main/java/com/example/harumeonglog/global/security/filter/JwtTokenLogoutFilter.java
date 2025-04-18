package com.example.harumeonglog.global.security.filter;

import com.example.harumeonglog.domain.auth.service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class JwtTokenLogoutFilter extends AbstractTokenLogoutFilter {

    private final TokenQueryService tokenQueryService;
    private final RedisQueryService redisQueryService;
    private final RedisCommandService redisCommandService;

    public JwtTokenLogoutFilter(AbstractTokenFilter abstractTokenFilter, LogoutSuccessHandler logoutSuccessHandler, TokenQueryService tokenQueryService, RedisCommandService redisCommandService, RedisQueryService redisQueryService) {
        super(abstractTokenFilter, logoutSuccessHandler);
        this.tokenQueryService = tokenQueryService;
        this.redisQueryService = redisQueryService;
        this.redisCommandService = redisCommandService;
    }


    @Override
    protected void processLogout(HttpServletRequest request, HttpServletResponse response, String token) {
        Long id = tokenQueryService.getUserId(token);
        String refreshToken = redisQueryService.getRefreshToken(id);
        redisCommandService.addBlackList(token);
        redisCommandService.deleteRefreshToken(id);
        redisCommandService.addBlackList(refreshToken);
    }
}
