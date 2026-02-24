/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package com.blazartech.dataapidemo.security;

import jakarta.servlet.http.HttpServletRequest;
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
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;
import tools.jackson.databind.node.ObjectNode;

/**
 *
 * @author aar1069
 */
@Component
@Slf4j
public class OpaAuthorizationManager implements AuthorizationManager<RequestAuthorizationContext> {

    @Value("${opa.url}")
    private String opaUrl;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private OpaHttpClient opaClient;

    @Override
    public AuthorizationDecision authorize(Supplier<? extends Authentication> authentication, RequestAuthorizationContext object) {
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
            OpaResponse response = opaClient.sendOpaRequest(opaUrl, jsonRequest.toString());
            log.info("OPA response: \n" + response);

            // If access to resource API is allowed
            if (response.isAllowed()) {

                List<EntitledRelationship> filterList = response.getFilterList();

                // save the list of entitled relationships to the request session for use in DAL
                object.getRequest().getSession().setAttribute("filteredList", filterList);
                return new AuthorizationDecision(true);
            }

            // If access to resource API is not allowed
            return new AuthorizationDecision(false);

        } catch (JacksonException | JSONException e) {
            log.error("Error building OPA request \n");
            return new AuthorizationDecision(false);

        }

    }

}
