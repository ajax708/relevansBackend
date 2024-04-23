package com.relevans.repo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;


import java.time.LocalDateTime;

@Data
@Entity
public class PlayListModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idPlaylist;
    private String info;
    private String name;
    private String url;
    private LocalDateTime fechaRegistro;
    private String lastUser;
}
