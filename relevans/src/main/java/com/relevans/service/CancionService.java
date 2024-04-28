package com.relevans.service;

import com.relevans.dto.CancionDto;
import com.relevans.dto.EventDto;
import com.relevans.repo.CancionRepo;
import com.relevans.repo.model.CancionModel;
import com.relevans.repo.model.EventModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CancionService {

    private final CancionRepo cancionRepo;
    private final ModelMapper modelMapper;

    public CancionDto save(String session, CancionDto eventDto){
        CancionModel model=modelMapper.map(eventDto,CancionModel.class);
        model=cancionRepo.save(model);
        eventDto.setIdCancion(model.getIdCancion());
        return eventDto;
    }
    public CancionDto update(String session,CancionDto eventDto){
        CancionModel model=modelMapper.map(eventDto,CancionModel.class);
        model=cancionRepo.save(model);
        return eventDto;

    }
    public CancionDto readById(String session,Integer id){
        CancionModel model=cancionRepo.findById(id).orElseThrow(()->new RuntimeException("NOT FOUND:"+id));
        CancionDto dto=modelMapper.map(model,CancionDto.class);
        return dto;
    }
    public List<CancionDto> all(String session){
        List<CancionModel> modelList=cancionRepo.findAll();
        List<CancionDto> eventDtoList=modelList.stream().map(model->modelMapper.map(model,CancionDto.class)).toList();
        return eventDtoList;
    }

    public List<CancionDto> findByPlayList(String session,Integer idPlayList){
        List<CancionModel> modelList=cancionRepo.findCancionModelByIdPlayList(idPlayList);
        List<CancionDto> eventDtoList=modelList.stream().map(model->modelMapper.map(model,CancionDto.class)).toList();
        return eventDtoList;
    }
}
