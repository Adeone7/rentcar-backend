package com.example.sharebackend.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.sharebackend.domain.Account;
import com.example.sharebackend.mapper.AccountMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTVerifyFilter extends OncePerRequestFilter {

    final AccountMapper accountMapper;

    public JWTVerifyFilter(AccountMapper accountMapper) {
        this.accountMapper = accountMapper;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();   // 요청 주소
        String method = request.getMethod();

        if (uri.equals("/signup")
                || uri.equals("/login")
                || uri.equals("/verify-email")
                || method.equalsIgnoreCase("options")
                || uri.equals("/rental-offer/day")
                || uri.startsWith("/car-images")
                || uri.equals("/car/chart")
                || uri.equals("/car/month/chart")) {
            return true;
        }

        if (method.equalsIgnoreCase("Get")&& uri.equals("/rental-offer")) {
            return true;
        }
        if (method.equalsIgnoreCase("Get") && uri.matches("/rental-offer/\\d+")) {
            return true;
        }
        return false;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
//                response.getWriter().println("{\"success\":false, \"cause\":\"Token is null\"}");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        String token = authHeader.substring(7);

        DecodedJWT jwt;
        try {
            jwt = JWT.require(Algorithm.HMAC256("secretKey")).withIssuer("carshare").build().verify(token);
        } catch (Exception e) {
//                response.getWriter().println("{\"success\": false}");
//                return;
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        String subject = jwt.getSubject();   // auth할 때 인증했던 계정의 id를 설정해서 보내뒀음.
        Account account = accountMapper.findById(subject);
        if (account == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        request.setAttribute("currentAccountId", account.getId());
        request.setAttribute("currentNickname", account.getNickname());
        filterChain.doFilter(request, response);
    }
}