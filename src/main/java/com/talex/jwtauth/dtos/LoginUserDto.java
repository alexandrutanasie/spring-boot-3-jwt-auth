package com.talex.jwtauth.dtos;

import jakarta.validation.constraints.NotEmpty;

public class LoginUserDto {
    @NotEmpty
    private String email;
    @NotEmpty
    private String password;

    public String getEmail() {
        return email;
    }

    public LoginUserDto setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public LoginUserDto setPassword(String password) {
        this.password = password;
        return this;
    }
}
