package com.social.network.authentication.module.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.social.network.authentication.module.component.exception.AuthenticationTokenException;
import com.social.network.authentication.module.component.exception.CodeTokenCreateException;
import com.social.network.authentication.module.component.exception.RedisConnectionException;
import com.social.network.authentication.module.component.token.MultiAuthToken;
import com.social.network.authentication.module.dto.JwtSessionDTO;
import com.social.network.authentication.module.service.AuthenticationService.AuthenticationService;
import com.social.network.authentication.module.service.BruteForceService.LockAuthenticationAttempt;
import com.social.network.authentication.module.service.JWTControlService.JWTControlService;
import com.social.network.authentication.module.service.JWTControlService.SessionInfo;
import javassist.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Pattern;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class LoginController {

    private AuthenticationService authenticationService;
    private JWTControlService jwtControlService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(method = RequestMethod.POST, value = "/login")
    private ResponseEntity<?> authenticationUser(HttpServletRequest request,
                                                 @RequestParam(name = "principal") String principal,
                                                 @RequestParam(name = "credentials") String credentials,
                                                 @RequestParam(name = "method") @ValidMethod String method){

        Map<String, Object> response = new HashMap<>();
        try {
            String token = authenticationService.authenticationUser(principal, credentials, method, request);
            response.put("status", true);
            response.put("message", "Authentication successful");
            response.put("jwt", token);
            return ResponseEntity.ok().body(response);
        } catch (NotFoundException | AuthenticationTokenException | JsonProcessingException | RedisConnectionException | LockAuthenticationAttempt e) {
            response.put("message", e.getMessage());
            response.put("service", "auth-service");
            return ResponseEntity.badRequest().body(response);
        }
    }


    @RequestMapping(method = RequestMethod.GET, value = "/logout")
    private ResponseEntity<?> authenticationUser(HttpServletRequest request,
                                                 Authentication authentication){

        Map<String, Object> response = new HashMap<>();
        try {
            MultiAuthToken auth = (MultiAuthToken) authentication;
            String sessionUUID = null;
            List<SessionInfo> jwtActiveList = jwtControlService.getUserSessions(auth.getUserId());
            for (SessionInfo sessionInfo : jwtActiveList){
                if (JwtSessionDTO.getChatHash(sessionInfo.getSessionUUID()).equals(auth.getSessionToken())){
                    sessionUUID = sessionInfo.getSessionUUID();
                    jwtControlService.removeSession(auth.getUserId(),sessionUUID);
                    response.put("sessionClose", auth.getSessionToken());
                }
            }
            response.put("status", "logoutComplete");
            response.put("message", "Goodbye");
            return ResponseEntity.ok().body(response);
        } catch (JsonProcessingException | RedisConnectionException e) {
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @RequestMapping(method = RequestMethod.POST, value = "/validate/user")
    private ResponseEntity<?> validateUserOrSendCode(@RequestParam(name = "principal") String principal,
                                                     @Valid @RequestParam(name = "method") @ValidMethod String method){

        Map<String, Object> response = new HashMap<>();
        try {

            if (principal == null){
                throw new NotFoundException("Login is null...");
            }

            if (authenticationService.checkExistsThisUser(principal, method)){
                response.put("status","userExists");
            }  else {
                throw new NotFoundException("User not found");
            }
            response.put("method",method);
            response.put("principalGlobal",principal);
            return ResponseEntity.ok().body(response);

        } catch (ClassCastException | CodeTokenCreateException | NotFoundException | LockAuthenticationAttempt e){
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }


    @Autowired
    public void setAuthenticationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @Autowired
    public void setJwtControlService(JWTControlService jwtControlService) {
        this.jwtControlService = jwtControlService;
    }

    @Pattern(regexp = "^(byPassword|byCode)$", message = "Method must be either 'byPassword' or 'byCode'")
    private @interface ValidMethod {}

}
