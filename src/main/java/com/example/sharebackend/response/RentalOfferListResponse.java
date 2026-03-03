package com.example.sharebackend.response;

import com.example.sharebackend.domain.CarImg;
import com.example.sharebackend.domain.RentalOffer;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOfferListResponse {
    boolean success;
    String message;
    int countAllRentalOffer;
    List<RentalOfferAllImages> rentalOfferListResponse;
}
