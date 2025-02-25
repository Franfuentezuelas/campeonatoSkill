package com.fjtm.campeonato.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CompetidorDto {

    private String nombre;
    private String centro;
    private String especialidad;
}