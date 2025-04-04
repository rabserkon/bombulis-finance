package com.bombulis.accounting.service.AuthenticationService;

import com.bombulis.accounting.component.Acc_MultiAuthToken;
import com.bombulis.accounting.component.Acc_Role;
import com.bombulis.accounting.entity.Acc_User;
import com.bombulis.accounting.service.UserService.Acc_UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.*;
import java.util.stream.Collectors;

@Service
@PropertySource("classpath:application-${spring.profiles.active}.properties")
@Transactional
public class Acc_AuthenticationServiceImpl implements Acc_AuthenticationService, Acc_JwtService {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    @Autowired
    private Acc_UserService userService;

    private final String SERVICE_NAME = "finance-service";

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public Acc_MultiAuthToken authenticationByJwt(String token, HttpServletRequest request)  {
        return this.authenticationByJwt(token);
    }

    @Override
    public Acc_MultiAuthToken authenticationByJwt(String token) {
        return null;
    }

    @Override
    public String generateLocalToken(String token) {
        return null;
    }


    @Override
    public Map<String, Object> decodeToken(String token) throws JwtException {
        return null;
    }
}
