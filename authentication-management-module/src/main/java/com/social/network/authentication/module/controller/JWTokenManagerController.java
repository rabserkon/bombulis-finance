package com.social.network.authentication.module.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.social.network.authentication.module.component.exception.RedisConnectionException;
import com.social.network.authentication.module.component.token.MultiAuthToken;
import com.social.network.authentication.module.dto.JwtSessionDTO;
import com.social.network.authentication.module.service.JWTControlService.JWTControlService;
import com.social.network.authentication.module.service.JWTControlService.SessionInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
public class JWTokenManagerController {

    private JWTControlService jwtControlService;

    @RequestMapping(method = RequestMethod.GET, value = "/session/open",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    private ResponseEntity<?> getActiveSession(HttpServletRequest request,
                                               Authentication auth){
        Map<String, Object> response = new HashMap<>();
        try {
            MultiAuthToken authToken = (MultiAuthToken) auth;
            List<SessionInfo> jwtActiveList = jwtControlService.getUserSessions(authToken.getUserId());
            response.put("userId", authToken.getUserId());
            response.put("sessions", jwtActiveList.stream().map(i -> new JwtSessionDTO(i)).collect(Collectors.toList()));
            return ResponseEntity.ok().body(response);
        } catch (RedisConnectionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @RequestMapping(method = RequestMethod.POST, value = "/session/remove",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    private ResponseEntity<?> removeActiveJwt(HttpServletRequest request,
                                              Authentication auth,
                                              @RequestParam(name = "sessionHash", required = false) String sessionHash)  {
        Map<String, Object> response = new HashMap<>();
        try {
            MultiAuthToken authToken = (MultiAuthToken) auth;
            String sessionUUID = null;
            if (sessionHash ==  null){
                try {
                    jwtControlService.deleteSessionsByUserId(authToken.getUserId());
                    response.put("jwt_session_remove", "all");
                } catch (JsonProcessingException e) {
                    response.put("isError", true);
                    response.put("error", "session_not_found");
                }
            }
            if (sessionHash != null){
                List<SessionInfo> jwtActiveList = jwtControlService.getUserSessions(authToken.getUserId());
                for (SessionInfo sessionInfo : jwtActiveList){
                    if (JwtSessionDTO.getChatHash(sessionInfo.getSessionUUID()).equals(sessionHash)){
                        sessionUUID = sessionInfo.getSessionUUID();
                    }
                }
                if (sessionUUID != null){
                    try {
                        jwtControlService.removeSession(authToken.getUserId(), sessionUUID);
                        response.put("jwt_session_remove", sessionHash);
                    } catch (JsonProcessingException e) {
                        response.put("isError", true);
                        response.put("error", "session_not_read");
                    }
                } else {
                    response.put("isError", true);
                    response.put("error", "session_not_found");
                }
            }
            response.put("userId", authToken.getUserId());
            return ResponseEntity.ok().body(response);
        } catch (RedisConnectionException e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Autowired
    public void setJwtControlService(JWTControlService jwtControlService) {
        this.jwtControlService = jwtControlService;
    }
}
