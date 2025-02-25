package com.fjtm.campeonato.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class EvaluacionDto {
    
    Long id;
    String estado;
    long competidorId;
    CompetidorDto competidorDto;
    PruebaDto pruebaDto;
    UserDto experto;

}
