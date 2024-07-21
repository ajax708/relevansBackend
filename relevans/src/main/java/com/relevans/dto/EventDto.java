package com.relevans.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
public class EventDto implements Serializable {
    private Integer idEvento;
    private String nombre;
    private LocalDateTime fecha;
    private String descripcion;
    private String lastUser;
    private String estado;
    private Set<MinisterioDto> ministerios = new HashSet<>();
}