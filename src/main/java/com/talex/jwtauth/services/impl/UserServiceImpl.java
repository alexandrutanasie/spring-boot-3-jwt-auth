package com.talex.jwtauth.services.impl;

import org.springframework.stereotype.Service;

import com.talex.jwtauth.entities.SiteUser;
import com.talex.jwtauth.repositories.SiteUserRepository;
import com.talex.jwtauth.services.UserService;

import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

 
    private SiteUserRepository userRepository;

    UserServiceImpl(SiteUserRepository userRepository){
        this.userRepository = userRepository;
    }


    public Optional<SiteUser> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}