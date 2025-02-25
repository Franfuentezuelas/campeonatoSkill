package com.fjtm.campeonato.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CompetidorUpdateRequest {
    private long id;
    private CompetidorDto competidorDto;

    // Getters y setters
}