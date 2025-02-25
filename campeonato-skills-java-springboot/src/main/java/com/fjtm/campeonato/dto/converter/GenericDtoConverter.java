package com.fjtm.campeonato.dto.converter;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GenericDtoConverter {
    
    private final ModelMapper modelMapper;

    // Convertir objetos de una clase a otra con ModelMapper
    // permite convertir objetos de una clase a otra de forma generica 
    // utilizando el cambio generico de modelMapper
    @PostConstruct
    public void init() {
        // Aquí se pueden agregar configuraciones adicionales para modelMapper
    }

    // convertir de un objeto a un DTO (mapear de un objeto a un DTO)
    // en este caso el dto tiene que tener igual o menor cantidad de campos que el objeto origen
    // ya que se utiliza el cambio generico de modelMapper
    public <S, D> D convertToDto(S origen, Class<D> destino) {
        if (origen == null) {
            return null;  // Evitar mapeo si el objeto es null
        }
        return modelMapper.map(origen, destino);
    }

    // convertir de un DTO a un objeto (mapear de un DTO a un objeto)
    // en este caso el objeto destino tiene que tener un constructor con los mismos o menos parametros que el DTO
    // ya que se utiliza el cambio generico de modelMapper
    public <S, D> S convertToEntity(D dto, Class<S> origen) {
        if (dto == null) {
            return null;  // Evitar mapeo si el DTO es null
        }
        return modelMapper.map(dto, origen);
    }
}
