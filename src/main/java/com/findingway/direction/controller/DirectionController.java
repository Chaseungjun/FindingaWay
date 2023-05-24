package com.findingway.direction.controller;


import com.findingway.direction.service.DirectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
@RequiredArgsConstructor
public class DirectionController {

    private final DirectionService directionService;


    @GetMapping("/dir/{encodedId}")
    public String searchDirection(@PathVariable String encodedId){
        String directionUrl = directionService.findDirectionUrlById(encodedId);
        // 사용자가 입력한 주소를 기준으로 추천된 약국을 DB에서 가져오고 길안내 url 생성

        log.info("direction url : {}", directionUrl);
        return "redirect:" + directionUrl;
    }
}
