/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.security;

/**
 *
 * @author aar1069
 */
public record OpaRequestData(
        String method,
        String path,
        String encodedJwt,
        int fr) {

}
