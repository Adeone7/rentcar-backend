package com.example.sharebackend.response;

import lombok.*;

import java.time.Year;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOfferAllImages {
    int idx;
    String nickname;
    int rentalPrice;
    String description;
    String corporation;
    String modelName;
    String carType;
    Year modelYear;
    int fewSeats;
    String gearType;
    List<String> images;
}
