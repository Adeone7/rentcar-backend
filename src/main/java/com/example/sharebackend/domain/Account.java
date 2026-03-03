package com.example.sharebackend.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Account {
    String id;
    String nickname;
    String pw;
    RentalOffer rentalOffer;
}
