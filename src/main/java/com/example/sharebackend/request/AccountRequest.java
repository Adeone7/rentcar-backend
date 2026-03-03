package com.example.sharebackend.request;

import com.example.sharebackend.domain.Account;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class AccountRequest {
    @NotNull
    @NotBlank
    @Email
    String id;

    @NotNull
    @NotBlank
    @Size(min = 3, max = 10)
    String nickname;

    @NotNull
    @NotBlank
    @Pattern(regexp = "(?=.*[!@#$%^&*()<>?{}])(?=.*[a-z])(?=.*\\d).{8,20}")
    String pw;



    public Account toAccount(String pw) {
        Account account = new Account();
        account.setId(this.id);
        account.setNickname(this.nickname);
        account.setPw(pw);
        return account;
    }
}
