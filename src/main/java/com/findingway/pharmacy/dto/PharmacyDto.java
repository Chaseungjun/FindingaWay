package com.findingway.pharmacy.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PharmacyDto { // 약국에 대한 정보

    private Long id;
    private String pharmacyName;
    private String pharmacyAddress;
    private double latitude;
    private double longitude;
}
