package com.relevans.controller;

import com.relevans.dto.DeviceDto;
import com.relevans.service.DeviceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequiredArgsConstructor
@RequestMapping("api-relevans/device")
public class DeviceController {
    public static final Logger LOGGER = Logger.getLogger(DeviceController.class.getName());
    private final DeviceService service;

    @PostMapping(consumes = "application/json;charset=UTF-8", produces = "application/json;charset=UTF-8")
    public ResponseEntity<DeviceDto> save(@RequestBody DeviceDto dto) {
        String session = UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save request:{1}",new Object[]{session,dto});
        service.save(session,dto);
        LOGGER.log(Level.INFO,"[{0}]save response:{1}",new Object[]{session,dto});
        LOGGER.info("save response");
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }
}
