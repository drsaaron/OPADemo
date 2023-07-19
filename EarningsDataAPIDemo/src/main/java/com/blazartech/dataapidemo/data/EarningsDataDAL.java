/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.blazartech.dataapidemo.data;

import com.blazartech.dataapidemo.security.EntitledRelationship;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author aar1069
 */
public interface EarningsDataDAL {
    
    public Collection<EarningsFailure> getFailures(List<EntitledRelationship> entitledRelationships);
   
    public Optional<EarningsFailure> getFailure(int legalEntityID, List<EntitledRelationship> entitledRelationships);
}
