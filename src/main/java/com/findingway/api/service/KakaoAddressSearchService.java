package com.findingway.api.service;

import com.findingway.api.dto.KakaoResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Recover;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoAddressSearchService {

    private final RestTemplate restTemplate;
    private final KakaoUriBuilderService kakaoUriBuilderService;

    @Value("${KAKAO_REST_API_KEY}")
    private String KakaoRestApiKey;

    @Retryable(
            value = {RuntimeException.class},
            maxAttempts = 2,
            backoff = @Backoff(delay = 2000)  // 시도를 실패했을 때 다음 시도까지의 딜레이
    )
    public KakaoResponseDto requestAddressSearch(String address) {

        if (ObjectUtils.isEmpty(address)) {
            return null;
        }

        URI uri = kakaoUriBuilderService.buildUriByAddressSearch(address);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, "KakaoAK " + KakaoRestApiKey);
        HttpEntity httpEntity = new HttpEntity<>(headers);


        /**
         * 입력받은 주소를 카카오 주소 api를 사용해서 위치기반 데이터인 KakaoResponseDto로 반환 (정확히는 KakaoDocumentDto)
         * restTemplate는  HTTP 헤더 설정, 요청 메서드 선택, 요청 본문 데이터 전송을 하면서 카카오 API를 사용하기 위해 사용한다.
         *  uri가 restTemplate에 의해 인코딩 되어 String 타입이 된다.
         */
        return restTemplate.exchange(uri, HttpMethod.GET, httpEntity, KakaoResponseDto.class).getBody();
    }

    @Recover  // retry에서 정한 횟수가 모두 실패했을 때 실행되는 메서드로 recover의 반환타입은 retry를 적용하는 메서드의 반환타입과 같아야한다
    public KakaoResponseDto recover(RuntimeException e, String address) {
        log.info("All retries failed. address = {} , error = {}", address, e.getMessage());
        return null;
    }
}
