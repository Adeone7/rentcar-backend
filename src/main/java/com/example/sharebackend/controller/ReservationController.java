package com.example.sharebackend.controller;

import com.example.sharebackend.domain.CarImg;
import com.example.sharebackend.domain.Reservation;
import com.example.sharebackend.mapper.RentalOfferMapper;
import com.example.sharebackend.mapper.ReservationMapper;
import com.example.sharebackend.request.ReservationRequest;
import com.example.sharebackend.request.ReservationStatusUpdate;
import com.example.sharebackend.response.ReservationListResponse;
import com.example.sharebackend.response.ReservationResponse;
import com.example.sharebackend.response.ReservationStatusUpdateResponse;
import com.example.sharebackend.response.ReservationWithReview;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Update;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin
public class ReservationController {
    final ReservationMapper reservationMapper;
    final RentalOfferMapper rentalOfferMapper;


    @PostMapping("/reservation")
    public ReservationResponse ReservationHandle(@RequestBody ReservationRequest rvr,
                                                 @RequestAttribute("currentAccountId") String accountId,
                                                 HttpSession session) {

        Reservation rvt = rvr.toReservation();
        rvt.setAccountId(accountId);
        int price = rentalOfferMapper.selectByRentalPrice(rvr.getRentalOfferIdx());

        if (price == 0) {
            return ReservationResponse.builder().success(false).message("금액오류").build();
        }

        System.out.println("price : " + price);
        // 금액 계산
        int total = calculateTotalPrice(
                rvr.getStartDate(),
                rvr.getEndDate(),
                price
        );

        // 중복 예약 확인
        int ct = reservationMapper.existsReservation(
                rvr.getRentalOfferIdx(),
                rvr.getStartDate(),
                rvr.getEndDate()
        );

        if (ct > 0) {
            return ReservationResponse.builder().success(false).message("이미 예약된 날짜입니다.").build();
        }

        rvt.setPaymentAmount(total);
        rvt.setReservationStatus("예약중");

        reservationMapper.insertOne(rvt);

        return ReservationResponse.builder().success(true).reservation(rvt)
                .message("예약이 완료 되었습니다.").build();
    }

    // 시작날짜, 끝날짜, 하루 금액을 가져와서 for문 돌리면 if 처리
    public int calculateTotalPrice(
            LocalDate startDate,
            LocalDate endDate,
            int rentalOfferPrice) {
        int total = 0;
        // date를 시작일로 초기화하고 date가 endDate일 동안 반복하고 하루씩 증가
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            DayOfWeek day = date.getDayOfWeek(); // 현재 날짜의 요일가져옴(월 ~ 일 중에 하나)
            boolean isWeekend = (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY); // 토 ,일 이면 ture

            if (isWeekend) {
                total += (int) (rentalOfferPrice * 1.5); // 주말이면 기본 가격에 1.5배
            } else {
                total += rentalOfferPrice;
            }

        }
        return total;
    }
    // 예약 기록 조회
    @GetMapping("/reservation")
    public ReservationListResponse getReservations(@RequestAttribute("currentAccountId") String accountId) {
        List<Reservation> list = reservationMapper.selectListAll(accountId);
        if (!list.isEmpty()) {
            int total = list.size();
            return ReservationListResponse.builder()
                    .success(true)
                    .message("조회성공")
                    .total(total)
                    .reservationsWithReviews(list)
                    .build();
        }
        return ReservationListResponse.builder()
                .success(false)
                .message("조회실패")
                .build();
    }

    // 리류 여부 예약 조회
    @GetMapping("/reservation/review")
    public ReservationListResponse getReservationsReview(@RequestAttribute("currentAccountId") String accountId) {
        List<ReservationWithReview> list = reservationMapper.selectReservationsWithReview(accountId);
        if (!list.isEmpty()) {
            int total = list.size();
            return ReservationListResponse.builder()
                    .success(true)
                    .message("조회성공")
                    .total(total)
                    .reservations(list)
                    .build();
        }
        return ReservationListResponse.builder()
                .success(false)
                .message("조회실패")
                .build();
    }

    @DeleteMapping("/reservation/{idx}")
    public ReservationResponse deleteReservationHandle(@PathVariable int idx, @RequestAttribute("currentAccountId") String accountId) {
        System.out.println("idx : " + idx);
        System.out.println("accountId : " + accountId);

        int update = reservationMapper.reservationDelete(idx, accountId);
        if (update > 0) {
            return ReservationResponse.builder().success(true).message("예약이 취소 되었습니다.").build();
        }

        return ReservationResponse.builder().success(false).message("예약취소 실패").build();
    }

    // 반납하기 (예약상태 업데이트)
    @PatchMapping("/reservation/statusUpdate")
    public ReservationStatusUpdateResponse reservationStatusUpdate(@RequestBody ReservationStatusUpdate rsu,
                                                                   @RequestAttribute("currentAccountId") String accountId) {

        Reservation reservation = reservationMapper.selectReservationForReturn(rsu.getReservationIdx());

        if(reservation.getAccountId() == null) {
            return ReservationStatusUpdateResponse.builder().success(false).message("예약 정보가 없습니다.").build();
        }

        if(!accountId.equals(reservation.getAccountId())) {
            return ReservationStatusUpdateResponse.builder().success(true).message("접근 불가.").build();
        }

        reservationMapper.reservationStatusUpdate(reservation.getIdx(), accountId);

        return ReservationStatusUpdateResponse.builder().success(true).message("이용해 주셔서 감사합니다.").build();
      }

}
