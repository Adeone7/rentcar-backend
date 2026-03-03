package com.example.sharebackend.response;

import com.example.sharebackend.domain.Account;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AccountResponse {
    boolean success;
    String message;
    Account account;
}
