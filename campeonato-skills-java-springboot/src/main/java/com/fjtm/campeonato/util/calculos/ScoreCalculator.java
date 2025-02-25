package com.fjtm.campeonato.util.calculos;

import java.util.List;

import org.springframework.stereotype.Component;

import com.fjtm.campeonato.modelo.Evaluacion;
import com.fjtm.campeonato.modelo.Item;
import com.fjtm.campeonato.modelo.Prueba;
import com.fjtm.campeonato.repository.ItemRepository;
import com.fjtm.campeonato.repository.EvaluacionItemRepository;
import com.fjtm.campeonato.repository.EvaluacionRepository;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Component
@AllArgsConstructor
@NoArgsConstructor
public class ScoreCalculator {

    private ItemRepository itemRepository;
    private EvaluacionItemRepository evaluacionItemRepository;
    private EvaluacionRepository evaluacionRepository;


    public float calcularPuntuacionMaxima(Long pruebaId) {
        float maxima = 0;
        // pido al repositorio todos los items de una prueba y sumo los valores de peso con stream
        maxima = itemRepository.findByPruebaId(pruebaId).stream()
                               .mapToInt(Item::getPeso)  // Mapea cada item a su peso
                               .sum();                   // Suma todos los pesos
        return maxima;
    }

    public float calcularNotaTotal(Long userId) {
        // TODO: Implementar la lógica de cálculo de la puntuación
        return evaluacionRepository.sumaPuntuacionObtenidaPorCompetidor(userId);
    }

    public float calcularPuntuacionEvaluacion(Evaluacion evaluacion) {
        return evaluacionItemRepository.calcularPuntuacionObtenida(evaluacion);
    }

    public float puntuacionMaxima(Long pruebaId) {
        return itemRepository.puntuacionMaxima(pruebaId);
    }
}
