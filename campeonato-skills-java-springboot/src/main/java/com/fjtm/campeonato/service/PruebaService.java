package com.fjtm.campeonato.service;

import org.springframework.stereotype.Service;

import com.fjtm.campeonato.error.NotFoundException;
import com.fjtm.campeonato.modelo.Prueba;
import com.fjtm.campeonato.repository.PruebaRepository;
import com.fjtm.campeonato.service.base.BaseService;

@Service
public class PruebaService extends BaseService<Prueba, Long, PruebaRepository> {

    public boolean existePrueba(String enunciado) {
        return repositorio.findByEnunciado(enunciado).isPresent();
    }

    public Prueba findByEnunciado(String enunciado) {
        return repositorio.findByEnunciado(enunciado).orElseThrow(() -> new NotFoundException("No existe la prueba indicada", "datos proporcionados par esta peticion erroneos"));
    }
}
