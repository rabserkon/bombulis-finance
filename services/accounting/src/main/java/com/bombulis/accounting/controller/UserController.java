package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.MultiAuthToken;
import com.bombulis.accounting.entity.User;
import com.bombulis.accounting.service.UserService.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class UserController {

    private UserService userService;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createUser(Authentication auth){
        Map<String, Object> response = new HashMap<>();
        User user = this.userService.createUser(((MultiAuthToken) auth).getUserId());
        response.put("status","create");
        response.put("userId", user.getUserId());
        return ResponseEntity.ok(response);
    }

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;

    }
}
