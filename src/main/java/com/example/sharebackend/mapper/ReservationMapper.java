package com.example.sharebackend.mapper;

import com.example.sharebackend.domain.Reservation;
import com.example.sharebackend.response.ReservationWithReview;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

@Mapper
public interface ReservationMapper {
    // 예약하기
    int insertOne(Reservation reservation);

    // 해당 아이디 예약 리스트 조회
    List<Reservation> selectListAll(String accountId);

    // 해당 아이디 예약 조회
    Reservation selectById(int reservationIdx);

    // 반납절차 확인용 예약 조회
    Reservation selectReservationForReturn(int reservationIdx);

    // 예약 총 횟수
    int selectAllCount(String accountId);

    // 중복 예약 체크
    int existsReservation(int rentalOfferIdx, LocalDate startDate, LocalDate endDate);

    // 예약 취소
    int reservationDelete(int idx, String accountId);

    // 예약 상태 업데이트
    int reservationStatusUpdate(int reservationIdx, String accountId);

    // 예약 리뷰 확인
    List<ReservationWithReview> selectReservationsWithReview(@Param("accountId") String accountId);

}
