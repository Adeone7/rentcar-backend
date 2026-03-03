package com.example.sharebackend.domain;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Review {
    int idx;
    int reservationIdx;
    String content;
    int starRating;
}
