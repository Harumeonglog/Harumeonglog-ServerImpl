package com.example.harumeonglog.global.security.filter;

import com.example.harumeonglog.domain.auth.service.TokenCommandService;
import com.example.harumeonglog.domain.auth.service.TokenQueryService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class JwtTokenLogoutFilter extends AbstractTokenLogoutFilter {

    private final TokenQueryService tokenQueryService;
    private final TokenCommandService tokenCommandService;

    public JwtTokenLogoutFilter(AbstractTokenFilter abstractTokenFilter, LogoutSuccessHandler logoutSuccessHandler, TokenQueryService tokenQueryService, TokenCommandService tokenCommandService) {
        super(abstractTokenFilter, logoutSuccessHandler);
        this.tokenQueryService = tokenQueryService;
        this.tokenCommandService = tokenCommandService;
    }


    @Override
    protected void processLogout(HttpServletRequest request, HttpServletResponse response, String token) {
    }
}
