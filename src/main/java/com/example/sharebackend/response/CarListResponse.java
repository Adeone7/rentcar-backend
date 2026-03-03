package com.example.sharebackend.response;

import com.example.sharebackend.domain.Car;
import com.example.sharebackend.domain.RentalOffer;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Setter
@Getter
@Builder
@JsonPropertyOrder({"success", "total", "carList"})
public class CarListResponse {
    boolean success;
    List<Car> carList;
    List<RentalOffer> rentalOfferList;
    int total;
}
