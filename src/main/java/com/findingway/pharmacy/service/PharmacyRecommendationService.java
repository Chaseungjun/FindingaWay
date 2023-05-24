package com.findingway.pharmacy.service;

import com.findingway.api.dto.KakaoDocumentDto;
import com.findingway.api.dto.KakaoResponseDto;
import com.findingway.api.service.KakaoAddressSearchService;
import com.findingway.direction.dto.PharmacyInfoDto;
import com.findingway.direction.entity.Direction;
import com.findingway.direction.service.Base62Service;
import com.findingway.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class PharmacyRecommendationService {

    private final KakaoAddressSearchService addressSearchService;
    private final DirectionService directionService;

    private final Base62Service base62Service;

    private static final String ROAD_VIEW_BASE_URL = "https://map.kakao.com/link/roadview/";
    @Value("${pharmacy.recommendation.base.url}")
    private String baseUrl;


    public List<PharmacyInfoDto> recommendPharmacy(String address) {
        KakaoResponseDto kakaoResponseDto = addressSearchService.requestAddressSearch(address);
         // 카카오 API를 사용해서 입력받은 주소를 위치기반 데이터로 변경

        // retry의 결과로 null이 반환된 경우와 검색 결과로 아무것도 나오지 않은 경우
        if (Objects.isNull(kakaoResponseDto) || CollectionUtils.isEmpty(kakaoResponseDto.getDocumentDtoList())) {
            log.info("PharmacyRecommendationService recommendPharmacy fail input address: {}", address);
            return Collections.emptyList();
        }

        KakaoDocumentDto documentDto = kakaoResponseDto.getDocumentDtoList().get(0);  // 주소 검색 결과들이 위도,경도,주소명의 주소데이터로 변경된 dto를 가져온다
//        List<Direction> pharmacyList = directionService.searchPharmacyWithAddress(documentDto);  // 공공데이터 사용
        List<Direction> pharmacyList = directionService.searchPharmacyByCategoryApi(documentDto);  // 조건을 반영한 약국 3개를 가져온다

        List<Direction> directionList = directionService.saveAll(pharmacyList);
        return directionList.stream()
                .map(direction -> convertToPharmacyInfo(direction))
                .collect(Collectors.toList());
    }

    private PharmacyInfoDto convertToPharmacyInfo(Direction direction) {

        return PharmacyInfoDto.builder()
                .pharmacyAddress(direction.getTargetAddress())
                .pharmacyName(direction.getTargetPharmacyName())
                .directionUrl(baseUrl + base62Service.encodeDirectionId(direction.getId())) // shorten url
                .roadViewUrl(ROAD_VIEW_BASE_URL + direction.getTargetLatitude() + "," + direction.getTargetLongitude())
                .distance(String.format("%.2f km", direction.getDistance()))  // 소수점 2번째 자리까지
                .build();
    }

}
