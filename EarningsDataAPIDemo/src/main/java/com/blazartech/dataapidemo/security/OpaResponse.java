/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.security;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

/**
 *
 * @author scott
 */
@Data
public class OpaResponse {
    
    @JsonProperty("allow")
    private boolean allowed;
    
    @JsonProperty("filter_list")
    private List<EntitledRelationship> filterList;
}
