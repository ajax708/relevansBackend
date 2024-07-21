package com.relevans.dto;


import lombok.Data;

import java.io.Serializable;

@Data
public class MinisterioDto implements Serializable {
    private Integer idMinisterio;
    private String nombre;
    private String descripcion;
}