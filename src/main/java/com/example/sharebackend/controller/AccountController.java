package com.example.sharebackend.controller;


import com.example.sharebackend.domain.Account;
import com.example.sharebackend.mapper.AccountMapper;
import com.example.sharebackend.mapper.VerifyMapper;
import com.example.sharebackend.request.AccountRequest;
import com.example.sharebackend.response.AccountResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.SecureRandom;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class AccountController {
    final AccountMapper accountMapper;
    final VerifyMapper verifyMapper;
    final JavaMailSender javaMailSender;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    SecureRandom random = new SecureRandom();

    @PostMapping("/signup")
    public AccountResponse AccountSignup(@Valid @RequestBody AccountRequest asr,
                                         BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            System.out.println("id? " + result.hasFieldErrors("id"));
            System.out.println("nickname? " + result.hasFieldErrors("nickname"));
            System.out.println("pw? " + result.hasFieldErrors("pw"));

            result.getFieldErrors().forEach(error ->
                    System.out.println(error.getField() + " : " + error.getDefaultMessage()));

            return AccountResponse.builder().success(false).build();
        }

        Account account = asr.toAccount(passwordEncoder.encode(asr.getPw()));
        int r = accountMapper.insertOne(account);
        System.out.println("회원 정보 저장 : " + r);
        if (result.hasErrors()) {
            System.out.println("accountId ?" + result.hasFieldErrors("accountId"));
            System.out.println("code ?" + result.hasFieldErrors("code"));
            result.getFieldErrors().forEach(error ->
                    System.out.println(error.getField() + " : " + error.getDefaultMessage()));

            return AccountResponse.builder().success(false).build();
        }

        //'a'자체가 아스키코드 97이며 랜덤으로 0~26까지 뽑아서 두 수의 합으로 나온 아스키코드 값을 char로 변환
        char lower = (char) ('a' + random.nextInt(26));
        // '0'아스키코드는 48이고 0~9 중 하나를 뽑아 두 수의 합으로 나온 아스키코드 값을 char로 변환
        char num = (char) ('0' + random.nextInt(10));


        String chars = "abcdefghijklmnopqrstuvwxyz0123456789"; // 랜덤으로 뽑을 문자 후보 문자열
        //StringBuilder - 긴문자열을 반복문 돌리고 랜덤 문자 여러개 붙이기 위해 사용
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            // 반복문 4번 돌면서 char길에에서 랜덤으로 값을 뽑아 문자로 바꾸고 sb에 넣는다.
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        // char + char은 문자 더하기가 아니라 아스키코드인 숫자 연사 뎃셈이다
        // 앞에 ""(빈문자열)을 넣어 주면 문자열 + 문자로 시작되어 뒤에 붙는 값이 전부 문자열로 변환
        String code = "" + lower + num + sb;

        int b = verifyMapper.insertCode(asr.getId(), code);
        System.out.println("코드 저장 :" + b);

        SimpleMailMessage mailMessage = new SimpleMailMessage();
        mailMessage.setTo(asr.getId());
        mailMessage.setSubject("이메일 인증 코드");
        mailMessage.setText("안녕하세요. "+ asr.getNickname() +"님!\n\n" +
                "아래 인증 코드를 입력해 회원가입 절차를 완료해주세요.\n\n" +
                "인증코드 : "+ code +"\n\n" + "감사합니다.");
        javaMailSender.send(mailMessage);

        return AccountResponse.builder().success(true).account(account).build();
    }

}
