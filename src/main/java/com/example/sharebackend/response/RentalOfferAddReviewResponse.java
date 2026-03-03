package com.example.sharebackend.response;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Year;

@Setter
@Getter
public class RentalOfferAddReviewResponse {
    int idx;
    // account
    String nickname;

    // rental_offer
    String accountId;
    int rentalPrice;
    String description;

    // car
    String corporation;
    String modelName;
    String carType;
    Year modelYear;
    int fewSeats;
    String gearType;
}
