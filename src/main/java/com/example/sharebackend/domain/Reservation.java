package com.example.sharebackend.domain;

import lombok.Getter;
import lombok.Setter;
import org.apache.ibatis.type.Alias;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Setter
@Getter
public class Reservation {
    int idx;
    String accountId;
    String nickname;
    int rentalOfferIdx;
    LocalDate startDate;
    LocalDate endDate;
    String reservationStatus;
    int paymentAmount;
    RentalOffer rentalOffer;
    boolean hasReview;

    Car car;
    CarImg carImg;

}