package com.example.sharebackend.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Verify {
    String accountId;
    String code;
}
