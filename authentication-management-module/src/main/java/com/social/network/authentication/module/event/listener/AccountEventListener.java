package com.social.network.authentication.module.event.listener;

import com.social.network.authentication.module.entity.User;
import com.social.network.authentication.module.event.EnabledConfirmEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Component
public class AccountEventListener {
    private static final int MAX_ACTIVATIONS_PER_DAY = 2;
    private final Map<Long, LocalDateTime> lastActivationTimes = new HashMap<>();

    @EventListener(EnabledConfirmEvent.class)
    public void handleRegistrationConfirmEvent(EnabledConfirmEvent event) {
        User user = event.getUser();
        LocalDateTime activationTime = event.getActivationTime();
        if (canActivate(user.getUserId(), activationTime)) {
            lastActivationTimes.put(user.getUserId(), activationTime);
        }

    }

    private boolean canActivate(Long userId, LocalDateTime activationTime) {
        LocalDateTime lastActivationTime = lastActivationTimes.get(userId);
        if (lastActivationTime != null &&
                ChronoUnit.DAYS.between(lastActivationTime, activationTime) < 1 &&
                getActivationCountForUser(userId) >= MAX_ACTIVATIONS_PER_DAY) {
            return false;
        }
        return true;
    }

    private int getActivationCountForUser(Long userId) {
        return 0;
    }
}
