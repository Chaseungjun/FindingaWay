package com.findingway.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class KakaoDocumentDto {  // 고객의 검색결과로 나온 주소정보

    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("x")
    private double longitude;

    @JsonProperty("y")
    private double latitude;

    @JsonProperty("place_name")
    private String placeName;

    @JsonProperty("distance")
    private double distance;

    @Builder
    public KakaoDocumentDto(String addressName, double longitude, double latitude) {
        this.addressName = addressName;
        this.longitude = longitude;
        this.latitude = latitude;
    }
}
