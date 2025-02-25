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
import com.fjtm.campeonato.dto.EspecialidadDto;
import com.fjtm.campeonato.dto.converter.EspecialidadDtoConverter;
import com.fjtm.campeonato.dto.converter.GenericDtoConverter;
import com.fjtm.campeonato.error.NotFoundException;

import lombok.RequiredArgsConstructor;
import lombok.experimental.PackagePrivate;

@RestController
@RequestMapping("/especialidad")
@RequiredArgsConstructor
public class EspecialidadController {

    private final EspecialidadService especialidadService;
    private final ModelMapper modelMapper;
    private final EspecialidadDtoConverter especialidadDtoConverter;
    private final GenericDtoConverter genericDtoConverter;
    
    @GetMapping("/all")
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
    public ResponseEntity<?> delete(@RequestBody EspecialidadDto especialidadDto) {
        Optional<Especialidad> optionalEspecialidad = especialidadService.findByCodigo(especialidadDto.getCodigo());
        
        if (optionalEspecialidad.isEmpty()) {
            throw new NotFoundException("No existe una especialidad con ese código", "/especialidad/delete");
        }
        
        especialidadService.deleteById(optionalEspecialidad.get().getId());
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Especialidad eliminada correctamente"));
    }




    @GetMapping("/hola")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Hola Mundo");
    }

    @GetMapping("/dto")
    public ResponseEntity<List<EspecialidadDto>> allDto() {
        List<EspecialidadDto> especialidades = especialidadService.findAll()
                .stream()
                .map(especialidadDtoConverter::convertToDto)
                .toList();
        return ResponseEntity.ok(especialidades);
    }

    @GetMapping("/dtogeneric")
    public ResponseEntity<List<EspecialidadDto>> allDtoGeneric() {
        List<EspecialidadDto> especialidades = especialidadService.findAll()
                .stream()
                .map(especialidad->genericDtoConverter.convertToDto(especialidad, EspecialidadDto.class))
                .toList();
        return ResponseEntity.ok(especialidades);
    }

   
    @PostMapping("/dto3")
    public ResponseEntity<Especialidad> getDto(@RequestBody EspecialidadDto especialidadDto) {
        Especialidad especialidad = new Especialidad();
        especialidad.setCodigo(especialidadDto.getCodigo());
        especialidad.setNombre(especialidadDto.getNombre());
    
        Optional<Especialidad> especial = especialidadService.findByCodigo(especialidad.getCodigo());
    
        return especial.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/dtogeneric")
    public ResponseEntity<Especialidad> getDtoGeneric(@RequestBody EspecialidadDto especialidadDto) {
        Especialidad especialidad = genericDtoConverter.convertToEntity(especialidadDto, Especialidad.class);
    
        Optional<Especialidad> especial = especialidadService.findByCodigo(especialidad.getCodigo());
    
        return especial.map(ResponseEntity::ok)
                       .orElseGet(() -> ResponseEntity.notFound().build());
    }
    

}
