package com.example.sharebackend.request;

import lombok.Getter;
import lombok.Setter;

import java.time.Year;

@Setter
@Getter
public class CarAddRequest {
    String corporation;
    String modelName;
    String carType;
    Year modelYear;
    int fewSeats;
    String gearType;
}

