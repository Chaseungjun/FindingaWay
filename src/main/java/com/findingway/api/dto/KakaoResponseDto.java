package com.findingway.api.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class KakaoResponseDto {

    @JsonProperty("meta")
    private KakaoMetaDto metaDto;

    @JsonProperty("documents")
    private List<KakaoDocumentDto> documentDtoList = new ArrayList<>();
}
