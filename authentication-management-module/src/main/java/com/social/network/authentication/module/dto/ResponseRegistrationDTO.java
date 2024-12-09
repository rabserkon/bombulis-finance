package com.social.network.authentication.module.dto;

import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Getter @Setter
@Data
public class ResponseRegistrationDTO {
    private UserDTO user;
    private String registrationToken;
    private String status;

    public ResponseRegistrationDTO(UserDTO user, String registrationToken) {
        this.user = user;
        this.registrationToken = registrationToken;
    }
}
