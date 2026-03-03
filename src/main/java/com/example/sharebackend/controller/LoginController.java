package com.example.sharebackend.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.sharebackend.domain.Account;
import com.example.sharebackend.mapper.AccountMapper;
import com.example.sharebackend.request.LoginRequest;
import com.example.sharebackend.response.LoginResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class LoginController {
    final AccountMapper accountMapper;

    @PostMapping("/login")
    public LoginResponse loginHandle(@Valid @RequestBody LoginRequest lrt){
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        Account account = accountMapper.selectAll(lrt.getId());

        if(account != null && passwordEncoder.matches(lrt.getPw(), account.getPw())){
            String token = JWT.create().withSubject(String.valueOf(account.getId()))
                    .withIssuedAt(new Date()).withIssuer("carshare")
                    .sign(Algorithm.HMAC256("secretKey"));

            return  LoginResponse.builder().success(true).message("로그인 성공").account(account).auth(token).build();

        }
        return LoginResponse.builder().success(false)
                .message("로그인 실패했습니다. 아이디와 비밀번호를 확인하세요.").build();

    }

}
