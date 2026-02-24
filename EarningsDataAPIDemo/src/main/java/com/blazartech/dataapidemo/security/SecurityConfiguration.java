/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.security.autoconfigure.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;

/**
 *
 * @author aar1069
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration {

    @Autowired
    private AuthorizationManager<RequestAuthorizationContext> opaManager;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(authz -> authz
                .requestMatchers(request -> request.getRequestURI().startsWith("/failures")).access(opaManager)
                .requestMatchers(request -> request.getRequestURI().startsWith("/monitoring") || request.getRequestURI().startsWith("/v3/api-docs")).permitAll()
                .requestMatchers(request -> request.getRequestURI().startsWith("/v3/api-docs")).permitAll()
                .requestMatchers("/swagger-ui.html", "/swagger-ui/**").permitAll()
        );
        return http.build();

    }

    @Bean
    public SecurityProperties securityProperties() {
        return new SecurityProperties();
    }
}
