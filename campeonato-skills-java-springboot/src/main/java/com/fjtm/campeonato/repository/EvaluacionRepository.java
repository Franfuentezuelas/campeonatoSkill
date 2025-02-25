package com.fjtm.campeonato.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Evaluacion;

public interface EvaluacionRepository extends JpaRepository<Evaluacion, Long>{

    // Consulta para obtener la suma de puntuacionObtenida por competidor
    @Query("SELECT SUM(e.puntuacionObtenida) FROM Evaluacion e WHERE e.competidor.id = :competidorId")
    Float sumaPuntuacionObtenidaPorCompetidor(@Param("competidorId") Long competidorId);

    public List<Evaluacion> findByCompetidor(Competidor competidor);
}
