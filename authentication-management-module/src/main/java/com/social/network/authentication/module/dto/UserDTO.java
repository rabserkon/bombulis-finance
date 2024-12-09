package com.social.network.authentication.module.dto;


import com.social.network.authentication.module.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.util.Date;

@NoArgsConstructor
public class UserDTO {
    @Getter@Setter
    @Email(message = "Invalid email address")
    @NotNull
    @NotNull(message = "Email is null")
    private String email;

    @Getter@Setter
    @NotBlank(message = "Login must not be blank")
    @Size(min = 3, max = 15, message = "Login length must be between 3 and 15 characters")
    @NotNull(message = "Login is null")
    @Pattern(regexp = "^[0-9a-zA-Z_#$%]+$", message = "Login must contain only letters, digits, and symbols: '_', '#', '$', '%'")
    private String login;

    @NotNull(message = "Password is null")
    @Getter@Setter
    @NotBlank(message = "Password must not be blank")
    @Size(min = 8, message = "Password must be at least 8 characters long")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).+$", message = "Password must contain at least one lowercase letter, one uppercase letter, and one digit")
    private String password;


    public UserDTO(User user) {
        this.email = user.getEmail();
        this.login = user.getLogin();
        this.password = "[encrypted]";
    }
}
