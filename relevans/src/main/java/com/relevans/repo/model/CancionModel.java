package com.relevans.repo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Data
@Entity
public class CancionModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idCancion;
    private String nombre;
    private String descripcion;

    private Integer idPlayList;

}
