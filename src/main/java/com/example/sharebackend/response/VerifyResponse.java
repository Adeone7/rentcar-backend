package com.example.sharebackend.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VerifyResponse {
    boolean success;
    String message;
}
