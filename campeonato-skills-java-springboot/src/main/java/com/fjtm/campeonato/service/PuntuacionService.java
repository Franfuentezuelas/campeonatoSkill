package com.fjtm.campeonato.service;

import org.springframework.stereotype.Service;

import com.fjtm.campeonato.modelo.Evaluacion;
import com.fjtm.campeonato.util.calculos.ScoreCalculator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PuntuacionService {

     private final ScoreCalculator scoreCalculator;

    public int calcularPuntuacionMaxima() {
        // llamar al método calcularPuntuacion de ScoreCalculator con el objeto combinacion
        return 0;
    }

    public float calcularPuntuacionTotal() {
        // llamar al método calcularPuntuacion de ScoreCalculator con el objeto combinacion
        return 0;
    }

    public float calcularPuntuacionEvaluacion(Evaluacion evaluacion) {
        // llamar al método calcularPuntuacion de ScoreCalculator con el objeto combinacion
        return 0;
    }
}
