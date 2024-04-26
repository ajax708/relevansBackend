package com.relevans.controller;

import com.relevans.dto.EventDto;
import com.relevans.dto.PlayListDto;
import com.relevans.service.EventService;
import com.relevans.service.PlayListService;
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
@RequestMapping("api-relevans/event")
public class EventController {
    private static final Logger LOGGER=Logger.getLogger(PlayListController.class.getName());
    private final EventService service;

    @PostMapping(consumes = "application/json;charset=UTF-8",produces = "application/json;charset=UTF-8")
    public ResponseEntity<EventDto> save(@RequestBody EventDto dto){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save request:{1}",new Object[]{session,dto});
        dto=service.save(session,dto);
        LOGGER.log(Level.INFO,"[{0}]save response:{1}",new Object[]{session,dto});
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json;charset=UTF-8",produces = "application/json;charset=UTF-8")
    public ResponseEntity<EventDto> update(@RequestBody EventDto dto){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save update:{1}",new Object[]{session,dto});
        dto=service.update(session,dto);
        LOGGER.log(Level.INFO,"[{0}]save update:{1}",new Object[]{session,dto});
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json;charset=UTF-8",produces = "application/json;charset=UTF-8")
    public ResponseEntity<EventDto> findById(@RequestBody Integer id){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save findById:{1}",new Object[]{session,id});
        EventDto dto=service.readById(session,id);
        LOGGER.log(Level.INFO,"[{0}]save findById:{1}",new Object[]{session,dto});
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(path ="all",produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<EventDto>> all(){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save all:{1}",new Object[]{session});
        List<EventDto> dtoList=service.all(session);
        LOGGER.log(Level.INFO,"[{0}]save all:{1}",new Object[]{session,dtoList});
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
