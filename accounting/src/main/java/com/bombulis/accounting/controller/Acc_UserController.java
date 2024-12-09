package com.bombulis.accounting.controller;

import com.bombulis.accounting.component.Acc_MultiAuthToken;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.service.UserService.Acc_UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/v1/user")
public class Acc_UserController {

    private Acc_UserService userService;

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/create", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
    private ResponseEntity<?> createUser(Authentication auth){
        Map<String, Object> response = new HashMap<>();
        Acc_User user = this.userService.createUser(((Acc_MultiAuthToken) auth).getUserId());
        response.put("status","create");
        response.put("userId", user.getUserId());
        return ResponseEntity.ok(response);
    }

    @Autowired
    public void setUserService(Acc_UserService userService) {
        this.userService = userService;

    }
}
