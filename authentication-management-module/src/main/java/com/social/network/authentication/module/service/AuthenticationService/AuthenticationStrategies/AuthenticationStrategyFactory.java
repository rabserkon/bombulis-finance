package com.social.network.authentication.module.service.AuthenticationService.AuthenticationStrategies;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class AuthenticationStrategyFactory {

    @Autowired
    @Getter @Setter
    private List<AuthenticationStrategy> strategies;

    public AuthenticationStrategy getStrategy(String method) {
        return strategies.stream()
                .filter(strategy -> strategy.getType().equals(method))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown authentication type: " + method));
    }
}