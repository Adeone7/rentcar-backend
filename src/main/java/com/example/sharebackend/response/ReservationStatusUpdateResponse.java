package com.example.sharebackend.response;

import com.example.sharebackend.domain.Reservation;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationStatusUpdateResponse {
    boolean success;
    String message;
    Reservation  reservation;
}
