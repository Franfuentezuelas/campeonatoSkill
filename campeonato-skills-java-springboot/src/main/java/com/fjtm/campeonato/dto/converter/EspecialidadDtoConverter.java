package com.fjtm.campeonato.dto.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.fjtm.campeonato.dto.EspecialidadDto;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.service.EspecialidadService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EspecialidadDtoConverter {

    private final ModelMapper modelMapper;
	private final EspecialidadService especialidadService;
	
	@PostConstruct
	public void init() {
		modelMapper.addMappings(new PropertyMap<Especialidad, EspecialidadDto>() {

            @Override
            protected void configure() {
        
            }
		});
	}
	
	/**
	 * Opción 1 con ModelMapper
	 * @param producto
	 * @return
	 */
	public EspecialidadDto convertToDto(Especialidad especialidad) {
		return modelMapper.map(especialidad, EspecialidadDto.class);
	}
   
    public Especialidad convertToEntity(EspecialidadDto especialidadDto) {
		Especialidad especialidad = new Especialidad(
				especialidadDto.getCodigo(),
				especialidadDto.getNombre()
		);
        return modelMapper.map(especialidadDto, Especialidad.class);
    }
}
