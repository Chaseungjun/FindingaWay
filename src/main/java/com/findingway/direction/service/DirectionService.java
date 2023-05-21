package com.findingway.direction.service;

import com.findingway.api.dto.KakaoDocumentDto;
import com.findingway.api.service.KakaoCategorySearchService;
import com.findingway.direction.entity.Direction;
import com.findingway.direction.repository.DirectionRepository;
import com.findingway.pharmacy.service.PharmacySearchService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityNotFoundException;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Getter
@RequiredArgsConstructor
public class DirectionService {

    private static final int MAX_SEARCH_COUNT = 3;// 최대 약국 검색 개수
    private static final double RADIUS_KM = 10.0;// 반경 10km 이내 약국
    private static final String DIRECTION_VIEW_BASE_URL = "https://map.kakao.com/link/map/";


    private final PharmacySearchService pharmacySearchService;
    private final DirectionRepository directionRepository;

    private final KakaoCategorySearchService categorySearchService;
    private final Base62Service base62Service;



    public List<Direction> searchPharmacyByPublic(KakaoDocumentDto documentDto){  // 공공기관 데이터를 활용

        if (Objects.isNull(documentDto)) return Collections.emptyList();
        // 약국 데이터 조회
       return pharmacySearchService.searchPharmacyDtoList().stream() // 모든 약국 데이터를 조회해서 Dto로 반환
                .map(pharmacyDto ->
                        Direction.builder()
                                .inputAddress(documentDto.getAddressName())
                                .inputLatitude(documentDto.getLatitude())
                                .inputLongitude(documentDto.getLongitude())
                                .targetAddress(pharmacyDto.getPharmacyAddress())
                                .targetLatitude(pharmacyDto.getLatitude())
                                .targetLongitude(pharmacyDto.getLongitude())
                                .distance(
                                        calculateDistance(documentDto.getLatitude(), documentDto.getLatitude(),
                                                pharmacyDto.getLatitude(), pharmacyDto.getLongitude()))
                                .build())
                .filter(direction -> direction.getDistance() >= RADIUS_KM)
                .sorted(Comparator.comparing(Direction::getDistance))  // 거리를 가져와서 오름차순 정렬
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());

    }

    // pharmacy search by category kakao api
    public List<Direction> searchPharmacyByCategoryApi(KakaoDocumentDto inputDocumentDto) {
        if(Objects.isNull(inputDocumentDto)) return Collections.emptyList();

        return categorySearchService
                .requestPharmacyCategorySearch(inputDocumentDto.getLatitude(), inputDocumentDto.getLongitude(), RADIUS_KM)
                .getDocumentDtoList()
                .stream().map(resultDocumentDto ->
                        Direction.builder()
                                .inputAddress(inputDocumentDto.getAddressName())
                                .inputLatitude(inputDocumentDto.getLatitude())
                                .inputLongitude(inputDocumentDto.getLongitude())
                                .targetPharmacyName(resultDocumentDto.getPlaceName())
                                .targetAddress(resultDocumentDto.getAddressName())
                                .targetLatitude(resultDocumentDto.getLatitude())
                                .targetLongitude(resultDocumentDto.getLongitude())
                                .distance(resultDocumentDto.getDistance() * 0.001) // km 단위
                                .build())
                .limit(MAX_SEARCH_COUNT)
                .collect(Collectors.toList());
    }
    @Transactional
    public List<Direction> saveAll(List<Direction> directionList){
        if (CollectionUtils.isEmpty(directionList))
            return Collections.emptyList();
       return directionRepository.saveAll(directionList);
    }

    public String findDirectionUrlById(String encodeId){
        Long directionId = base62Service.decodeDirectionId(encodeId);
        Direction direction = directionRepository.findById(directionId).orElseThrow(EntityNotFoundException::new);

        String params = String.join(",", direction.getTargetPharmacyName(), String.valueOf(direction.getTargetLatitude()),
                String.valueOf(direction.getTargetLongitude()));

        //Uri를 동적으로 생성해주는 클래스, .toUriString()는 한글을 UTF-8로 자동으로 인코딩 해준다.
        String directionUrl = UriComponentsBuilder.fromHttpUrl(DIRECTION_VIEW_BASE_URL + params).toUriString();

        return directionUrl;
    }


    // Haversine formula
    private double calculateDistance(double lat1, double lon1, double lat2, double lon2) {

        // 1은 자신의 위도와 경도 2는 목적지의 위도와 경도
        lat1 = Math.toRadians(lat1);
        lon1 = Math.toRadians(lon1);
        lat2 = Math.toRadians(lat2);
        lon2 = Math.toRadians(lon2);

        double earthRadius = 6371; //Kilometers
        return earthRadius * Math.acos(Math.sin(lat1) * Math.sin(lat2) + Math.cos(lat1) * Math.cos(lat2) * Math.cos(lon1 - lon2));

    }
}
