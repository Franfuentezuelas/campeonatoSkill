package com.fjtm.campeonato.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.fjtm.campeonato.modelo.Competidor;
import com.fjtm.campeonato.modelo.Evaluacion;
import com.fjtm.campeonato.repository.EvaluacionRepository;
import com.fjtm.campeonato.service.base.BaseService;

@Service
public class EvaluacionService extends BaseService<Evaluacion, Long, EvaluacionRepository> {

    public List<Evaluacion> findByCompetidor(Competidor competidor){
        return repositorio.findByCompetidor(competidor);
    }

}
