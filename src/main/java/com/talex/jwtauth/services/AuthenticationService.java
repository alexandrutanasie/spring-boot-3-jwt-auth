package com.talex.jwtauth.services;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.talex.jwtauth.dtos.LoginUserDto;
import com.talex.jwtauth.dtos.RegisterUserDto;
import com.talex.jwtauth.entities.SiteUser;
import com.talex.jwtauth.repositories.SiteUserRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class AuthenticationService {
    private final SiteUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(
        SiteUserRepository userRepository,
        AuthenticationManager authenticationManager,
        PasswordEncoder passwordEncoder
    ) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public SiteUser signup(RegisterUserDto input) {
        SiteUser user = new SiteUser();
            user.setFirstname(input.getFirstname());
            user.setLastname(input.getLastname());
            user.setEmail(input.getEmail());
            user.setPassword(passwordEncoder.encode(input.getPassword()));

        return userRepository.save(user);
    }

    public SiteUser authenticate(LoginUserDto loginUserDto) {
        authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                loginUserDto.getEmail(),
                loginUserDto.getPassword()
            )
        );

        return userRepository.findByEmail(loginUserDto.getEmail()).orElseThrow();
    }
}