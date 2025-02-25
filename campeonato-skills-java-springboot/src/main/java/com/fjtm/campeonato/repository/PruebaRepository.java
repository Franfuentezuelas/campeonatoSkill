package com.fjtm.campeonato.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.fjtm.campeonato.modelo.Prueba;

public interface PruebaRepository extends JpaRepository<Prueba, Long> {

    public Optional<Prueba> findByEnunciado(String enunciado);
}
