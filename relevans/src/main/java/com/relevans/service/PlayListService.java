package com.relevans.service;

import com.relevans.dto.EventDto;
import com.relevans.dto.PlayListDto;
import com.relevans.repo.IPlayList;
import com.relevans.repo.model.EventModel;
import com.relevans.repo.model.PlayListModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PlayListService {
    private final IPlayList repo;
    private final ModelMapper modelMapper;

    public PlayListDto save(String session, PlayListDto dto){
        PlayListModel model=modelMapper.map(dto,PlayListModel.class);
        model=repo.save(model);
        dto.setIdPlaylist(model.getIdPlaylist());
        return dto;
    }
    public PlayListDto update(String session,PlayListDto eventDto){
        PlayListModel model=modelMapper.map(eventDto,PlayListModel.class);
        model=repo.save(model);
        return eventDto;

    }
    public PlayListDto readById(String session,Integer id){
        PlayListModel model=repo.findById(id).orElseThrow(()->new RuntimeException("NOT FOUND:"+id));
        PlayListDto dto=modelMapper.map(model,PlayListDto.class);
        return dto;
    }
    public List<PlayListDto> all(String session){
        List<PlayListModel> modelList=repo.findAll();
        List<PlayListDto> dtoList=modelList.stream().map(model->modelMapper.map(model,PlayListDto.class)).toList();
        return dtoList;
    }
}
