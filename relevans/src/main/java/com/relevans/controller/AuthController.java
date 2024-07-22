package com.relevans.controller;

import com.relevans.dto.AuthRes;
import com.relevans.dto.LoginReq;
import com.relevans.dto.RegisterReq;
import com.relevans.service.AuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@RestController
@RequiredArgsConstructor
@RequestMapping("api-relevans/auth")
public class AuthController {
    private static final Logger LOGGER = Logger.getLogger(AuthController.class.getName());
    private final AuthenticationService authenticationService;

    @PostMapping(path = "login", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthRes> login(@RequestBody LoginReq loginReq) {
        String session = UUID.randomUUID().toString();
        LOGGER.log(Level.INFO, "[{0}] Login request: {1}", new Object[]{session, loginReq});
        AuthRes token = authenticationService.login(loginReq);
        LOGGER.log(Level.INFO, "[{0}] Login response: {1}", new Object[]{session, token});
        return new ResponseEntity<>(token, HttpStatus.OK);
    }

    @PostMapping(path = "register", consumes = "application/json", produces = "application/json")
    public ResponseEntity<AuthRes> register(@RequestBody RegisterReq registerReq) {
        String session = UUID.randomUUID().toString();
        LOGGER.log(Level.INFO, "[{0}] Register request: {1}", new Object[]{session, registerReq});
        AuthRes token = authenticationService.register(registerReq);
        LOGGER.log(Level.INFO, "[{0}] Register response: {1}", new Object[]{session, token});
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
}
