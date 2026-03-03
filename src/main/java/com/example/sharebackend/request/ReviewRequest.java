package com.example.sharebackend.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReviewRequest {
    @NotNull (message = "예약한 해당 차 선택")
    int reservationIdx;

    String content;
    int starRating;


}
