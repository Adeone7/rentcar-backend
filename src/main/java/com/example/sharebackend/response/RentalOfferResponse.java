package com.example.sharebackend.response;

import com.example.sharebackend.domain.*;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOfferResponse {
    boolean success;
    String message;
    int total;
    RentalOfferAddReviewResponse rentalOfferAddReview;
    List<Review> reviewList;
    List<CarImg> rentalOfferCarImg;
    List<RentalOfferReviewResponse> rentalOfferReview;
    List<RentalOfferDayListResponse> rentalOfferDayList;
    List<RentalOfferReviewListResponse> rentalOfferReviewListResponses;
}
