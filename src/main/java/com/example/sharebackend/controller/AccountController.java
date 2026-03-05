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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.SecureRandom;


@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class AccountController {
    final AccountMapper accountMapper;
    final VerifyMapper verifyMapper;
    final JavaMailSender mailSender;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    SecureRandom random = new SecureRandom();

    @PostMapping("/signup")
    public AccountResponse AccountSignup(@Valid @RequestBody AccountRequest asr,
                                         BindingResult result, HttpSession session) {

        if (result.hasErrors()) {
            result.getFieldErrors().forEach(error ->
                    System.out.println(error.getField() + " : " + error.getDefaultMessage()));
            return AccountResponse.builder().success(false).build();
        }

        Account account = asr.toAccount(passwordEncoder.encode(asr.getPw()));
        int r = accountMapper.insertOne(account);
        System.out.println("회원 정보 저장 : " + r);

        char lower = (char) ('a' + random.nextInt(26));
        char num = (char) ('0' + random.nextInt(10));

        String chars = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 4; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        String code = "" + lower + num + sb;

        int b = verifyMapper.insertCode(asr.getId(), code);
        System.out.println("코드 저장 :" + b);

        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom("waryz6422@gmail.com");
            message.setTo(asr.getId());
            message.setSubject("이메일 인증 코드");
            message.setText(String.format(
                    "안녕하세요. %s님!\n\n아래 인증 코드를 입력해 회원가입 절차를 완료해주세요.\n\n인증코드 : %s\n\n감사합니다.",
                    asr.getNickname(), code
            ));

            mailSender.send(message);
            System.out.println("이메일 발송 성공");

        } catch (Exception e) {
            System.out.println("이메일 발송 실패 : " + e.getMessage());
        }

        return AccountResponse.builder().success(true).account(account).build();
    }

}