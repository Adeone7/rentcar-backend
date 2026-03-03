package com.example.sharebackend.response;

import com.example.sharebackend.domain.Car;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarChartResultResponse {
    int idx;
    String corporation;
    String modelName;
    int count;

}
