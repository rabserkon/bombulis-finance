package com.social.network.authentication.module.controller;

import com.social.network.authentication.module.dto.ResponseRegistrationDTO;
import com.social.network.authentication.module.dto.UserDTO;
import com.social.network.authentication.module.service.CaptchaService.ICaptchaService;
import com.social.network.authentication.module.service.CredentialRecoveryService.TokenException;
import com.social.network.authentication.module.service.RegistrationService.RegistrationException;
import com.social.network.authentication.module.service.RegistrationService.RegistrationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@RestController
@Validated
public class RegistrationController {

    private ICaptchaService captchaService;
    private RegistrationService registrationService;

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @RequestMapping(method = RequestMethod.POST, value = "/registration")
    public ResponseEntity<?> registration(HttpServletRequest request,
                                               @Valid @RequestBody UserDTO userDTO,
                                               @RequestParam("g-recaptcha-response") String recaptcha) throws RegistrationException, IOException {
            Map<String, Object> response = new HashMap<>();
            captchaService.processResponse(recaptcha, request.getHeader("X-FORWARDED-FOR"));
            ResponseRegistrationDTO token = registrationService.registrationUserAndConfirmToken(userDTO);
            token.setStatus("accountCreate");
            return ResponseEntity.ok().body(token);
    }


    @RequestMapping(method = RequestMethod.GET, value = "/registration/token/confirm")
    public ResponseEntity<?> registrationTokenConfirm(HttpServletRequest request,
                                          @RequestParam(name = "token") String token,
                                          @RequestParam(name = "code") String code) throws TokenException {
        Map<String, Object> response = new HashMap<>();
        registrationService.confirmRegistrationTokenByToken(token,code);
        response.put("status", "accountConfirm");
        return ResponseEntity.ok().body(response);

    }

    @ExceptionHandler(TokenException.class)
    public ResponseEntity<?> handleException(TokenException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response , HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RegistrationException.class)
    public ResponseEntity<?> handleException(RegistrationException ex) {
        Map<String, Object> response = new HashMap<>();
        response.put("error",ex.getMessage());
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
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
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }
}
