package com.relevans.controller;

import com.relevans.dto.CancionDto;
import com.relevans.service.CancionService;
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
@RequestMapping("api-relevans/cancion")
public class CancionController {

    private static final Logger LOGGER=Logger.getLogger(PlayListController.class.getName());
    private final CancionService service;

    @PostMapping(consumes = "application/json;charset=UTF-8",produces = "application/json;charset=UTF-8")
    public ResponseEntity<CancionDto> save(@RequestBody CancionDto dto){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save request:{1}",new Object[]{session,dto});
        dto=service.save(session,dto);
        LOGGER.log(Level.INFO,"[{0}]save response:{1}",new Object[]{session,dto});
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @PutMapping(consumes = "application/json;charset=UTF-8",produces = "application/json;charset=UTF-8")
    public ResponseEntity<CancionDto> update(@RequestBody CancionDto dto){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save update:{1}",new Object[]{session,dto});
        dto=service.update(session,dto);
        LOGGER.log(Level.INFO,"[{0}]save update:{1}",new Object[]{session,dto});
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(consumes = "application/json;charset=UTF-8",produces = "application/json;charset=UTF-8")
    public ResponseEntity<CancionDto> findById(@RequestBody Integer id){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save findById:{1}",new Object[]{session,id});
        CancionDto dto=service.readById(session,id);
        LOGGER.log(Level.INFO,"[{0}]save findById:{1}",new Object[]{session,dto});
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping(path ="all",produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<CancionDto>> all(){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save all:{1}",new Object[]{session});
        List<CancionDto> dtoList=service.all(session);
        LOGGER.log(Level.INFO,"[{0}]save all:{1}",new Object[]{session,dtoList});
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }

    @PostMapping(path ="findByPlayList",consumes = "application/json;charset=UTF-8",produces = "application/json;charset=UTF-8")
    public ResponseEntity<List<CancionDto>> findByIdPlayList(@RequestBody Integer idPlayList){
        String session= UUID.randomUUID().toString();
        LOGGER.log(Level.INFO,"[{0}]save findByPlayList:{1}",new Object[]{session,idPlayList});
        List<CancionDto> dtoList=service.findByPlayList(session,idPlayList);
        LOGGER.log(Level.INFO,"[{0}]save findByPlayList:{1}",new Object[]{session,dtoList});
        return new ResponseEntity<>(dtoList, HttpStatus.OK);
    }
}
