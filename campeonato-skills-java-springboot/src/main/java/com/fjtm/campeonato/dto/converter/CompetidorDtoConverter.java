package com.fjtm.campeonato.dto.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.fjtm.campeonato.dto.CompetidorDto;
import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.service.CompetidorService;
import com.fjtm.campeonato.service.EspecialidadService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class CompetidorDtoConverter {

    private final ModelMapper modelMapper;
	private final EspecialidadService especialidadService;
	private final CompetidorService serviceCompetidor;
	
	@PostConstruct
	public void init() {
		modelMapper.addMappings(new PropertyMap<Competidor, CompetidorDto>() {

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
	public CompetidorDto convertToDto(Competidor competidor) {
		CompetidorDto competidorDto = new CompetidorDto(
				competidor.getNombre(),
				competidor.getCentro(),
				competidor.getEspecialidad().getNombre()
				);
		return competidorDto;

	}
   
    public Competidor convertToEntity(CompetidorDto competidorDto) {
		Competidor competidor = new Competidor(
				competidorDto.getNombre(),
				competidorDto.getCentro(),
				especialidadService.findByNombre(competidorDto.getEspecialidad()).get()
		);
		return competidor;
    }

}
