package com.example.sharebackend.response;

import com.example.sharebackend.domain.CarImg;
import com.example.sharebackend.domain.RentalOffer;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOfferAddResponse {
    boolean success;
    String message;
    int registeredRentalOfferIdx;
    RentalOffer rentalOffer;
    String nickname;
    List<CarImg> carImg;
}
