package com.talex.jwtauth.services;

import java.util.Optional;

import com.talex.jwtauth.entities.SiteUser;

public interface UserService {
    Optional<SiteUser> findByEmail(String email);
}
