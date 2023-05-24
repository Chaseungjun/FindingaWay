package com.findingway.pharmacy.service;

import com.findingway.pharmacy.cache.PharmacyRedisService;
import com.findingway.pharmacy.dto.PharmacyDto;
import com.findingway.pharmacy.entity.Pharmacy;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Getter
@Slf4j
public class PharmacySearchService {


    private final PharmacyRepositoryService pharmacyRepositoryService;
    private final PharmacyRedisService redisService;


    public List<PharmacyDto> searchPharmacyDtoList(){

        // redis에서 먼저 가져오고 없으면 DB에서 가져온다
        List<PharmacyDto> pharmacyDtoList = redisService.findAll();
        if (!pharmacyDtoList.isEmpty()){
            log.info("redis find All success");
            return pharmacyDtoList;
        }

        // DB
       return pharmacyRepositoryService.findAll().stream()
                .map(pharmacy -> from(pharmacy))
                .collect(Collectors.toList());

    }


    public PharmacyDto from(Pharmacy pharmacy){
        return PharmacyDto.builder()
                .id(pharmacy.getId())
                .pharmacyAddress(pharmacy.getPharmacyAddress())
                .pharmacyName(pharmacy.getPharmacyName())
                .latitude(pharmacy.getLatitude())
                .longitude(pharmacy.getLongitude())
                .build();
    }
}
