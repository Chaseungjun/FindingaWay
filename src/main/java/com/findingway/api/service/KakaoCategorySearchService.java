package com.findingway.api.service;


import com.findingway.api.dto.KakaoResponseDto;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;


@Service
@Slf4j
@RequiredArgsConstructor
@Getter
public class KakaoCategorySearchService {


    private static final String PHARMACY_CATEGORY = "PM9";

    @Value("${kakao.rest.api.key}")
    private String kakaoRestApiKey;

    private final KakaoUriBuilderService uriBuilderService;
    private final RestTemplate restTemplate;  // 카카오 api를 호출하기 위한 restTemplate, 지금은 WebClient 사용이 권장됨


    public KakaoResponseDto requestPharmacyCategorySearch(double latitude, double longitude, double radius) {

        URI uri = uriBuilderService.buildUriByCategorySearch(latitude, longitude, radius, PHARMACY_CATEGORY);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK "+ kakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);

        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponseDto.class).getBody();
    }
}
