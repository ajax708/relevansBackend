package com.relevans.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class EventDto implements Serializable {
    private Integer idEvento;
    private String nombre;
    private LocalDateTime fechaRegistro;
    private String lastUser;
}
