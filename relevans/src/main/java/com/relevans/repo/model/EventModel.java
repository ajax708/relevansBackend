package com.relevans.repo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "evento")
public class EventModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idEvento;
    private String nombre;
    private LocalDateTime fecha;
    private String descripcion;
    private String lastUser;
    private String estado;

    @ManyToMany(mappedBy = "eventos")
    private Set<MinisterioModel> ministerios = new HashSet<>();
}
