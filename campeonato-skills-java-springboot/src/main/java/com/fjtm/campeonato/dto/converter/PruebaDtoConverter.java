package com.fjtm.campeonato.dto.converter;

import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import com.fjtm.campeonato.dto.CompetidorDto;
import com.fjtm.campeonato.dto.PruebaDto;
import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Prueba;
import com.fjtm.campeonato.service.CompetidorService;
import com.fjtm.campeonato.service.EspecialidadService;
import com.fjtm.campeonato.service.PruebaService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PruebaDtoConverter {


    private final ModelMapper modelMapper;
	private final EspecialidadService especialidadService;
	private final PruebaService pruebaCompetidor;
	
	@PostConstruct
	public void init() {
		modelMapper.addMappings(new PropertyMap<Prueba, PruebaDto>() {

            @Override
            protected void configure() {
        
            }
		});
	}
	
	public PruebaDto convertToDto(Prueba prueba) {
		PruebaDto pruebaDto = new PruebaDto(
                prueba.getEnunciado(),
                prueba.getEspecialidad().getNombre(),
                prueba.getPuntuacionMaxima()
				);
		return pruebaDto;

	}
   
    public Prueba convertToEntity(PruebaDto pruebaDto) {
		Prueba prueba = new Prueba(
                pruebaDto.getEnunciado(),
                especialidadService.findByNombre(pruebaDto.getEspecialidad()).get(),
                pruebaDto.getNotaMaxima()
		);
		return prueba;
    }

}
