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
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "competidor")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Competidor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    // aplicamos las restricciones del atributo
    @Column(nullable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 3, max = 100, message = "El nombre debe tener entre 3 y 100 caracteres")
    private String nombre;
       
    @Column(nullable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 6, max = 100, message = "El centro debe tener entre 6 y 100 caracteres")
    private String centro;

    @Column(nullable = true)
    @Range(min = 0, max = 100, message = "El valor debe estar entre 0 y 100")
    private float notaTotal=0;
    
    @ManyToOne
    @JoinColumn(name = "especialidades_id_especialidad", nullable = false)
    private Especialidad especialidad;

    // Constructor con parametros antes de guardarlo en la base de datos
    public Competidor(String nombre, String centro, Especialidad especialidad) {
        this.nombre = nombre;
        this.centro = centro;
        this.especialidad = especialidad;
    }

}
