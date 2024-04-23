package com.relevans.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PlayListDto implements Serializable {
    private Integer idPlaylist;
    private String info;
    private String name;
    private String url;
    private LocalDateTime fechaRegistro;
    private String lastUser;
}
