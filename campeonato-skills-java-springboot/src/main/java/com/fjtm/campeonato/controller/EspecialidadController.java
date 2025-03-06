package com.fjtm.campeonato.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.service.EspecialidadService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import com.fjtm.campeonato.dto.EspecialidadDto;
import com.fjtm.campeonato.dto.converter.EspecialidadDtoConverter;
import com.fjtm.campeonato.dto.converter.GenericDtoConverter;
import com.fjtm.campeonato.error.NotFoundException;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/especialidad")
@RequiredArgsConstructor
@Tag(name = "Especialidad", description = "Operaciones relacionadas con las especialidades")
public class EspecialidadController {

    private final EspecialidadService especialidadService;
    private final ModelMapper modelMapper;
    private final EspecialidadDtoConverter especialidadDtoConverter;
    private final GenericDtoConverter genericDtoConverter;
    
    @GetMapping("/all")
    @Operation(summary = "Obtiene todas las especialidades disponibles")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de especialidades obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class))),
        @ApiResponse(responseCode = "404", description = "No se encontraron especialidades", content = @Content)
    })
    public ResponseEntity<List<Especialidad>> getAll() {
        // obtengo toda la lista de especialidades, y filtro por codigo no ADM para que no este administrador
        List<Especialidad> especialidades = especialidadService.findAll().stream().filter(a->!a.getCodigo().equals("ADM"))
                                                            .sorted((a, b) -> a.getNombre().compareTo(b.getNombre()))
                                                            .toList();
        if(especialidades.size()==0){
            throw new NotFoundException("No hay especialidades", "/especialidad/all");
        }
        return ResponseEntity.ok(especialidades);
    }

    @PostMapping("/save")
    @Operation(summary = "Guarda una nueva especialidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad guardada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class))),
        @ApiResponse(responseCode = "404", description = "Ya existe una especialidad con el código proporcionado", content = @Content)
    })
    public ResponseEntity<?> save(@RequestBody EspecialidadDto especialidadDto) {
        // Convertir el DTO a la entidad
        Especialidad especialidad = especialidadDtoConverter.convertToEntity(especialidadDto);
    
        // Verificar si la especialidad ya existe en la base de datos por código
        Optional<Especialidad> existingEspecialidad = especialidadService.findByCodigo(especialidad.getCodigo());
    
        if (existingEspecialidad.isPresent()) {
            // si existe no la guardamos
            throw new NotFoundException("Ya existe una especialidad con ese código", "/especialidad/save");
        }
    
        // Guardar o actualizar la especialidad
        especialidad = especialidadService.save(especialidad);
    
        return ResponseEntity.ok(especialidad); // Devolver la especialidad guardada/actualizada
    }

    @PostMapping("/update")
    @Operation(summary = "Actualiza una especialidad existente")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad actualizada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class))),
        @ApiResponse(responseCode = "404", description = "No se encontró una especialidad con el código proporcionado", content = @Content)
    })
    public ResponseEntity<?> update(@RequestBody EspecialidadDto especialidadDto) {
        // Convertir el DTO a la entidad
        Especialidad especialidad = especialidadDtoConverter.convertToEntity(especialidadDto);
    
        // Verificar si la especialidad ya existe en la base de datos por código
        Optional<Especialidad> existingEspecialidad = especialidadService.findByCodigo(especialidad.getCodigo());
    
        if (existingEspecialidad.isPresent()) {
            // Si existe, actualizar la especialidad
            especialidad.setId(existingEspecialidad.get().getId()); // Mantener el ID existente para la actualización
        }
    
        // Guardar o actualizar la especialidad
        especialidad = especialidadService.save(especialidad);
    
        return ResponseEntity.ok(especialidad); // Devolver la especialidad guardada/actualizada
    }
    

    @PostMapping("/delete")
    @Operation(summary = "Elimina una especialidad")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad eliminada correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Map.class))),
        @ApiResponse(responseCode = "404", description = "No existe una especialidad con el código proporcionado", content = @Content)
    })
    public ResponseEntity<?> delete(@RequestBody EspecialidadDto especialidadDto) {
        Optional<Especialidad> optionalEspecialidad = especialidadService.findByCodigo(especialidadDto.getCodigo());
        
        if (optionalEspecialidad.isEmpty()) {
            throw new NotFoundException("No existe una especialidad con ese código", "/especialidad/delete");
        }
        
        especialidadService.deleteById(optionalEspecialidad.get().getId());
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Especialidad eliminada correctamente"));
    }




    @GetMapping("/hola")
    @Operation(summary = "Endpoint de prueba 'Hola Mundo'")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Mensaje 'Hola Mundo' retornado correctamente", content = @Content(mediaType = "application/json"))
    })
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hola Mundo");
    }

    @GetMapping("/dto")
    @Operation(summary = "Obtiene todas las especialidades como DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de especialidades DTO obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EspecialidadDto.class)))
    })
    public ResponseEntity<List<EspecialidadDto>> allDto() {
        List<EspecialidadDto> especialidades = especialidadService.findAll()
                .stream()
                .map(especialidadDtoConverter::convertToDto)
                .toList();
        return ResponseEntity.ok(especialidades);
    }

    @GetMapping("/dtogeneric")
    @Operation(summary = "Obtiene todas las especialidades como DTO utilizando un converter genérico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de especialidades DTO genérico obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = EspecialidadDto.class)))
    })
    public ResponseEntity<List<EspecialidadDto>> allDtoGeneric() {
        List<EspecialidadDto> especialidades = especialidadService.findAll()
                .stream()
                .map(especialidad->genericDtoConverter.convertToDto(especialidad, EspecialidadDto.class))
                .toList();
        return ResponseEntity.ok(especialidades);
    }

   
    @PostMapping("/dto3")
    @Operation(summary = "Obtiene una especialidad a partir de un DTO")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class))),
        @ApiResponse(responseCode = "404", description = "No existe una especialidad con el código proporcionado", content = @Content)
    })
    public ResponseEntity<Especialidad> getDto(@RequestBody EspecialidadDto especialidadDto) {
        Especialidad especialidad = new Especialidad();
        especialidad.setCodigo(especialidadDto.getCodigo());
        especialidad.setNombre(especialidadDto.getNombre());
    
        Optional<Especialidad> especial = especialidadService.findByCodigo(especialidad.getCodigo());
    
        return especial.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/dtogeneric")
    @Operation(summary = "Obtiene una especialidad a partir de un DTO con un converter genérico")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Especialidad obtenida correctamente", content = @Content(mediaType = "application/json", schema = @Schema(implementation = Especialidad.class))),
        @ApiResponse(responseCode = "404", description = "No existe una especialidad con el código proporcionado", content = @Content)
    })
    public ResponseEntity<Especialidad> getDtoGeneric(@RequestBody EspecialidadDto especialidadDto) {
        Especialidad especialidad = genericDtoConverter.convertToEntity(especialidadDto, Especialidad.class);
    
        Optional<Especialidad> especial = especialidadService.findByCodigo(especialidad.getCodigo());
    
        return especial.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

}
