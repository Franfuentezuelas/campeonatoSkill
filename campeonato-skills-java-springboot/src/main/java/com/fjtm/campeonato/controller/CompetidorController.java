package com.fjtm.campeonato.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fjtm.campeonato.dto.CompetidorDto;
import com.fjtm.campeonato.dto.CompetidorUpdateRequest;
import com.fjtm.campeonato.dto.GanadorDto;
import com.fjtm.campeonato.dto.UserDto;
import com.fjtm.campeonato.dto.converter.CompetidorDtoConverter;
import com.fjtm.campeonato.dto.converter.GanadorDtoConverter;
import com.fjtm.campeonato.error.NotFoundException;
import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.modelo.User;
import com.fjtm.campeonato.service.CompetidorService;
import com.fjtm.campeonato.service.EspecialidadService;
import com.fjtm.campeonato.service.JwtService;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/competidor")
@RequiredArgsConstructor
public class CompetidorController {

    private final CompetidorService serviceCompetidor;
    private final EspecialidadService serviceEspecialidad;
    private final CompetidorDtoConverter competidorDtoConverter;
    private final GanadorDtoConverter ganadorDtoConverter;
    private final JwtService jwtService;

    @GetMapping("/all")
    public ResponseEntity<List<Competidor>> getAll() {
        // obtengo toda la lista de especialidades, y filtro por codigo no ADM para que no este administrador
        List<Competidor> competidores = serviceCompetidor.findAll()
                                        .stream().sorted((a, b) -> a.getEspecialidad().compareTo(b.getEspecialidad()))
                                        .toList();
        
        if (competidores.size()==0) {
            throw new NotFoundException("No hay competidores", "/competidor/all");
        }
        
        return ResponseEntity.ok(competidores);
    }

    @GetMapping("/winner")
    public ResponseEntity<List<GanadorDto>> getAllGanadores() {
        // obtengo toda la lista de especialidades, y filtro por codigo no ADM para que no este administrador
        List<GanadorDto> competidores = serviceCompetidor.findAllWinner().stream()
                                        .sorted((a, b) -> a.getEspecialidad().compareTo(b.getEspecialidad()))
                                        .map(ganadorDtoConverter::convertToDto)
                                        .toList();
        return ResponseEntity.ok(competidores);
    }

    @GetMapping("/allesp")
    public ResponseEntity<List<Competidor>> getAllCompetidoresEsp(@RequestHeader("Authorization") String token) {
        // obtengo toda la lista de especialidades, y filtro por codigo no ADM para que no este administrador

        String especialidad=jwtService.extractEspecialidad(token);
        System.out.println("Especialidad: " + especialidad);
        List<Competidor> competidores = serviceCompetidor.findAll().stream()
                                                        .sorted((a, b) -> a.getEspecialidad().compareTo(b.getEspecialidad()))
                                                        .filter(a -> "Administrador".equals(especialidad) || a.getEspecialidad().getNombre().equals(especialidad))
                                                        .toList();
        if (competidores.size()==0) {
            throw new NotFoundException("No hay competidores", "/competidor/allesp");
        }
                                                
        return ResponseEntity.ok(competidores);
    }
    
    @PostMapping("/save")
    public ResponseEntity<Competidor> saveCompetidor(@RequestHeader("Authorization") String token, @RequestBody CompetidorDto competidorDto) {
        
        Competidor competidor = competidorDtoConverter.convertToEntity(competidorDto);
        competidor.setId(null); // Asegurar que el ID sea nulo para evitar conflictos en la BD
        Competidor savedCompetidor = serviceCompetidor.save(competidor);
         if(savedCompetidor==null){
            throw new NotFoundException("No se ha guardado el competidor", "/competidor/save");
        }
        return ResponseEntity.ok(savedCompetidor);
    }

    @PostMapping("/update")
    public ResponseEntity<Competidor> updateUser(@RequestHeader("Authorization") String token, @RequestBody CompetidorUpdateRequest requestUpdate) {
        // convierto el objeto a entidad
        Competidor competidor = competidorDtoConverter.convertToEntity(requestUpdate.getCompetidorDto());
        // con el id busco el competidor
        Competidor competidorSave = serviceCompetidor.findById(requestUpdate.getId())
                                                    .orElseThrow(() -> new NotFoundException("No existe el competidor con ese id", "/competidor/update"));
        // actualizo el competidor
        competidor.setId(competidorSave.getId());
        // actualizo el competidor en la base de datos
        competidor = serviceCompetidor.edit(competidor);
        return ResponseEntity.ok(competidor);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody Long id) {
        Competidor optionalCompetidor = serviceCompetidor.findById(id).orElseThrow(() -> new NotFoundException("No existe el competidor con ese id", "/competidor/delete"));
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Experto eliminado correctamente"));
    }

    @GetMapping("/evaluacion")
    public ResponseEntity<List<Competidor>> getAllEvaluacion(@RequestHeader("Authorization") String token) {
        String especialidad=jwtService.extractEspecialidad(token);

        List<Competidor> competidores = serviceCompetidor.findAllEvaluacion().stream().sorted((a, b) -> a.getEspecialidad().compareTo(b.getEspecialidad()))
                .filter(a -> "Administrador".equals(especialidad) || a.getEspecialidad().getNombre().equals(especialidad))
                .toList();
        if (competidores.size()==0) {
            throw new NotFoundException("No hay competidores sin asignar", "/competidor/allesp");
        }
        return ResponseEntity.ok(competidores);
    }

}
