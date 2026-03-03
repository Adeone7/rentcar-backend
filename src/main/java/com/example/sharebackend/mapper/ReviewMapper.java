package com.example.sharebackend.mapper;

import com.example.sharebackend.domain.Review;
import com.example.sharebackend.response.RentalOfferResponse;
import com.example.sharebackend.response.RentalOfferReviewListResponse;
import org.apache.ibatis.annotations.Param;
import com.example.sharebackend.response.RentalOfferReviewResponse;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface ReviewMapper {
    // 리뷰 작성
    int insertOne(Review  review);

    // reservationIdx에 해당하는 모든 리뷰 조회
    List<Review> selectByReservationIdxList(int  reservationIdx);

    //  rentalOfferIdx에 해당하는 모든 리뷰 조회
    List<RentalOfferReviewListResponse> selectByRentalOfferIdx(int rentalOfferIdx);

    // 내가 작성한 리뷰 전체 조회
    List<RentalOfferReviewListResponse> selectMyReviews(String accountId);

    // 리뷰 수정
    int updateReview(Review review);

    // 리뷰 삭제 (본인 예약만)
    int deleteReview(@Param("reviewIdx") int reviewIdx, @Param("accountId") String accountId);
}