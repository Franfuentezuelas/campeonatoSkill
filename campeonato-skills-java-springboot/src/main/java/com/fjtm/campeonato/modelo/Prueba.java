package com.fjtm.campeonato.modelo;

import org.hibernate.validator.constraints.Range;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "prueba")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prueba {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false, updatable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 3, max = 300, message = "El enunciado debe tener entre 3 y 300 caracteres")
    private String enunciado;
    
    @ManyToOne
    @JoinColumn(name = "especialidades_id_especialidad")
    private Especialidad especialidad;
    
    @Column(nullable = true)
    private int puntuacionMaxima;

    @Column(nullable = true)
    private String archivo;

    // Constructor con parametros antes de guardarlo en la base de datos
    public Prueba(String enunciado, Especialidad especialidad, int puntuacionMaxima) {
        this.enunciado = enunciado;
        this.especialidad = especialidad;
        this.puntuacionMaxima = puntuacionMaxima;
    }
}
