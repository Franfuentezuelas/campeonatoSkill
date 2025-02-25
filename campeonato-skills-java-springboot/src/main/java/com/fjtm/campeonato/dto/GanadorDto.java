package com.fjtm.campeonato.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GanadorDto {
    private String nombre;
    private String centro;
    private String especialidad;
    private float nota;
}
