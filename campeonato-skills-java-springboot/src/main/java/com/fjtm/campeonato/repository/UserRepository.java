package com.fjtm.campeonato.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.modelo.User;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsuario(String usuario);

    boolean existsByUsuario(String string);

    public List<User> findAllByEspecialidad(Especialidad especialidad);
}

