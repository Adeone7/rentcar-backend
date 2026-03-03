package com.example.sharebackend.response;

import lombok.*;

import java.time.LocalDate;
import java.time.Year;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOfferDayListResponse {
    int rentalOfferIdx;
    String nickname;
    int carIdx;
    String modelName;
    Year modelYear;
    int rentalPrice;
    String description;
    Integer carImgIdx;
    Integer carImgRentalOfferIdx;
    String imgUrl;
}
