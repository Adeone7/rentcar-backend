package com.example.sharebackend.response;

import com.example.sharebackend.domain.Car;
import com.example.sharebackend.domain.RentalOffer;
import com.example.sharebackend.domain.Reservation;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationResponse {
    boolean success;
    String message;
    Reservation reservation;
    RentalOffer rentalOffer;
    String reservationStatus;
}
