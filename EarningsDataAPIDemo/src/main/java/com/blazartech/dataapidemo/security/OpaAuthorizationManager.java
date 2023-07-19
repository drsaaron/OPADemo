/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.blazartech.dataapidemo.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.node.ObjectNode;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;
import java.util.function.Supplier;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

/**
 *
 * @author aar1069
 */
@Component
@Slf4j
public class OpaAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Value("${opa.url}")
    private String opaUrl;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private OpaHttpClient opaClient;

    @Override
    public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext object) {
        // package up the request method, path and JWT to send to OPA
        HttpServletRequest request = object.getRequest();
        String method = request.getMethod();
        String path = request.getRequestURI();
        // List<String> headers = Collections.list(request.getHeaderNames());

        // TODO:  line below assumes "bearer" is present.  Should test first.
        String authorizationHeader = request.getHeader("Authorization");
        String jwt = null;
        if (authorizationHeader != null) {
            jwt = authorizationHeader.split("\\s+")[1];
        }
        
        int fr = -1;
        if (path.matches("/failures/.*")) {
            log.info("found match for FR call");
            fr = Integer.parseInt(path.replace("/failures/", ""));
        } 

        ObjectNode requestNode = objectMapper.createObjectNode();
        requestNode.set(
                "input",
                objectMapper.valueToTree(
                        OpaRequestBuilder.builder()
                                .method(method)
                                .path(path)
                                .encodedJwt(jwt)
                                .fr(fr)
                                .build()));

        try {
            JSONObject jsonRequest = new JSONObject(objectMapper.writeValueAsString(requestNode));
            log.info("OPA request: \n" + jsonRequest);

            // send request to OPA
            JsonNode response = opaClient.sendOpaRequest(opaUrl, jsonRequest.toString());
            log.info("OPA response: \n" + response);

            // If access to resource API is allowed
            if (response.findValue("allow").asBoolean()) {

                JsonNode filterListJson = response.findValue("filter_list");
                ObjectReader reader = objectMapper.readerFor(new TypeReference<List<EntitledRelationship>>() {
                });

                try {
                    List<EntitledRelationship> filterList = reader.readValue(filterListJson);
                    
                    // save the list of entitled relationships to the request session for use in DAL
                    object.getRequest().getSession().setAttribute("filteredList", filterList);
                } catch (IOException e) {
                    log.error("Error building list for filtering: " + e.getMessage(), e);
                    return new AuthorizationDecision(false);
                }
                return new AuthorizationDecision(true);
            }

            // If access to resource API is not allowed
            return new AuthorizationDecision(false);

        } catch (JsonProcessingException | JSONException e) {
            log.error("Error building OPA request \n");
            return new AuthorizationDecision(false);

        }

    }

}
