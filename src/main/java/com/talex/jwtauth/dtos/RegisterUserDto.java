package com.talex.jwtauth.dtos;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class RegisterUserDto {
    @NotEmpty(message = "Firstname is required")
    private String firstname;

    @NotEmpty(message = "Lastname is required")
    private String lastname;
    
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;
}
