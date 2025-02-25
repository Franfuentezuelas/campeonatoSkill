package com.fjtm.campeonato.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.repository.CompetidorRepository;
import com.fjtm.campeonato.service.base.BaseService;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CompetidorService extends BaseService<Competidor, Long, CompetidorRepository> {

    private final CompetidorRepository competidorRepository;

    // TODO: Implementar métodos adicionales para el servicio de Competidor
    // Ejemplo: obtener todos los competidores de una especialidad

    public Competidor findByNombreAndCentroAndEspecialidad(String nombre, String centro, Especialidad especialidad) {
        return repositorio.findByNombreAndCentroAndEspecialidad(nombre, centro, especialidad);
    }
    
    public List<Competidor> findAllWinner() {
        return competidorRepository.findCompetidoresConNotaMaximaPorEspecialidad();
    }

    public List<Competidor> findAllEvaluacion() {
        return competidorRepository.findAllEvaluacion();
    }

    public List<Competidor> findAllByEspecialidad(Especialidad especialidad) {
        return competidorRepository.findAllByEspecialidad(especialidad);
    }
}
