package com.example.sharebackend.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.Year;

@Setter
@Getter
public class RentalOffer {
    String nickName;

    int idx;
    String accountId;
    int carIdx;
    int rentalPrice;
    String description;

    Car car;


    String img;
}
