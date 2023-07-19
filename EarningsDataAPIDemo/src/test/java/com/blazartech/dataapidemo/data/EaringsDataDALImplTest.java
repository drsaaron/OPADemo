/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.blazartech.dataapidemo.data;

import com.blazartech.dataapidemo.data.EarningsDataDALImpl;
import com.blazartech.dataapidemo.data.EarningsFailure;
import com.blazartech.dataapidemo.security.EntitledRelationship;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import static org.junit.Assert.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

/**
 *
 * @author aar1069
 */
@ExtendWith(SpringExtension.class)
@Slf4j
public class EaringsDataDALImplTest {
    
    private static Date parseDate(String dateString) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(dateString);
        } catch(ParseException e) {
            throw new RuntimeException("error parsing date: " + e.getMessage(), e);
        }
    }

    @TestConfiguration
    public static class EarningsDataDALImplTestConfiguration {
        
        @Bean
        public EarningsDataDALImpl instance() {
            return new EarningsDataDALImpl();
        }
        
        @Bean
        public List<EntitledRelationship> entitledRelationships() {
            List<EntitledRelationship> relationships = List.of(
                    new EntitledRelationship(1000, parseDate("2023-04-01"), null, "", ""),
                    new EntitledRelationship(1001, parseDate("2023-04-01"), null, "", ""),
                    new EntitledRelationship(1002, parseDate("2023-04-01"), null, "", ""),
                    new EntitledRelationship(1003, parseDate("2023-04-01"), null, "", ""),
                    new EntitledRelationship(1004, parseDate("2023-04-01"), null, "", "")
            );
            
            return relationships;
        }
    }
    
    @Autowired
    private EarningsDataDALImpl instance;
    
    @Autowired
    private List<EntitledRelationship> entitledRelationships;
    
    public EaringsDataDALImplTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of getFailures method, of class EarningsDataDALImpl.
     */
    @Test
    public void testGetFailures() {
        log.info("getFailures");

        Collection<EarningsFailure> result = instance.getFailures(entitledRelationships);
        
        assertNotNull(result);
        
        /* there are 6 failures in the "DB", but one is for 2022, and all the
           entitled relationships start in 2023 thus the DAL should filter that
           2022 failure out.
        */
        assertEquals(5, result.size());
    }
    
    @Test
    public void testGetFailure() {
        log.info("getFailure");
        
        Optional<EarningsFailure> result = instance.getFailure(1001, entitledRelationships);
        assertTrue(result.isPresent());
        assertEquals(1001, result.get().getLegalEntityID());
    }
}
