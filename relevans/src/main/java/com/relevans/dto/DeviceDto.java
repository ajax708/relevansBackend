package com.relevans.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DeviceDto {
    private Integer idDevice;
    private String codigoDispositivo;
    private String name;
    private LocalDateTime fechaRegistro;
}
