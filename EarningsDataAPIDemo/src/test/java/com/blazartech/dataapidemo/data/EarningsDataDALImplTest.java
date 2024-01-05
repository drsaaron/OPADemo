/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit4TestClass.java to edit this template
 */
package com.blazartech.dataapidemo.data;

import com.blazartech.dataapidemo.security.EntitledRelationship;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
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
public class EarningsDataDALImplTest {
    
    private static LocalDate parseDate(String dateString) {
        return LocalDate.parse(dateString);
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
                    new EntitledRelationship(1004, parseDate("2023-04-01"), null, "", ""),
                    new EntitledRelationship(1005, parseDate("2022-01-01"), parseDate("2024-12-31"), "", "")
            );
            
            return relationships;
        }
    }
    
    @Autowired
    private EarningsDataDALImpl instance;
    
    @Autowired
    private List<EntitledRelationship> entitledRelationships;
    
    public EarningsDataDALImplTest() {
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
    
    @Test
    public void testGetEntitledRelationshipFilter() {
        log.info("getEntitledRelationshipFilter");
        
        Predicate<EarningsFailure> filter = instance.getEntitledRelationshipFilter(entitledRelationships);
        
        EarningsFailure failure1 = new EarningsFailure(1000, "1", 0, parseDate("2021-01-01"));
        boolean result1 = filter.test(failure1);
        assertFalse(result1);
        
        EarningsFailure failure2 = new EarningsFailure(1000, "1", 0, parseDate("2025-01-01"));
        boolean result2 = filter.test(failure2);
        assertTrue(result2);
        
        EarningsFailure failure3 = new EarningsFailure(1005, "1", 0, parseDate("2025-01-01"));
        boolean result3 = filter.test(failure3);
        assertFalse(result3);
        
        EarningsFailure failure4 = new EarningsFailure(1005, "1", 0, parseDate("2024-12-31"));
        boolean result4 = filter.test(failure4);
        assertTrue(result4);
    }
}
