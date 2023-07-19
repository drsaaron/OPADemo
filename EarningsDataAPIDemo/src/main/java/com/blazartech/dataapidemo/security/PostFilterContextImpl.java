/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.security;

import java.util.List;

/**
 *
 * @author aar1069
 */
@Deprecated
public class PostFilterContextImpl implements PostFilterContext {
    
    // as this is a singleton object, we need to use a threadlocal wrapper so
    // that each thread has its own list
    private final ThreadLocal<List<EntitledRelationship>> tl = new ThreadLocal<>();

    public void setEntitledRelationships(List<EntitledRelationship> filteredLegalEntityIDs) {
        tl.set(filteredLegalEntityIDs);
    }

    @Override
    public List<EntitledRelationship> getEntitledRelationships() {
        return tl.get();
    }
}
