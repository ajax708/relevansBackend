package com.relevans.controller;

import com.relevans.dto.UserDto;
import com.relevans.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("api-relevans/user")
public class UserController {
    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());
    private final UserService userService;

    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto) {
        String session = UUID.randomUUID().toString();
        LOGGER.log(Level.INFO, "[{0}] Save request: {1}", new Object[]{session, userDto});
        userDto = userService.save(userDto);
        LOGGER.log(Level.INFO, "[{0}] Save response: {1}", new Object[]{session, userDto});
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }

}
