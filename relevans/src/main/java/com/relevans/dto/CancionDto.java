package com.relevans.dto;

import lombok.Data;

@Data
public class CancionDto {

    private Integer idCancion;
    private String nombre;
    private String descripcion;
    private Integer idPlayList;

}
