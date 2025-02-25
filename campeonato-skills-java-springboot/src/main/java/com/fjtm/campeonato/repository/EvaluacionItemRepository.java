package com.fjtm.campeonato.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fjtm.campeonato.modelo.Evaluacion;
import com.fjtm.campeonato.modelo.EvaluacionItem;
import com.fjtm.campeonato.modelo.Item;

public interface EvaluacionItemRepository extends JpaRepository<EvaluacionItem, Long> {
    // TODO: Implementar métodos adicionales para el repositorio de EvaluacionItem
    
    // Consulta para obtener la suma de las puntuaciones obtenidas de EvaluacionItem
    @Query("SELECT SUM((ei.item.peso * ei.valoracion) / ei.item.gradosConsecuencia) " +
    "FROM EvaluacionItem ei " +
    "JOIN ei.item i " +  // Hacemos el JOIN con la entidad Item (a través de la relación)
    "WHERE ei.evaluacion.id = :evaluacionId")
    // llamar un prodecimiento almacenado jpa
    public Float calcularPuntuacionObtenida( Evaluacion evaluacion);

    public List<EvaluacionItem> findAllByEvaluacion(Evaluacion evaluacion);

    public Optional<EvaluacionItem> findByEvaluacionAndItem(Evaluacion evaluacion, Item item);

}
