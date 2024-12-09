package com.social.network.authentication.module.component.filter;

import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DefaultBearerTokenResolver {
    private static final String ACCESS_TOKEN_PARAMETER_NAME = "access_token";
    private static final Pattern authorizationPattern = Pattern.compile("^Bearer (?<token>[a-zA-Z0-9-._~+/]+=*)$", 2);
    private boolean allowFormEncodedBodyParameter = false;
    private boolean allowUriQueryParameter = false;
    private String bearerTokenHeaderName = "Authorization";
    public DefaultBearerTokenResolver() {
    }

    public String resolve(final HttpServletRequest request) {
        String authorizationHeaderToken = this.resolveFromAuthorizationHeader(request);
        String parameterToken = this.isParameterTokenSupportedForRequest(request) ? resolveFromRequestParameters(request) : null;
        if (authorizationHeaderToken != null) {
            if (parameterToken != null) {
                throw new BadCredentialsException("");
            } else {
                return authorizationHeaderToken;
            }
        } else {
            return parameterToken != null && this.isParameterTokenEnabledForRequest(request) ? parameterToken : null;
        }
    }

    public void setAllowFormEncodedBodyParameter(boolean allowFormEncodedBodyParameter) {
        this.allowFormEncodedBodyParameter = allowFormEncodedBodyParameter;
    }

    public void setAllowUriQueryParameter(boolean allowUriQueryParameter) {
        this.allowUriQueryParameter = allowUriQueryParameter;
    }

    public void setBearerTokenHeaderName(String bearerTokenHeaderName) {
        this.bearerTokenHeaderName = bearerTokenHeaderName;
    }
    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader(this.bearerTokenHeaderName);
        if (!StringUtils.startsWithIgnoreCase(authorization, "bearer")) {
            return null;
        } else {
            Matcher matcher = authorizationPattern.matcher(authorization);
            if (!matcher.matches()) {
                throw new BadCredentialsException("");
            } else {
                return matcher.group("token");
            }
        }
    }

    private static String resolveFromRequestParameters(HttpServletRequest request) {
        String[] values = request.getParameterValues("access_token");
        if (values != null && values.length != 0) {
            if (values.length == 1) {
                return values[0];
            } else {
                throw new BadCredentialsException("");
            }
        } else {
            return null;
        }
    }

    private boolean isParameterTokenSupportedForRequest(final HttpServletRequest request) {
        return isFormEncodedRequest(request) || isGetRequest(request);
    }

    private static boolean isGetRequest(HttpServletRequest request) {
        return HttpMethod.GET.name().equals(request.getMethod());
    }

    private static boolean isFormEncodedRequest(HttpServletRequest request) {
        return "application/x-www-form-urlencoded".equals(request.getContentType());
    }

    private static boolean hasAccessTokenInQueryString(HttpServletRequest request) {
        return request.getQueryString() != null && request.getQueryString().contains("access_token");
    }

    private boolean isParameterTokenEnabledForRequest(HttpServletRequest request) {
        return this.allowFormEncodedBodyParameter && isFormEncodedRequest(request) && !isGetRequest(request) && !hasAccessTokenInQueryString(request) || this.allowUriQueryParameter && isGetRequest(request);
    }
}
