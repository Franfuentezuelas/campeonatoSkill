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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Competidor", description = "Operaciones relacionadas con los competidores")
public class CompetidorController {

    private final CompetidorService serviceCompetidor;
    private final EspecialidadService serviceEspecialidad;
    private final CompetidorDtoConverter competidorDtoConverter;
    private final GanadorDtoConverter ganadorDtoConverter;
    private final JwtService jwtService;

    @GetMapping("/all")
    @Operation(summary = "Obtener todos los competidores", description = "Recupera todos los competidores ordenados por especialidad.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de competidores obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron competidores")
    })
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
    @Operation(summary = "Obtener todos los ganadores", description = "Recupera la lista de ganadores ordenados por especialidad.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de ganadores obtenida correctamente")
    })
    public ResponseEntity<List<GanadorDto>> getAllGanadores() {
        // obtengo toda la lista de especialidades, y filtro por codigo no ADM para que no este administrador
        List<GanadorDto> competidores = serviceCompetidor.findAllWinner().stream()
                                        .sorted((a, b) -> a.getEspecialidad().compareTo(b.getEspecialidad()))
                                        .map(ganadorDtoConverter::convertToDto)
                                        .toList();
        return ResponseEntity.ok(competidores);
    }

    @GetMapping("/allesp")
    @Operation(summary = "Obtener competidores por especialidad", description = "Recupera los competidores filtrados por especialidad del usuario autenticado.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de competidores por especialidad obtenida correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron competidores")
    })
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
    @Operation(summary = "Guardar un nuevo competidor", description = "Registra un nuevo competidor en el sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Competidor guardado correctamente"),
        @ApiResponse(responseCode = "404", description = "Error al guardar el competidor")
    })
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
    @Operation(summary = "Actualizar competidor", description = "Actualiza los datos de un competidor existente.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Competidor actualizado correctamente"),
        @ApiResponse(responseCode = "404", description = "Competidor no encontrado")
    })
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
    @Operation(summary = "Eliminar un competidor", description = "Elimina un competidor del sistema.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Competidor eliminado correctamente"),
        @ApiResponse(responseCode = "404", description = "Competidor no encontrado")
    })
    public ResponseEntity<?> delete(@RequestBody Long id) {
        Competidor optionalCompetidor = serviceCompetidor.findById(id).orElseThrow(() -> new NotFoundException("No existe el competidor con ese id", "/competidor/delete"));
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Experto eliminado correctamente"));
    }

    @GetMapping("/evaluacion")
    @Operation(summary = "Obtener competidores para evaluación", description = "Recupera los competidores pendientes para evaluación.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Competidores para evaluación obtenidos correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron competidores")
    })
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
