package com.relevans.dto;

import lombok.Data;

import java.util.HashSet;
import java.util.Set;

@Data
public class UserDto {
    private Integer idUser;
    private String username;
    private String email;
    private String password;
    private String idDevice;
    private Set<MinisterioDto> ministerios = new HashSet<>();
}
