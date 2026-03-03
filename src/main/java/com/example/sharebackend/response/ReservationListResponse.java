package com.example.sharebackend.response;

import com.example.sharebackend.domain.CarImg;
import com.example.sharebackend.domain.Reservation;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReservationListResponse {
    boolean success;
    String message;
    int total;
    List<ReservationWithReview> reservations;
    List<Reservation> reservationsWithReviews;
    boolean hasReview;
}
