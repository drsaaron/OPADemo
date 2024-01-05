/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.security;

import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author aar1069
 */
@Data
@AllArgsConstructor
public class EntitledRelationship {
    
    public EntitledRelationship() {
    }

    private int legalEntityID;
    private LocalDate startDate;
    private LocalDate endDate;
    private String entitleableFunction;
    private String manager;
}
