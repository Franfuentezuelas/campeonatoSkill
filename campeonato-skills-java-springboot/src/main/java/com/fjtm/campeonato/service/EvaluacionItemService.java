package com.fjtm.campeonato.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import com.fjtm.campeonato.modelo.Evaluacion;
import com.fjtm.campeonato.modelo.EvaluacionItem;
import com.fjtm.campeonato.modelo.Item;
import com.fjtm.campeonato.repository.EvaluacionItemRepository;
import com.fjtm.campeonato.service.base.BaseService;

@Service
public class EvaluacionItemService extends BaseService<EvaluacionItem, Long, EvaluacionItemRepository> {

    public List<EvaluacionItem> saveAll(List<EvaluacionItem> evaluacionItems) {
        return repositorio.saveAll(evaluacionItems);
    }

    public List<EvaluacionItem> getAllEvaluacionItemsByEvaluacion(Evaluacion evaluacion) {
        return repositorio.findAllByEvaluacion(evaluacion);
    }

    public Optional<EvaluacionItem> findByEvaluacionAndItem(Evaluacion evaluacion, Item item) {
        return repositorio.findByEvaluacionAndItem(evaluacion, item);
    }

}
