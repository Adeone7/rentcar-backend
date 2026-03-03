package com.example.sharebackend.response;

import com.example.sharebackend.domain.RentalOffer;
import com.example.sharebackend.domain.Reservation;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationWithReview {
    Reservation reservation;
    RentalOffer rentalOffer;
    String reservationStatus;
    boolean hasReview;
}
