package com.example.sharebackend.request;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class VerifyRequest {
    String accountId;
    @Pattern(regexp = "(?=.*[a-z])(?=.*\\d).{6}")
    String code;
}
