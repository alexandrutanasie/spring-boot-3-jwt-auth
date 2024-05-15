package com.talex.jwtauth.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.talex.jwtauth.dtos.LoginUserDto;
import com.talex.jwtauth.dtos.RegisterUserDto;
import com.talex.jwtauth.entities.SiteUser;
import com.talex.jwtauth.response.LoginResponse;
import com.talex.jwtauth.services.AuthenticationService;
import com.talex.jwtauth.services.JwtService;
import com.talex.jwtauth.services.UserService;

import jakarta.validation.Valid;

import org.springframework.web.bind.annotation.RequestMapping;



@RestController
@RequestMapping("/api/v1")
public class AuthenticationController {

	private final AuthenticationService authenticationService;
	private final JwtService jwtService;
	private final UserService userService;

	public AuthenticationController(JwtService jwtService, AuthenticationService authenticationService, UserService userService) {
        this.jwtService = jwtService;
        this.authenticationService = authenticationService;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterUserDto registerUserDto, BindingResult bindingResult) {

        Optional<SiteUser> existing = userService.findByEmail(registerUserDto.getEmail());
        if (existing.isPresent()) {
            bindingResult.rejectValue("email", null, "There is already an account registered with that email");
        }

        if (bindingResult.hasErrors()) {
            List<String> errorMessages = new ArrayList<>();
            for (FieldError error : bindingResult.getFieldErrors()) {
                errorMessages.add(error.getDefaultMessage());
            }

            Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.BAD_REQUEST.value());
            response.put("success", false);
            response.put("errors", errorMessages);

            return ResponseEntity.ok(response);
        }

        
            
        SiteUser registeredUser = authenticationService.signup(registerUserDto);
        Map<String, Object> response = new HashMap<>();
            response.put("status", HttpStatus.OK.value());
            response.put("success", true);
            response.put("errors", null);
            response.put("user", registeredUser);
            
        return ResponseEntity.ok(response);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> authenticate(@RequestBody LoginUserDto loginUserDto) {
        try {
            SiteUser authenticatedUser = authenticationService.authenticate(loginUserDto);
            String jwtToken = jwtService.generateToken(authenticatedUser);
            LoginResponse loginResponse = new LoginResponse().setToken(jwtToken).setExpiresIn(jwtService.getExpirationTime());
            return ResponseEntity.ok(loginResponse);
        } catch (AuthenticationException e) {
            // Autentificarea a eșuat, puteți trata cazul aici
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Authentication failed: " + e.getMessage());
        }
    }

    @GetMapping("/current-user")
    public ResponseEntity<SiteUser> getCurrentUser() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        SiteUser currentUser = (SiteUser) authentication.getPrincipal();

        return ResponseEntity.ok(currentUser);
    }
    
}
