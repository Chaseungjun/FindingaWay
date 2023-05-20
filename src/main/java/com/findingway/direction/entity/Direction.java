package com.findingway.direction.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@NoArgsConstructor
public class Direction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String inputAddress;
    private double inputLatitude;    // 카카오  api를 통해 고객주소의 위도 경도를 구한다
    private double inputLongitude;

    private String targetPharmacyName;
    private String targetAddress;
    private double targetLatitude;
    private double targetLongitude;

    // 고객주소와 목적지 간의 거리
    private double distance;
    @Builder
    private Direction(String inputAddress, double inputLatitude, double inputLongitude, String targetPharmacyName, String targetAddress, double targetLatitude, double targetLongitude, double distance) {
        this.inputAddress = inputAddress;
        this.inputLatitude = inputLatitude;
        this.inputLongitude = inputLongitude;
        this.targetPharmacyName = targetPharmacyName;
        this.targetAddress = targetAddress;
        this.targetLatitude = targetLatitude;
        this.targetLongitude = targetLongitude;
        this.distance = distance;
    }
}
