package com.findingway.pharmacy.entity;

import com.findingway.pharmacy.BaseEntity;
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
public class Pharmacy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String pharmacyName;
    private String pharmacyAddress;
    private double latitude;
    private double longitude;

    @Builder
    public Pharmacy(String pharmacyName, String pharmacyAddress, double latitude, double longitude) {
        this.pharmacyName = pharmacyName;
        this.pharmacyAddress = pharmacyAddress;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public void updateAddress(Long id, String address){
        pharmacyAddress = address;
    }
}
