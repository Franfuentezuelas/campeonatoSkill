package com.fjtm.campeonato.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fjtm.campeonato.dto.EvaluacionDto;
import com.fjtm.campeonato.dto.GuardarDto;
import com.fjtm.campeonato.dto.converter.EvaluacionDtoConverter;
import com.fjtm.campeonato.error.NotFoundException;
import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Evaluacion;
import com.fjtm.campeonato.modelo.EvaluacionItem;
import com.fjtm.campeonato.modelo.Item;
import com.fjtm.campeonato.modelo.User;
import com.fjtm.campeonato.service.CompetidorService;
import com.fjtm.campeonato.service.EvaluacionItemService;
import com.fjtm.campeonato.service.EvaluacionService;
import com.fjtm.campeonato.service.ItemService;
import com.fjtm.campeonato.service.JwtService;
import com.fjtm.campeonato.service.PuntuacionService;
import com.fjtm.campeonato.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;


import jakarta.persistence.criteria.CriteriaBuilder.In;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/evalucacion")
@RequiredArgsConstructor
public class EvalucacionController {

    private final EvaluacionService evalucacionservice;
    private final JwtService jwtService;
    private final EvaluacionDtoConverter EvalucacionDtoConverter;
    private final EvaluacionService evaluacionService;
    private final UserService userService;
    private final EvaluacionItemService evaluacionItemService;
    private final ItemService itemService;
    private final PuntuacionService puntuacionService;
    private final CompetidorService competidorService;

    @GetMapping("/all")
    @Operation(summary = "Obtener todas las evaluaciones",
               description = "Este endpoint devuelve todas las evaluaciones filtradas por especialidad.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Lista de evaluaciones obtenidas correctamente"),
        @ApiResponse(responseCode = "404", description = "No se encontraron evaluaciones")
    })
    public List<EvaluacionDto> getAll( @RequestHeader("Authorization") String token) {
        String especialidad=jwtService.extractEspecialidad(token);
        // obtengo todas las evaluaciones
        List<EvaluacionDto> evaluacionesDto = evalucacionservice.findAll().stream()
                                        .filter(a -> "Administrador".equals(especialidad) || a.getCompetidor().getEspecialidad().getNombre().equals(especialidad))
                                        .map(EvalucacionDtoConverter::convertToDto)
                                        .toList();
        if (evaluacionesDto.size()==0) {
            throw new NotFoundException("No hay evaluaciones sin asignar", "/evaluacion/all");
        }
        return evaluacionesDto;
    }

    @PostMapping("/experto")
    @Transactional
    @Operation(summary = "Asignar experto a una evaluación",
               description = "Este endpoint asigna un experto a una evaluación existente y cambia el estado a seleccionado, impidiendo que otro experto la seleccione. Ademas todo se realiza en una transacción para asegurar la integridad de los datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Experto asignado correctamente"),
        @ApiResponse(responseCode = "404", description = "Evaluación o experto no encontrado"),
        @ApiResponse(responseCode = "403", description = "Experto ya asignado a la evaluación")
    })
    public List<EvaluacionItem> setExperto( @RequestHeader("Authorization") String token, @RequestBody Long idEvaluacion) {
        String especialidad=jwtService.extractEspecialidad(token);
        // obtener la evaluacion con el id
        Evaluacion evaluacion = evaluacionService.findById(idEvaluacion).orElseThrow(() -> new NotFoundException("No existe la evaluacion con ese id", "/evaluacion/experto"));
        // obtener el experto
        User experto = userService.findByUsuario(jwtService.extractUsername(token)).orElseThrow(() -> new NotFoundException("No existe el usuario indicado", "/evaluacion/experto"));
        if (evaluacion.getExperto() == null) {
            // asignar el experto a la evaluacion
            evaluacion.setExperto(experto);
            evaluacion.setEstado("seleccionado");
            evaluacion = evaluacionService.save(evaluacion);
        }

        if (evaluacion == null) {
            throw new NotFoundException("No se ha guardado la evaluacion", "/evaluacion/all");
        }

        if(evaluacion.getExperto().getId()!=experto.getId()){
            throw new NotFoundException("El expertoya estaba asignado y no es usted", "/evaluacion/all");
        }

        List<EvaluacionItem> evaluacionItems = evaluacionItemService.getAllEvaluacionItemsByEvaluacion(evaluacion);


        evaluacionItems.forEach(item->{
            System.out.println("---------------------------------------");
            System.out.println(item.getId());
            System.out.println(item.getValoracion());
            System.out.println(item.getComentario());
        }
        );
        // List<Item> items = new ArrayList<>();

        // for (EvaluacionItem evaluacionItem : evaluacionItems) {
        //     items.add(evaluacionItem.getItem());
        // }

        // creo un itemDto para mandar solo lo que necesito
        return evaluacionItems;
    }


    @PostMapping("/guardar")
    @Transactional
    @Operation(summary = "Guardar evaluación con valoraciones",
               description = "Este endpoint guarda una evaluación con las valoraciones de los ítems, no se realiza el calculo de la puntuación. Ademas todo se realiza en una transacción para asegurar la integridad de los datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluación guardada correctamente"),
        @ApiResponse(responseCode = "404", description = "Evaluación o experto no encontrado"),
        @ApiResponse(responseCode = "403", description = "Experto no es el mismo que el que guardo en la evaluacion"),
        @ApiResponse(responseCode = "403", description = "No existe la evaluacion con ese id"),
        @ApiResponse(responseCode = "400", description = "Valoraciones no válidas")
    })
    public ResponseEntity<String> saveEvaluacion( @RequestHeader("Authorization") String token, @RequestBody GuardarDto guardarDto) {
       
        // obtener la evaluacion con el id
        final Evaluacion evaluacion = evaluacionService.findById(guardarDto.getEvaluacionId()).orElseThrow(() -> new NotFoundException("No existe la evaluacion con ese id", "/evaluacion/guardar"));
        // obtener el experto
        User experto = userService.findByUsuario(jwtService.extractUsername(token)).orElseThrow(() -> new NotFoundException("No existe el usuario indicado", "/evaluacion/guardar"));
        
        if(evaluacion.getExperto().getId()!=experto.getId()){
            throw new NotFoundException("El experto no es el mismo que el que guardo en la evaluacion", "/evaluacion/guardar");
        }
        
        guardarDto.getValoraciones().forEach(valoracionDto -> {

            EvaluacionItem evalItem = evaluacionItemService.findById(valoracionDto.getId()).orElseThrow(() -> new NotFoundException("No existe el item con ese id", "/evaluacion/guardar"));
                evalItem.setValoracion(valoracionDto.getValoracion());
                evalItem.setComentario(valoracionDto.getComentarios());
                evalItem=evaluacionItemService.edit(evalItem);
        });

        if (evaluacion.getEstado().equals("evaluado")) {
            evaluacion.setEstado("evaluado");
        }else{
            evaluacion.setEstado("borrador");
        }

        evaluacionService.save(evaluacion);

        return ResponseEntity.ok().body("Evaluacion guardada con exito");
    }

    @PostMapping("/finalizar")
    @Transactional
    @Operation(summary = "Finalizar evaluación",
               description = "Este endpoint finaliza una evaluación y cambia el estado a evaluado, calcula la puntuacion, y se actualiza en la evaluacion ademas se ajusta tambien la nota total del competidor para la clasificacion de ganadores. Ademas todo se realiza en una transacción para asegurar la integridad de los datos.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Evaluacion finalizada con exito"),
        @ApiResponse(responseCode = "403", description = "No existe el usuario indicado"),
        @ApiResponse(responseCode = "403", description = "Experto no es el mismo que el que guardo en la evaluacion"),
        @ApiResponse(responseCode = "403", description = "No existe la evaluacion con ese id")
    })
    public ResponseEntity<String> finalizarEvaluacion( @RequestHeader("Authorization") String token, @RequestBody GuardarDto guardarDto) {
        
        // obtener la evaluacion con el id
        final Evaluacion evaluacion = evaluacionService.findById(guardarDto.getEvaluacionId()).orElseThrow(() -> new NotFoundException("No existe la evaluacion con ese id", "/evaluacion/guardar"));
        // obtener el experto
        User experto = userService.findByUsuario(jwtService.extractUsername(token)).orElseThrow(() -> new NotFoundException("No existe el usuario indicado", "/evaluacion/guardar"));
        
        if(evaluacion.getExperto().getId()!=experto.getId()){
            throw new NotFoundException("El experto no es el mismo que el que guardo en la evaluacion", "/evaluacion/guardar");
        }
        
        guardarDto.getValoraciones().forEach(valoracionDto -> {

            EvaluacionItem evalItem = evaluacionItemService.findById(valoracionDto.getId()).orElseThrow(() -> new NotFoundException("No existe el item con ese id", "/evaluacion/guardar"));
                evalItem.setValoracion(valoracionDto.getValoracion());
                evalItem.setComentario(valoracionDto.getComentarios());
                evalItem=evaluacionItemService.edit(evalItem);
        });

        List<EvaluacionItem> itemEvaluados = evaluacionItemService.getAllEvaluacionItemsByEvaluacion(evaluacion);

        evaluacion.setPuntuacionObtenida(0);
        
        itemEvaluados.forEach(item -> {
            
            evaluacion.setPuntuacionObtenida(evaluacion.getPuntuacionObtenida()+ ((float) item.getValoracion()*item.getItem().getPeso()/item.getItem().getGradosConsecuencia()));
        });

        // calcular la parte de la puntuacion de la evaluacion seria 
       
        // la suma total de puntos obtenidos por cada item entre los puntos totales de la prueba
        // ahora seria guardar la puntuacion en el competidor que es la suma de evaluaciones
        evaluacion.setEstado("evaluado");
        evaluacionService.save(evaluacion);

        Competidor competidor = evaluacion.getCompetidor();
        
        List<Evaluacion> evaluaciones = evaluacionService.findByCompetidor(competidor);

        competidor.setNotaTotal(0);

        evaluaciones.forEach(eva->{
            competidor.setNotaTotal(competidor.getNotaTotal()+ eva.getPuntuacionObtenida());
        }
        );

        competidorService.edit(competidor);

        return ResponseEntity.ok().body("Evaluacion finalizada con exito");
    }

}


