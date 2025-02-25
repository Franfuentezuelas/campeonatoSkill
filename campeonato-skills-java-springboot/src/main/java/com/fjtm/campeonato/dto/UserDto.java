package com.fjtm.campeonato.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String nombre;
    private String usuario;
    private String password;
    private String rol;
    private String especialidad;

}
