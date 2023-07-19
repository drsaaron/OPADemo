/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.security;

import lombok.Builder;
import lombok.Data;

/**
 *
 * @author aar1069
 */
@Data
@Builder
public class OpaRequestBuilder {

    private String method;
    private String path;
    private String encodedJwt;
    private int fr;
}
