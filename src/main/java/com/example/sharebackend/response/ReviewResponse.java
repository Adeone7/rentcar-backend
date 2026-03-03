package com.example.sharebackend.response;

import com.example.sharebackend.domain.Reservation;
import com.example.sharebackend.domain.Review;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponse {
    boolean success;
    String message;
    Review review;
    List<Review> reviewList;
    List<Reservation> reservationList;
    List<RentalOfferReviewListResponse> myReviewList;
}
