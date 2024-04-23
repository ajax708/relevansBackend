package com.relevans.service;

import com.relevans.dto.EventDto;
import com.relevans.repo.IEvent;
import com.relevans.repo.model.EventModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EventService {
    private final IEvent eventRepo;
    private final ModelMapper modelMapper;

    public EventDto save(String session,EventDto eventDto){
        EventModel eventModel=modelMapper.map(eventDto,EventModel.class);
        eventModel=eventRepo.save(eventModel);
        eventDto.setIdEvento(eventModel.getIdEvento());
        return eventDto;
    }
    public EventDto update(String session,EventDto eventDto){
        EventModel eventModel=modelMapper.map(eventDto,EventModel.class);
        eventModel=eventRepo.save(eventModel);
        return eventDto;

    }
    public EventDto readById(String session,Integer id){
            EventModel model=eventRepo.findById(id).orElseThrow(()->new RuntimeException("NOT FOUND:"+id));
        EventDto dto=modelMapper.map(model,EventDto.class);
            return dto;
    }
    public List<EventDto> all(String session){
        List<EventModel> eventModelList=eventRepo.findAll();
        List<EventDto> eventDtoList=eventModelList.stream().map(model->modelMapper.map(model,EventDto.class)).toList();
        return eventDtoList;
    }
}
