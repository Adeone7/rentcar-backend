package com.example.sharebackend.response;

import com.example.sharebackend.domain.Car;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;


@Setter
@Getter
@Builder
public class CarAddResponse {
    boolean success;
    Car car;
}
