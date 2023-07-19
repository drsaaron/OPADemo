/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.blazartech.dataapidemo.controller;

import com.blazartech.dataapidemo.data.EarningsFailure;
import com.blazartech.dataapidemo.security.EntitledRelationship;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import com.blazartech.dataapidemo.data.EarningsDataDAL;

/**
 *
 * @author aar1069
 */
@RestController
@Slf4j
@OpenAPIDefinition(
        info = @Info(
                title = "demo earnings API with entitlements",
                version = "1.0"
        )
)
public class EarningsDataAPI {

    @Autowired
    private EarningsDataDAL dal;
    
    private List<EntitledRelationship> getEntitledRelationships(HttpServletRequest request) {
        return (List<EntitledRelationship>) request.getSession().getAttribute("filteredList");
    }

    @GetMapping(path = "/failures")
    @Operation(summary = "get list of all failures to which the caller is entitled")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "got the failure list",
                content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EarningsFailure.class))
                    )
                }),
        @ApiResponse(responseCode = "403", description = "caller is not authorized to the requested data",
                content = {
                    @Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = EarningsFailure.class))
                    )
                })
    })
    public Collection<EarningsFailure> getFailures(HttpServletRequest request) {
        log.info("getting all failures");
        return dal.getFailures(getEntitledRelationships(request));
    }

    @GetMapping(path = "/failures/{legalEntityID}")
    @Operation(summary = "get a specific failure, not really right but it's a POC")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "got the failure",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EarningsFailure.class))
                }),
        @ApiResponse(responseCode = "403", description = "caller is not authorized to the requested data",
                content = {
                    @Content(mediaType = "application/json",
                            schema = @Schema(implementation = EarningsFailure.class))
                })
    })
    public Optional<EarningsFailure> getFailure(@PathVariable int legalEntityID, HttpServletRequest request) {
        log.info("getting failure for {}", legalEntityID);
        Optional<EarningsFailure> failure = dal.getFailure(legalEntityID, getEntitledRelationships(request));
        return failure;
    }

    @InitBinder
    public void initBinder(WebDataBinder binder) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        df.setLenient(false);
        binder.registerCustomEditor(Date.class, new CustomDateEditor(df, false));
    }

}
