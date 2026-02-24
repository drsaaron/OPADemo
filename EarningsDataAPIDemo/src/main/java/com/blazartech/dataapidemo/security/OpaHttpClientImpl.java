package com.blazartech.dataapidemo.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClient.ResponseSpec;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author aar1069
 */
@Component
@Slf4j
public class OpaHttpClientImpl implements OpaHttpClient {
    
    @Autowired
    private RestClient opaRestClient;

    @Override
    public OpaResponse sendOpaRequest(String url, String body) {
        
        ResponseSpec spec = opaRestClient.post()
                .uri(url)
                .body(body)
                .retrieve();
        
        log.info("response {}", spec.body(String.class));
        OpaResponseWrapper response = spec.toEntity(OpaResponseWrapper.class).getBody();
        
        return response.getResult();
        
    }

}
