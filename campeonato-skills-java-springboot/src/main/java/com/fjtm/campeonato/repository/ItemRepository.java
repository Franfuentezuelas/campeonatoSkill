package com.fjtm.campeonato.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.fjtm.campeonato.modelo.Evaluacion;
import com.fjtm.campeonato.modelo.Item;
import java.util.List;
import com.fjtm.campeonato.modelo.Prueba;


public interface ItemRepository extends JpaRepository<Item, Long> {

    // TODO: Implementar métodos adicionales para el repositorio de Item

    // Método que obtiene todos los items de una prueba usando el objeto Prueba
    public List<Item> findByPrueba(Prueba prueba);

    // Si prefieres usar solo el ID de la prueba directamente
    public List<Item> findByPruebaId(Long pruebaId);

    // Método que obtiene la puntuación máxima de una prueba
    @Query("SELECT SUM(i.peso) FROM Item i WHERE i.prueba.id = :pruebaId")
    public Float puntuacionMaxima(@Param("pruebaId") Long pruebaId);


}
