/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.data;

import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author aar1069
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class EarningsFailure {
    
    private int legalEntityID;
    private String officeNumber;
    private int failureCount;
    private Date effectiveDate;
}
