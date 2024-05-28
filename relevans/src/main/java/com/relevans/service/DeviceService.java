package com.relevans.service;

import com.relevans.dto.DeviceDto;
import com.relevans.repo.IDeviceRepo;
import com.relevans.repo.model.DeviceModel;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final IDeviceRepo iDevice;
    private final ModelMapper modelMapper;

    public DeviceDto save(String session, DeviceDto deviceDto){
        DeviceModel model=modelMapper.map(deviceDto,DeviceModel.class);
        model = iDevice.save(model);
        deviceDto.setIdDevice(model.getIdDevice());
        return deviceDto;
    }
}
