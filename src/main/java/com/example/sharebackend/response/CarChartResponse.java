package com.example.sharebackend.response;

import com.example.sharebackend.domain.Car;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarChartResponse {
    boolean success;
    String message;
    int total;
    List<Car> cars;
    List<CarChartResultResponse> carChartResponseList;

}
