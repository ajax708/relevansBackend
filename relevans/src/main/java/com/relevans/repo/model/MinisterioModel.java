package com.relevans.repo.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity(name = "ministerio")
public class MinisterioModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idMinisterio;
    private String nombre;
    private String descripcion;

    @ManyToMany
    @JoinTable(
            name = "ministerio_evento",
            joinColumns = @JoinColumn(name = "idMinisterio"),
            inverseJoinColumns = @JoinColumn(name = "idEvento")
    )
    private Set<EventModel> eventos = new HashSet<>();

    @ManyToMany(mappedBy = "ministerios")
    private Set<UserModel> users = new HashSet<>();
}
