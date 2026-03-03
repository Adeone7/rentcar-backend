package com.example.sharebackend.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
public class RentalOfferDayRequest {
    LocalDate startDate;
    LocalDate endDate;


}
