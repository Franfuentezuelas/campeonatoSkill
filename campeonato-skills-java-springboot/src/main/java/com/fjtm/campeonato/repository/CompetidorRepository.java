package com.fjtm.campeonato.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Especialidad;

public interface CompetidorRepository extends JpaRepository<Competidor, Long> {
    
    // TODO: Implementar métodos adicionales para el repositorio de Competidor
    // Ejemplo: obtener todos los competidores de una especialidad7

    public Competidor findByNombreAndCentroAndEspecialidad(String nombre, String centro, Especialidad especialidad);

    // quiero obtener todos los competidores que hayan ganado en su especialidad
    // seran los que tengan
    @Query("SELECT c FROM Competidor c WHERE c.notaTotal = (" +
    "SELECT MAX(c2.notaTotal) FROM Competidor c2 WHERE c2.especialidad.id = c.especialidad.id) " +
    "AND c.notaTotal > 0 " +
    "ORDER BY c.especialidad.id")
    public List<Competidor> findCompetidoresConNotaMaximaPorEspecialidad();


    // quiero obtener todos los competidores que no tengan evaluacion
    // aun no se le ha asignado una prueba y portanto tampoco tienen evaluacion ni evaluacion-items
    @Query("SELECT e FROM Evaluacion e LEFT JOIN e.competidor c WHERE e.id IS NULL")
    public List<Competidor> findAllEvaluacion();

    public List<Competidor> findAllByEspecialidad(Especialidad especialidad);

}

