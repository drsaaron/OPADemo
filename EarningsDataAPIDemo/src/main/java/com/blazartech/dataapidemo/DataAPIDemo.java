/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.blazartech.dataapidemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.security.autoconfigure.SecurityAutoConfiguration;

/**
 *
 * @author aar1069
 */
@SpringBootApplication(exclude = {SecurityAutoConfiguration.class })
public class DataAPIDemo {

    public static void main(String[] args) {
        SpringApplication.run(DataAPIDemo.class, args);
    }
}
