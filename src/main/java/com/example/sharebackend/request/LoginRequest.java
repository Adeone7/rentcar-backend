package com.example.sharebackend.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class LoginRequest {
    @NotBlank
    @NotNull
    @Email
    String id;

    @NotBlank
    @NotNull
    @Pattern(regexp = "(?=.*[!@#$%^&*()<>?{}])(?=.*[a-z])(?=.*\\d).{8,20}")
    String pw;
}
