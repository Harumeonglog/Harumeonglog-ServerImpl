package com.example.harumeonglog.domain.common.auth.filter;

import com.example.harumeonglog.domain.common.auth.util.JwtUtil;
import com.example.harumeonglog.domain.common.auth.util.RedisUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class JwtTokenLogoutFilter extends AbstractTokenLogoutFilter {

    private final RedisUtil redisUtil;
    private final JwtUtil jwtUtil;

    public JwtTokenLogoutFilter(AbstractTokenFilter abstractTokenFilter, LogoutSuccessHandler logoutSuccessHandler, RedisUtil redisUtil, JwtUtil jwtUtil) {
        super(abstractTokenFilter, logoutSuccessHandler);
        this.redisUtil = redisUtil;
        this.jwtUtil = jwtUtil;
    }


    @Override
    protected void processLogout(HttpServletRequest request, HttpServletResponse response, String token) {
        redisUtil.addBlackList(token, jwtUtil.getAccessExpiration());
    }
}
