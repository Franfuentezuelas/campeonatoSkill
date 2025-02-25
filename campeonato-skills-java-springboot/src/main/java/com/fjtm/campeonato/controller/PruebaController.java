package com.fjtm.campeonato.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.http.ResponseEntity.BodyBuilder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fjtm.campeonato.dto.UserDto;
import com.fjtm.campeonato.dto.converter.GenericDtoConverter;
import com.fjtm.campeonato.dto.converter.PruebaDtoConverter;
import com.fjtm.campeonato.error.NotFoundException;
import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.modelo.Evaluacion;
import com.fjtm.campeonato.modelo.EvaluacionItem;
import com.fjtm.campeonato.modelo.Item;
import com.fjtm.campeonato.modelo.Prueba;
import com.fjtm.campeonato.modelo.User;
import com.fjtm.campeonato.service.CompetidorService;
import com.fjtm.campeonato.service.EvaluacionItemService;
import com.fjtm.campeonato.service.EvaluacionService;
import com.fjtm.campeonato.service.ItemService;
import com.fjtm.campeonato.service.JwtService;
import com.fjtm.campeonato.service.PruebaService;
import com.fjtm.campeonato.service.UserService;

import jakarta.transaction.Transactional;

import com.fjtm.campeonato.dto.ItemDto;
import com.fjtm.campeonato.dto.PruebaDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/prueba")
@RequiredArgsConstructor
public class PruebaController {

    private final PruebaService pruebaService;
    private final PruebaDtoConverter pruebaDtoConverter;
    private final GenericDtoConverter genericDtoConverter;
    private final ItemService itemService;
    private final JwtService jwtService;    
    private final CompetidorService competidorService;
    private final UserService userService;
    private final EvaluacionService evaluacionService;
    private final EvaluacionItemService evaluacionItemService;

    @PostMapping("/nueva")
    @Transactional
    public ResponseEntity<?> handleFormData(
            @RequestHeader("Authorization") String token,
            @RequestParam("enunciado") String enunciado,
            @RequestParam("especialidad") String especialidad,
            @RequestParam("totalPeso") int totalPeso,
            @RequestPart("archivo") MultipartFile archivo,
            @RequestParam Map<String, String> allParams) throws IOException {
        

        // Crear el objeto PruebaDto
        PruebaDto pruebaDto = new PruebaDto(enunciado, especialidad, totalPeso);
        // Guardamos la prueba
        Prueba prueba = pruebaDtoConverter.convertToEntity(pruebaDto);
        
        if (prueba == null) {
            throw new NotFoundException("Error al guardar la prueba: error en los datos ", "/prueba/nueva");
        }
        if (pruebaService.existePrueba(prueba.getEnunciado())) {
            // si ya existe una prueba con ese enunciado, lanzo una excepcion
            throw new NotFoundException("Ya existe una prueba con ese enunciado", "/prueba/nueva");
        }
        prueba = pruebaService.save(prueba);
        // Obtener la ID de la prueba para renombrar el archivo
        String pruebaId = String.valueOf(prueba.getId());
    
        // Obtener el nombre original del archivo y la extensión
        String originalFileName = archivo.getOriginalFilename();
        String extension = originalFileName.substring(originalFileName.lastIndexOf("."));
    
        // Crear el nuevo nombre del archivo utilizando la ID de la prueba
        String newFileName = pruebaId + extension;
    
        // Ruta donde se guardará el archivo (puedes ajustar esta ruta a la ubicación que desees)
        Path folderPath = Paths.get("./archivos/pruebas");
    
        // Crear el directorio si no existe
        Files.createDirectories(folderPath);
    
        // Crear el archivo con el nuevo nombre
        Path filePath = Paths.get(folderPath.toString(), newFileName);
    
        // Copiar el archivo del servidor al directorio con el nuevo nombre
        Files.copy(archivo.getInputStream(), filePath);
    
        // Obtener la ruta del archivo
        String filePathString = filePath.toString();
    
        // Actualizar la ruta del archivo en la base de datos
        prueba.setArchivo(filePathString);
    
        // Guardar los cambios en la base de datos
        prueba = pruebaService.edit(prueba);

        // Continuo guardando los items
    
        // Crear la lista de items a partir de los parámetros
        List<ItemDto> itemsDto = new ArrayList<>();
        
        int index = 0;
        while (allParams.containsKey("items[" + index + "].contenido")) {
            String contenido = allParams.get("items[" + index + "].contenido");
            int peso = Integer.parseInt(allParams.get("items[" + index + "].peso"));
            int rangoValoracion = Integer.parseInt(allParams.get("items[" + index + "].rangoValoracion"));
    
            // Crear un nuevo Item y añadirlo a la lista
            ItemDto item = new ItemDto(contenido, peso, rangoValoracion);
            itemsDto.add(item);
    
            index++;
        }

        // declarando la variable final para evitar problemas de referencia y poder llamarla en el stream
        final Prueba pruebaFinal = prueba;  

        List<Item> items = itemsDto.stream()
                                .map(a -> genericDtoConverter.convertToEntity(a, Item.class))  // Convertir los DTOs a entidades
                                .peek(item -> item.setPrueba(pruebaFinal))  // Asignar la prueba a cada entidad
                                .collect(Collectors.toList());  // Recoger los elementos en la lista
    
        // Guardar los items en la base de datos
        items.forEach(item -> {
            // Guarda el item y obten el objeto guardado con su ID
            item = itemService.save(item);  // Guardamos el item y obtenemos el item actualizado con el ID generado
            
            if (item.getId() == null) {
                throw new RuntimeException("Error al guardar el item");
            }   
            
        });

        // ahora genero la evaluacion de todos los usuarios que no tengan evaluacion y sean de la 
        // especialidad que se le paso por parametro al crear la prueba

        // obtengo todos los competidores que no tengan evaluacion aun

        // la evaluacion tiene competidor, experto y prueba
        Especialidad especialidadPrueba= prueba.getEspecialidad();
        List<Competidor> competidores = competidorService.findAllByEspecialidad(especialidadPrueba);
        // List<User> expertos = userService.findAllByEspecialidad(especialidadPrueba);

        // recorro todos los competidores y creo una evaluacion para cada uno
        for (Competidor competidor : competidores) {
            Evaluacion evaluacion = new Evaluacion(competidor, prueba);
            evaluacion = evaluacionService.save(evaluacion);
            if (evaluacion == null) {
                throw new RuntimeException("Error al guardar la evaluacion");
            }
            // ahora hay que guardar los items de la evaluacion
            List<EvaluacionItem> evaluacionItems = new ArrayList<>();
            for (Item item : items) {
                EvaluacionItem evaluacionItem = new EvaluacionItem(evaluacion, item);
                evaluacionItems.add(evaluacionItem);
                //evaluacionItem=evaluacionItemService.save(evaluacionItem);
                // guardo el item y obtengo el objeto guardado con su ID
                if (evaluacionItem == null) {
                    throw new RuntimeException("Error al guardar el item");
                }
            }
            evaluacionItems = evaluacionItemService.saveAll(evaluacionItems);
            if (evaluacionItems == null) {
                throw new RuntimeException("Error al guardar la lista de evaluacionItems");
            }
        }

        // Retornar respuesta
        return ResponseEntity.ok(Map.of("success", true, "message", "Formulario recibido, guardado, guardados items y archivo guardado"));
    }
    
    @PostMapping("/all")
    public ResponseEntity<?> allPruebas( @RequestHeader("Authorization") String token) {
        // obtengo todas las pruebas y dejo solo las de la categoria del experto
        String especialidad=jwtService.extractEspecialidad(token);
        List<Prueba> pruebas = pruebaService.findAll().stream()
        .sorted((a, b) -> a.getEspecialidad().compareTo(b.getEspecialidad()))
        .filter(a -> a.getEspecialidad().getNombre().equals(especialidad))
        .toList();
        
        return ResponseEntity.ok(pruebas);
    }

}    
