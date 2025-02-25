package com.fjtm.campeonato.dto.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.fjtm.campeonato.dto.CompetidorDto;
import com.fjtm.campeonato.dto.EspecialidadDto;
import com.fjtm.campeonato.dto.EvaluacionDto;
import com.fjtm.campeonato.dto.PruebaDto;
import com.fjtm.campeonato.dto.UserDto;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.modelo.Evaluacion;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Component
public class EvaluacionDtoConverter {

    private final ModelMapper modelMapper;

    @PostConstruct
	public void init() {
		modelMapper.addMappings(new PropertyMap<Evaluacion, EvaluacionDto>() {

            @Override
            protected void configure() {
        
            }
		});
	}


    public EvaluacionDto convertToDto(Evaluacion evaluacion) {
        EvaluacionDto evaluacionDto = new EvaluacionDto();
        evaluacionDto.setId(evaluacion.getId());
        evaluacionDto.setCompetidorId(evaluacion.getCompetidor().getId());
        evaluacionDto.setEstado(evaluacion.getEstado());
        
        // Mapeo de prueba y competidor
        evaluacionDto.setPruebaDto(modelMapper.map(evaluacion.getPrueba(), PruebaDto.class));
        evaluacionDto.setCompetidorDto(modelMapper.map(evaluacion.getCompetidor(), CompetidorDto.class));
        
        // Verificación si el experto es nulo antes de mapear
        if (evaluacion.getExperto() != null) {
            evaluacionDto.setExperto(modelMapper.map(evaluacion.getExperto(), UserDto.class));
        } else {
            evaluacionDto.setExperto(null);  // Asignar null si experto es null
        }
        
        return evaluacionDto;
    }
    
    public Evaluacion convertToEntity(EvaluacionDto evaluacionDto, Especialidad especialidad) {
       return modelMapper.map(evaluacionDto, Evaluacion.class);
	}
   

}
