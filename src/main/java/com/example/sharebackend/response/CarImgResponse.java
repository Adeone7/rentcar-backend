package com.example.sharebackend.response;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CarImgResponse {
    int idx;
    int rentalOfferIdx;
    String img;
}
