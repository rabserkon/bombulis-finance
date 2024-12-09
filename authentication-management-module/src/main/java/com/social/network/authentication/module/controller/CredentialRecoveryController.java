package com.social.network.authentication.module.controller;

import com.social.network.authentication.module.component.exception.AuthenticationTokenException;
import com.social.network.authentication.module.service.CaptchaService.ICaptchaService;
import com.social.network.authentication.module.service.CredentialRecoveryService.CredentialRecoveryService;
import com.social.network.authentication.module.service.CredentialRecoveryService.TokenException;
import com.social.network.authentication.module.service.CredentialRecoveryService.UserAccountException;
import javassist.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
public class CredentialRecoveryController {

    private CredentialRecoveryService recoveryService;
    private ICaptchaService captchaService;

    @RequestMapping(method = RequestMethod.POST, value = "/credential/reset",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    private ResponseEntity<?> credentialReset(HttpServletRequest request,
                                              @RequestParam(name = "principal") String principal,
                                              @RequestParam("g-recaptcha-response") String recaptcha) throws IOException, AuthenticationTokenException, UserAccountException, NotFoundException {
        Map<String, Object> response = new HashMap<>();
        captchaService.processResponse(recaptcha, request.getHeader("X-FORWARDED-FOR"));
        this.recoveryService.createChangePasswordToken(principal);
        response.put("status", "sendRecoveryMessage");
        response.put("method", "recovery");
        response.put("message", "Send recovery password message on your email");
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/credential/update",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    private ResponseEntity<?> credentialUpdate(HttpServletRequest request,
                                               @RequestParam(name = "credentials") String credentials,
                                               @RequestParam(name = "token") String token,
                                               @RequestParam("g-recaptcha-response") String recaptcha) throws IOException, TokenException {
        Map<String, Object> response = new HashMap<>();
        captchaService.processResponse(recaptcha, request.getHeader("X-FORWARDED-FOR"));
        this.recoveryService.changePasswordByToken(token, credentials);
        response.put("status","changePassword");
        response.put("message","Your password update");
        return ResponseEntity.ok().body(response);
    }

    @RequestMapping(method = RequestMethod.POST, value = "/validate/token",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    private ResponseEntity<?> validateToken(HttpServletRequest request,
                                            @RequestParam(name = "token") String token) throws TokenException {
        Map<String, Object> response = new HashMap<>();
        boolean tokenExist = this.recoveryService.checkTokenExist(token);
        response.put("tokenExist", tokenExist);
        return ResponseEntity.ok().body(response);
    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<String> handleIllegalArgumentException(TokenException ex) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAccountException.class)
    public ResponseEntity<String> handleException(UserAccountException ex) {
        return new ResponseEntity<>("Error: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<String> handleException(IOException ex) {
        return new ResponseEntity<>("Recaptcha Exception" , HttpStatus.BAD_REQUEST);
    }

    @Autowired
    public void setCaptchaService(ICaptchaService captchaService) {
        this.captchaService = captchaService;
    }

    @Autowired
    public void setRecoveryService(CredentialRecoveryService recoveryService) {
        this.recoveryService = recoveryService;
    }
}
