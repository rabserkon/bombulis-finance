package com.social.network.authentication.module.service.CaptchaService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;


import java.io.IOException;

@Service
@Profile("dev")
public class DefaultReCaptchaService implements ICaptchaService {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @Override
    public void processResponse(String response, String ip) throws IOException {
        logger.info("Default processResponse, no reCaptcha validation.");
    }

    @Override
    public String getReCaptchaSite() {
        return null;
    }

    @Override
    public String getReCaptchaSecret() {
        return null;
    }
}
