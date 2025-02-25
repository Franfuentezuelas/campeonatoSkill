package com.fjtm.campeonato.dto;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class GuardarDto {

    private Long evaluacionId;
    private List<ValoracionDto> valoraciones;

}
