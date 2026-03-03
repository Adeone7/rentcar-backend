package com.example.sharebackend.request;

import com.example.sharebackend.domain.Account;
import com.example.sharebackend.domain.RentalOffer;
import com.example.sharebackend.domain.Reservation;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
public class ReservationRequest {
    int idx;
    String accountId;
    int rentalOfferIdx;
    LocalDate startDate;
    LocalDate endDate;



    public Reservation toReservation() {
        Reservation reservation = new Reservation();
        reservation.setRentalOfferIdx(this.rentalOfferIdx);
        reservation.setStartDate(this.startDate);
        reservation.setEndDate(this.endDate);
        return reservation;
    }

}

