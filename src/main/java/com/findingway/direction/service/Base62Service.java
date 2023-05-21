package com.findingway.direction.service;

import io.seruco.encoding.base62.Base62;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Getter
@RequiredArgsConstructor
@Slf4j
@Service
public class Base62Service {

    private static final Base62 base62Instance = Base62.createInstance();

    public String encodeDirectionId(Long directionId){
        return new String(base62Instance.encode(String.valueOf(directionId).getBytes()));
    }

    public Long decodeDirectionId(String encodedDirectionId){
        String directionId = new String(base62Instance.decode(encodedDirectionId.getBytes()));
        return Long.valueOf(directionId);
    }
}
