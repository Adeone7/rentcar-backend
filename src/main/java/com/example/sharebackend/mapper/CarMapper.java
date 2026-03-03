package com.example.sharebackend.mapper;

import com.example.sharebackend.domain.Car;
import com.example.sharebackend.domain.RentalOffer;
import com.example.sharebackend.response.CarChartResponse;
import com.example.sharebackend.response.CarChartResultResponse;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CarMapper {
    // 차등록
    int insertCar (Car car);

    // idx 해당 차 조회
    Car findCarByIdx(int idx);

    // query에 해당하는 모든 차와 갯수
    List<Car> findAllCars(@Param("query") String query);
    int countAllCars(@Param("query") String query);

    // 차트용 데이터
    List<CarChartResultResponse> ChartList();

    List<CarChartResultResponse> ChartMonthList(@Param("month") String month);
}
