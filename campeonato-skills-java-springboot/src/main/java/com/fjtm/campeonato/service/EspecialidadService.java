package com.fjtm.campeonato.service;

import java.util.Optional;

import org.springframework.stereotype.Service;

import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.repository.EspecialidadRepository;
import com.fjtm.campeonato.service.base.BaseService;

@Service
public class EspecialidadService extends BaseService<Especialidad, Long, EspecialidadRepository> {

    // TODO: Implementar métodos adicionales para el servicio de Especialidad
    // Ejemplo: obtener todos los competidores de una especialidad

    public Optional<Especialidad> findByCodigo(String codigo) {
        return repositorio.findByCodigo(codigo);
    }

    public Optional<Especialidad> findByNombre(String nombre){
        return repositorio.findByNombre(nombre);
    }
}
