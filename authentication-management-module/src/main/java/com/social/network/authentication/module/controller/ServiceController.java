package com.social.network.authentication.module.controller;

import com.social.network.authentication.module.component.token.MultiAuthToken;
import com.social.network.authentication.module.service.CaptchaService.ICaptchaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private ICaptchaService captchaService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(method = RequestMethod.GET, value = "/service/status",
            produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> serviceRequest(){
        Map<String, Object> response = new HashMap<>();
        LocalDateTime currentTime = LocalDateTime.now();
        response.put("status", "running");
        response.put("service", "auth-service");
        response.put("message", "Service is up and running. Current time: " + currentTime.toString());
        response.put("time", currentTime.toString() );
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
        response.put("userId", authToken.getUserId());
        response.put("service", "auth-service");
        response.put("message", "User auth status: " + authToken.isAuth());
        response.put("time", currentTime.toString() );
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/get/recaptcha/key",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    private ResponseEntity<?> credentialUpdate(HttpServletRequest request){
        Map<String, Object> response = new HashMap<>();
        response.put("google_recaptcha_key", captchaService.getReCaptchaSite());
        return ResponseEntity.ok().body(response);
    }

    @Autowired
    public void setCaptchaService(ICaptchaService captchaService) {
        this.captchaService = captchaService;
    }
}
