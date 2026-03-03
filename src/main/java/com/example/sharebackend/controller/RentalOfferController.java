package com.example.sharebackend.controller;

import com.example.sharebackend.domain.CarImg;
import com.example.sharebackend.domain.RentalOffer;
import com.example.sharebackend.mapper.ReviewMapper;
import com.example.sharebackend.request.RentalOfferDayRequest;
import com.example.sharebackend.response.RentalOfferAddReviewResponse;
import com.example.sharebackend.mapper.RentalOfferMapper;
import com.example.sharebackend.request.RentalOfferAddRequest;
import com.example.sharebackend.response.RentalOfferAddResponse;
import com.example.sharebackend.response.RentalOfferListResponse;
import com.example.sharebackend.response.RentalOfferResponse;
import com.example.sharebackend.response.*;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@Transactional
public class RentalOfferController {
    final RentalOfferMapper rentalOfferMapper;
    final ReviewMapper reviewMapper;

    @PostMapping("/rental-offer")
    public RentalOfferAddResponse addRentalOffer(@ModelAttribute RentalOfferAddRequest rentalOfferAddRequest,
                                                 @RequestAttribute String currentAccountId) throws IOException {

        RentalOffer rentalOffer = new RentalOffer();
        rentalOffer.setAccountId(currentAccountId);
        rentalOffer.setCarIdx(rentalOfferAddRequest.getCarIdx());
        rentalOffer.setRentalPrice(rentalOfferAddRequest.getRentalPrice());
        rentalOffer.setDescription(rentalOfferAddRequest.getDescription());

        int rentalOfferInsertResult = rentalOfferMapper.insertRentalOffer(rentalOffer);

        if (rentalOfferInsertResult != 1) {
            return RentalOfferAddResponse.builder().success(false).message("렌탈 오퍼 DB 저장에 실패했습니다.").build();
        }

        int generatedRentalOfferIdx = rentalOffer.getIdx();

        List<MultipartFile> images = rentalOfferAddRequest.getImg();
        if (images == null || images.isEmpty()) {
            return RentalOfferAddResponse.builder().success(false).message("하나 이상의 이미지를 업로드해야 합니다.").build();
        }
        if (images.size() > 5) {
            return RentalOfferAddResponse.builder().success(false).message("최대 5장의 이미지만 업로드할 수 있습니다.").build();
        }

        // 고정된 베이스 경로 설정
        Path baseUploadPath = Path.of(System.getProperty("user.home"), "share", "car-images");
        Files.createDirectories(baseUploadPath);

        String folderUuid = UUID.randomUUID().toString();
        Path specificUploadPath = baseUploadPath.resolve(folderUuid);
        Files.createDirectories(specificUploadPath);

        for (MultipartFile file : images) {
            if (!file.isEmpty()) {
                if (file.getContentType() == null || !file.getContentType().startsWith("image")) {
                    return RentalOfferAddResponse.builder().success(false).message("이미지 파일만 업로드 가능합니다.").build();
                }

                String originalFilename = file.getOriginalFilename();
                String fileExtension = "";
                if (originalFilename != null && originalFilename.contains(".")) {
                    fileExtension = originalFilename.substring(originalFilename.lastIndexOf("."));
                }
                String uniqueFileName = UUID.randomUUID().toString() + fileExtension;
                Path filePath = specificUploadPath.resolve(uniqueFileName);
                Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

                CarImg carImg = new CarImg();
                carImg.setRentalOfferIdx(generatedRentalOfferIdx);
                carImg.setImg("/car-images/" + folderUuid + "/" + uniqueFileName);

                int carImgInsertResult = rentalOfferMapper.insertCarImg(carImg);

                if (carImgInsertResult != 1) {
                    return RentalOfferAddResponse.builder().success(false).message("차량 이미지 DB 저장에 실패했습니다.").build();
                }
            }
        }

        List<CarImg> carImages = rentalOfferMapper.findCarImgs(generatedRentalOfferIdx);

        // --- 성공 시 ---
        return RentalOfferAddResponse.builder()
                .success(true)
                .message("렌탈 매물이 성공적으로 등록되었습니다.")
                .registeredRentalOfferIdx(generatedRentalOfferIdx)
                .rentalOffer(rentalOffer)
                .carImg(carImages)
                .build();
    }

    @GetMapping("/rental-offer")
    public RentalOfferListResponse rentalOfferInfoHandle() {
        // 매물 전체 조회
        List<RentalOfferAddReviewResponse> rentalOfferAllList = rentalOfferMapper.findAllRentalOffersWithImages();


        // 매물 idx 목록 추출
        List<Integer> ids = rentalOfferAllList.stream()
                .map(RentalOfferAddReviewResponse :: getIdx)
                .toList();

        // 해당 idx들의 이미지 조회
        List<CarImg> imgList =
                ids.isEmpty() ? List.of() : rentalOfferMapper.rentalOfferAllImages(ids);


        // 매물 + 이미지 묶기
        List<RentalOfferAllImages> result = new ArrayList<>();
        for(RentalOfferAddReviewResponse o : rentalOfferAllList) {
            List<String> image = imgList.stream()
                    .filter(img -> img.getRentalOfferIdx() == o.getIdx())
                    .map(CarImg::getImg).toList();

            RentalOfferAllImages dto =
                    RentalOfferAllImages.builder()
                            .idx(o.getIdx())
                            .nickname(o.getNickname())
                            .rentalPrice(o.getRentalPrice())
                            .description(o.getDescription())
                            .corporation(o.getCorporation())
                            .modelName(o.getModelName())
                            .carType(o.getCarType())
                            .modelYear(o.getModelYear())
                            .fewSeats(o.getFewSeats())
                            .gearType(o.getGearType())
                            .images(image)
                            .build();
            result.add(dto);
        }

        int count = rentalOfferMapper.countAllRentalOffer();
        return RentalOfferListResponse.builder().success(true).countAllRentalOffer(count)
                .rentalOfferListResponse(result).build();
    }


    // 특정 매물 조회
    @GetMapping("/rental-offer/{rentalOfferIdx}")
    public RentalOfferResponse rentalOfferReviewHandle(@PathVariable int rentalOfferIdx) {
        RentalOfferAddReviewResponse rentalOfferAddReviewList = rentalOfferMapper.selectRentalOfferAndReview(rentalOfferIdx);
        List<CarImg> cImg = rentalOfferMapper.findCarImgs(rentalOfferIdx);
        List<RentalOfferReviewListResponse> reviewList = reviewMapper.selectByRentalOfferIdx(rentalOfferIdx);
        if(rentalOfferAddReviewList == null){
            return RentalOfferResponse.builder().success(false).message("존재하지 않는 매물입니다.").build();
        }


    return RentalOfferResponse.builder().success(true).rentalOfferCarImg(cImg)
            .rentalOfferAddReview(rentalOfferAddReviewList)
            .rentalOfferReviewListResponses(reviewList).build();
    }

    // 날짜에 해당하는 매물 조회
    @GetMapping("/rental-offer/day")
    public RentalOfferResponse rentalOfferDayListHandle(@RequestParam("startDate") LocalDate startDate,
                                                        @RequestParam("endDate") LocalDate endDate) {
        List<RentalOfferDayListResponse> dayList =
                rentalOfferMapper.findAvailableRentalOffers(startDate, endDate);
        if(dayList.isEmpty()){
            return RentalOfferResponse.builder().success(true).message("매물 없음").build();
        }
        if (startDate.isAfter(endDate)) {
            return RentalOfferResponse.builder()
                    .success(false)
                    .message("시작일은 종료일보다 이후일 수 없습니다.")
                    .build();
        }
        return RentalOfferResponse.builder()
                .success(true).message("조회성공").total(dayList.size()).rentalOfferDayList(dayList).build();
    }

}
