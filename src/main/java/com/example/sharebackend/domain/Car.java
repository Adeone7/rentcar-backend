package com.example.sharebackend.domain;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.Year;

@Setter @Getter
public class Car {
    int idx;
    String corporation;
    String modelName;
    String carType;
    Year modelYear;
    int fewSeats;
    String gearType;
}
