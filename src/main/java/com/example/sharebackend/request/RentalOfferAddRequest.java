package com.example.sharebackend.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import java.time.Year;
import java.util.List;

@Setter
@Getter
public class RentalOfferAddRequest {
    @NotNull(message = "자동차를 선택해주세요.")
    int carIdx;

    @NotNull(message = "렌탈 가격을 입력해주세요.")
    @Min(value = 0, message = "렌탈 가격은 0 이상이어야 합니다.")
    int rentalPrice;
    @NotBlank(message = "설명을 입력해주세요.")
    String description;

    List<MultipartFile> img;
}
