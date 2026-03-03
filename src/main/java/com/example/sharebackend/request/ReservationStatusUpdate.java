package com.example.sharebackend.request;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ReservationStatusUpdate {
    int reservationIdx;
    String accountId;
}
