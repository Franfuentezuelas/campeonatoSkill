package com.fjtm.campeonato.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.fjtm.campeonato.modelo.Especialidad;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LoginDto {

    private String usuario;
    private String nombre;
    // con formato hora:minuto:segundo dia:mes:año
    // se puede usar el formato de LocalDateTime
    private LocalDateTime fecha= LocalDateTime.now();
    private String rol;
    private String token;
    private EspecialidadDto especialidad;

    // Constructor con parametros antes de guardarlo en la base de datos
    public LoginDto(String usuario, String nombre, String rol, String token, EspecialidadDto especialidad) {
        this.usuario = usuario;
        this.nombre = nombre;
        this.rol = rol;
        this.token = token;
        this.especialidad = especialidad;
    }

}
