package com.social.network.authentication.module.service.RegistrationService;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class RegistrationTokenDTO {
    private String code;
    private String tokenUUID;
}
