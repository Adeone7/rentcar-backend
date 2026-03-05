package com.example.sharebackend.controller;

import com.example.sharebackend.domain.Account;
import com.example.sharebackend.mapper.AccountMapper;
import com.example.sharebackend.mapper.VerifyMapper;
import com.example.sharebackend.request.AccountRequest;
import com.example.sharebackend.response.AccountResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.SecureRandom;


@RestController
@RequiredArgsConstructor
@RequestMapping
@CrossOrigin
public class AccountController {
    final AccountMapper accountMapper;
    final VerifyMapper verifyMapper;

    BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    SecureRandom random = new SecureRandom();

    private static final String BREVO_API_KEY = "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";

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

        // Brevo API로 이메일 발송
        try {
            String emailBody = String.format(
                    "{\"sender\":{\"name\":\"TOCAR\",\"email\":\"waryz6422@gmail.com\"}," +
                            "\"to\":[{\"email\":\"%s\",\"name\":\"%s\"}]," +
                            "\"subject\":\"이메일 인증 코드\"," +
                            "\"textContent\":\"안녕하세요. %s님!\\n\\n아래 인증 코드를 입력해 회원가입 절차를 완료해주세요.\\n\\n인증코드 : %s\\n\\n감사합니다.\"}",
                    asr.getId(), asr.getNickname(), asr.getNickname(), code
            );

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://api.brevo.com/v3/smtp/email"))
                    .header("accept", "application/json")
                    .header("api-key", BREVO_API_KEY)
                    .header("content-type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(emailBody))
                    .build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("이메일 발송 결과 : " + response.statusCode() + " " + response.body());

        } catch (Exception e) {
            System.out.println("이메일 발송 실패 : " + e.getMessage());
        }

        return AccountResponse.builder().success(true).account(account).build();
    }

}