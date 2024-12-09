package com.social.network.authentication.module.service.CaptchaService;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestOperations;

import java.io.IOException;
import java.net.URI;
import java.util.regex.Pattern;

@Service
@Profile("prod")
@PropertySource("classpath:application-${spring.profiles.active}.properties")
public class CaptchaService implements ICaptchaService {

    private RestOperations restTemplate;

    @Value("${google.recaptcha.key.site}")
    @Getter @Setter
    private String site;
    @Value("${google.recaptcha.key.secret}")
    @Getter @Setter
    private String secret;

    private static Pattern RESPONSE_PATTERN = Pattern.compile("[A-Za-z0-9_-]+");

    @Override
    public void processResponse(String response, String ip) throws IOException {
        if(!responseSanityCheck(response)) {
            throw new IOException("Response contains invalid characters");
        }
        URI verifyUri = URI.create(String.format(
                "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s&remoteip=%s",
                getReCaptchaSecret(), response, ip));
        GoogleResponse googleResponse = restTemplate.getForObject(verifyUri, GoogleResponse.class);
        if(!googleResponse.isSuccess()) {
            throw new IOException("reCaptcha was not successfully validated");
        }
    }

    @Override
    public String getReCaptchaSite() {
        return site;
    }

    @Override
    public String getReCaptchaSecret() {
        return secret;
    }

    protected boolean responseSanityCheck(final String response) {
        return StringUtils.hasLength(response) && RESPONSE_PATTERN.matcher(response).matches();
    }

    @Autowired
    public void setRestTemplate(RestOperations restTemplate) {
        this.restTemplate = restTemplate;
    }
}
