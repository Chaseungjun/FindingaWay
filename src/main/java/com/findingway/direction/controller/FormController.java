package com.findingway.direction.controller;

import com.findingway.direction.dto.PharmacyAddressDto;
import com.findingway.pharmacy.service.PharmacyRecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequiredArgsConstructor
public class FormController {

    private final PharmacyRecommendationService recommendationService;

    @GetMapping("/")
    public String main(){
        return "main";
    }


    @PostMapping("/search")
    public ModelAndView PostDirection(@ModelAttribute PharmacyAddressDto pharmacyAddressDto){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("output");
        modelAndView.addObject("outputFormList",
                recommendationService.recommendPharmacy(pharmacyAddressDto.getAddress()));

        return modelAndView;
    }
}
