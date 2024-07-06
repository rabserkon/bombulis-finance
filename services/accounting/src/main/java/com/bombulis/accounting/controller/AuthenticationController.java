package com.bombulis.accounting.controller;

import com.bombulis.accounting.service.AuthenticationService.AuthenticationService;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private AuthenticationService authenticationService;


    private static final String AUTHORIZATION_HEADER = "Authorization";

    @RequestMapping(value = "/create/token", method = RequestMethod.GET)
    public ResponseEntity<?> createLocalJwtToken(
            @RequestParam String token
    ) throws JwtException, AuthenticationException {
        Map<String, Object> response = new HashMap<>();
        final String localToken = authenticationService.generateLocalToken(token);
        response.put("localToken", localToken);
        return ResponseEntity.ok(response);
    }


    @ExceptionHandler(JwtException.class)
    public ResponseEntity<String> handleException(JwtException ex) {
        return new ResponseEntity<>(ex.getMessage() , HttpStatus.BAD_REQUEST);
    }
}
