package com.findingway.direction.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class PharmacyInfoDto {


    private String pharmacyName;    // 약국 명
    private String pharmacyAddress; // 약국 주소
    private String directionUrl;    // 길안내 url
    private String roadViewUrl;     // 로드뷰 url
    private String distance;        // 고객 주소와 약국 주소의 거리

    @Builder
    private PharmacyInfoDto(String pharmacyName, String pharmacyAddress, String directionUrl, String roadViewUrl, String distance) {
        this.pharmacyName = pharmacyName;
        this.pharmacyAddress = pharmacyAddress;
        this.directionUrl = directionUrl;
        this.roadViewUrl = roadViewUrl;
        this.distance = distance;
    }
}
