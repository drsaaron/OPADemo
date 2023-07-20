/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.data;

import com.blazartech.dataapidemo.security.EntitledRelationship;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 *
 * @author aar1069
 */
@Service
@Slf4j
public class EarningsDataDALImpl implements EarningsDataDAL {
    
    private static Date parseDate(String dateString) {
        try {
            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            return df.parse(dateString);
        } catch(ParseException e) {
            throw new RuntimeException("error parsing date: " + e.getMessage(), e);
        }
    }
    
    private static final List<EarningsFailure> DATA = Arrays.asList(
            new EarningsFailure(1000, "001", 2, parseDate("2023-04-01")),
            new EarningsFailure(1001, "001", 1, parseDate("2023-04-01")),
            new EarningsFailure(1002, "002", 5, parseDate("2023-04-01")),
            new EarningsFailure(1003, "003", 2, parseDate("2023-04-01")),
            new EarningsFailure(1004, "003", 1, parseDate("2023-04-01")),
            new EarningsFailure(1001, "003", 1, parseDate("2022-04-01")),
            new EarningsFailure(1003, "001", 1, parseDate("2022-04-01"))
    );
    
    public Predicate<EarningsFailure> getEntitledRelationshipFilter(List<EntitledRelationship> entitledRelationships) {
        return f -> {
            // legal entity ID must match
            Predicate<EntitledRelationship> legalEntityMatch = r -> f.getLegalEntityID() == r.getLegalEntityID();
            
            // start date before on on the effective date
            Predicate<EntitledRelationship> startDateCheck = r -> r.getStartDate().compareTo(f.getEffectiveDate()) <= 0;
            
            // end date is null or on/after the effective date
            Predicate<EntitledRelationship> endDateNullCheck = r -> r.getEndDate() == null;
            Predicate<EntitledRelationship> endDateAfterCheck = r -> r.getEndDate().compareTo(f.getEffectiveDate()) >= 0;
            Predicate<EntitledRelationship> endDateCheck = endDateNullCheck.or(endDateAfterCheck);
            
            // total date check combines the checks on start and end date.
            Predicate<EntitledRelationship> dateCheck = startDateCheck.and(endDateCheck);
            
            // pull it all together.
            return entitledRelationships.stream().anyMatch(legalEntityMatch.and(dateCheck));
        };
    }
    
    @Override
    public Collection<EarningsFailure> getFailures(List<EntitledRelationship> entitledRelationships) {
        log.info("getting all failures");
        log.info("filtered IDs = " + entitledRelationships);
        
        Predicate<EarningsFailure> filterCheck = getEntitledRelationshipFilter(entitledRelationships);
        
        return DATA.stream()
                .filter(filterCheck)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<EarningsFailure> getFailure(int legalEntityID, List<EntitledRelationship> entitledRelationships) {
        log.info("getting failure for LE {}", legalEntityID);
        
        Optional<EarningsFailure> failure = getFailures(entitledRelationships).stream()
                .filter(f -> f.getLegalEntityID() == legalEntityID)
                .findFirst();
        return failure;
    } 
    
}
