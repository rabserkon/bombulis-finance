package com.social.network.authentication.module.dto;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class AuthenticationRequestDTO {
    @NotNull(message = "Principal must not be null")
    @NotBlank(message = "Principal must not be empty")
    private String principal;

    @NotNull(message = "Credentials must not be null")
    @NotBlank(message = "Credentials must not be empty")
    private String credentials;

    @NotNull(message = "Method must not be null")
    @NotBlank(message = "Method must not be empty")
    private String method;
}
