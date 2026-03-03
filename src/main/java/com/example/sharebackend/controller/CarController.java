package com.example.sharebackend.controller;

import com.example.sharebackend.domain.Car;
import com.example.sharebackend.domain.RentalOffer;
import com.example.sharebackend.mapper.CarMapper;
import com.example.sharebackend.request.CarAddRequest;
import com.example.sharebackend.response.CarChartResponse;
import com.example.sharebackend.response.CarChartResultResponse;
import com.example.sharebackend.response.CarListResponse;
import com.example.sharebackend.response.CarAddResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class CarController {
    final CarMapper carMapper;

    // 차등록
    @PostMapping("/car")
    public CarAddResponse addCar(@RequestBody CarAddRequest carAddRequest) {
        Car car = new Car();
        car.setCorporation(carAddRequest.getCorporation());
        car.setModelName(carAddRequest.getModelName());
        car.setCarType(carAddRequest.getCarType());
        car.setModelYear(carAddRequest.getModelYear());
        car.setFewSeats(carAddRequest.getFewSeats());
        car.setGearType(carAddRequest.getGearType());
        carMapper.insertCar(car);
        return CarAddResponse.builder().success(true).car(car).build();
    }

    // 차 검색 매물 및 갯수
    @GetMapping("/car")
    public CarListResponse carInfoHandle(@RequestParam(defaultValue = "") String query) {

        List<Car> carList = carMapper.findAllCars(query);
        int allCars = carMapper.countAllCars(query);
        return CarListResponse.builder().success(true).carList(carList).total(allCars).build();
    }

    // 차트용 데이터
    @GetMapping("/car/chart")
    public CarChartResponse chartInfoHandle() {

        // 차트용 전체 데이터
        List<CarChartResultResponse> ChartList = carMapper.ChartList();
        if(ChartList == null){
            return CarChartResponse.builder().success(false).message("정보가 없습니다.").build();
        }

        List<Car> carList = new ArrayList<>();
        for(CarChartResultResponse chart : ChartList){
            Car car = carMapper.findCarByIdx(chart.getIdx());
            if(car != null) {
                carList.add(car);
            }
        }
        return CarChartResponse.builder()
                .success(true)
                .message("차트 데이터 조회 성공")
                .total(carList.size())
                .carChartResponseList(ChartList)
                .build();
    }

    @GetMapping("/car/month/chart")
    public CarChartResponse chartMonthInfoHandle(@RequestParam(defaultValue = "") String month) {

        // 차트용 월별 데이터
        List<CarChartResultResponse> ChartMonthList = carMapper.ChartMonthList(month);
        if(ChartMonthList == null){
            return CarChartResponse.builder().success(false).message("정보가 없습니다.").build();
        }

        List<Car> carList = new ArrayList<>();
        for(CarChartResultResponse chart : ChartMonthList){
            Car car = carMapper.findCarByIdx(chart.getIdx());
            if(car != null) {
                carList.add(car);
            }
        }
        return CarChartResponse.builder()
                .success(true)
                .message("차트 월별 데이터 조회 성공")
                .total(carList.size())
                .carChartResponseList(ChartMonthList)
                .build();
    }

}
