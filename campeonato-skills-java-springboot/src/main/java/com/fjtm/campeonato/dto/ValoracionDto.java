package com.fjtm.campeonato.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ValoracionDto {
    Long id;
    Integer valoracion;
    String comentarios;
}
