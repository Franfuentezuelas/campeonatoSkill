package com.fjtm.campeonato.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fjtm.campeonato.modelo.Especialidad;

public interface EspecialidadRepository extends JpaRepository<Especialidad, Long>{
    // TODO: Implementar métodos adicionales para el repositorio de Especialidad

    public Optional<Especialidad> findByCodigo(String codigo);

    public Optional<Especialidad> findByNombre(String nombre);
}
