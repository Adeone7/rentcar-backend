package com.example.sharebackend.controller;

import com.example.sharebackend.domain.Verify;
import com.example.sharebackend.mapper.AccountMapper;
import com.example.sharebackend.mapper.VerifyMapper;
import com.example.sharebackend.request.VerifyRequest;
import com.example.sharebackend.response.VerifyResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class VerifyController {
    final AccountMapper accountMapper;
    final VerifyMapper verifyMapper;


    @PostMapping("verify-email")
    public VerifyResponse VerifyEmail(@Valid @RequestBody VerifyRequest vrt,
                                      BindingResult result) {
        VerifyResponse verifyResponse = new VerifyResponse();
        if(result.hasErrors()){
            System.out.println("code ? " + result.hasFieldErrors("code"));
            return verifyResponse;
        }

        Verify verify = verifyMapper.selectByAccountId(vrt.getAccountId());

        if(verify.getCode().equals(vrt.getCode()) && vrt.getAccountId().equals(verify.getAccountId())) {
            int c = accountMapper.activeUpdate(verify.getAccountId());
            System.out.println("인증 성공 " + c);

            return VerifyResponse.builder().success(true).build();
        }

        return VerifyResponse.builder().success(false).build();

    }
}
