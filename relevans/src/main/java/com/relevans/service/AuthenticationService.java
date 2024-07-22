package com.relevans.service;

import com.relevans.dto.AuthRes;
import com.relevans.dto.CustomUserDetails;
import com.relevans.dto.LoginReq;
import com.relevans.dto.RegisterReq;
import com.relevans.repo.IUserRepo;
import com.relevans.repo.model.UserModel;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationService {
    private final IUserRepo userRepo;
    private final JwtServices jwtServices;
    private final BCryptPasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(IUserRepo userRepo, JwtServices jwtServices, BCryptPasswordEncoder passwordEncoder, AuthenticationManager authenticationManager) {
        this.userRepo = userRepo;
        this.jwtServices = jwtServices;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
    }

    public AuthRes login(LoginReq req) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));
        UserModel userModel = userRepo.findByUsername(req.getUsername()).orElseThrow();
        UserDetails user = new CustomUserDetails(userModel); // Convertir UserModel a UserDetails
        String token = jwtServices.getToken(user);
        return AuthRes.builder()
                .token(token)
                .build();
    }

    public AuthRes register(RegisterReq req) {
        String encodedPassword = passwordEncoder.encode(req.getPassword());

        UserModel user = UserModel.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(encodedPassword)
                .build();

        userRepo.save(user);

        return AuthRes.builder()
                .token(jwtServices.getToken(new CustomUserDetails(user))) // Convertir UserModel a UserDetails
                .build();
    }
}
