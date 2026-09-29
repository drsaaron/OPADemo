/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.data;

import java.time.LocalDate;

/**
 *
 * @author aar1069
 */
public record EarningsFailure(
        int legalEntityID,
        String officeNumber,
        int failureCount,
        LocalDate effectiveDate
        ) {

}
