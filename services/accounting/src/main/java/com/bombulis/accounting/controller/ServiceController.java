package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.MultiAuthToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
public class ServiceController {
    private final Logger logger = LoggerFactory.getLogger(getClass());

    public final static String SERVICE_NAME = "accounting-service";

    @RequestMapping(method = RequestMethod.GET, value = "/service/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> serviceRequest(){
        Map<String, Object> response = new HashMap<>();
        LocalDateTime currentTime = LocalDateTime.now();
        response.put("status", "running");
        response.put("service", SERVICE_NAME);
        response.put("message", "Service is up and running. Current time: " + currentTime.toString());
        response.put("time", currentTime.toString());
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.GET, value = "/auth/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> authRequest(HttpServletRequest request,
                                          Authentication auth){
        Map<String, Object> response = new HashMap<>();
        MultiAuthToken authToken = (MultiAuthToken) auth;
        LocalDateTime currentTime = LocalDateTime.now();
        response.put("status", authToken.isAuth());
        response.put("service", SERVICE_NAME);
        response.put("userId", authToken.getUserId());
        response.put("message", "User auth status: " + authToken.isAuth());
        response.put("time", currentTime.toString() );
        return ResponseEntity.ok().body(response);
    }
}
