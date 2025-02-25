package com.fjtm.campeonato.dto.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.fjtm.campeonato.dto.CompetidorDto;
import com.fjtm.campeonato.dto.EspecialidadDto;
import com.fjtm.campeonato.dto.GanadorDto;
import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.service.CompetidorService;
import com.fjtm.campeonato.service.EspecialidadService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class GanadorDtoConverter {

    private final ModelMapper modelMapper;
	private final EspecialidadService especialidadService;
	private final CompetidorService serviceCompetidor;
	
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
	public GanadorDto convertToDto(Competidor competidor) {
		GanadorDto ganadorDto = new GanadorDto(
				competidor.getNombre(),
				competidor.getCentro(),
				competidor.getEspecialidad().getNombre(),
                competidor.getNotaTotal()
				);
		return ganadorDto;
	}
   
    public Competidor convertToEntity(CompetidorDto competidorDto) {
		Competidor competidor = new Competidor(
				competidorDto.getNombre(),
				competidorDto.getCentro(),
				especialidadService.findByNombre(competidorDto.getEspecialidad()).get()
		);

		competidor= serviceCompetidor.findByNombreAndCentroAndEspecialidad(competidor.getNombre(), competidor.getCentro(), competidor.getEspecialidad());
		return competidor;
    }

}
