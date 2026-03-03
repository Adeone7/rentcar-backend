package com.example.sharebackend.response;

import com.example.sharebackend.response.RentalOfferReviewResponse;
import com.example.sharebackend.domain.CarImg;
import com.example.sharebackend.domain.Review;
import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RentalOfferReviewResponse {
    boolean success;
    String message;
    List<Review> reviewList;
}
