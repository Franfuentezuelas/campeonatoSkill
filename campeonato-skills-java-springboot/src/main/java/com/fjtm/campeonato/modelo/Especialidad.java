package com.fjtm.campeonato.modelo;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Table(name = "especialidades")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Especialidad {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(unique = true, nullable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 3, max = 3, message = "El codigo debe tener 3 caracteres")
    private String codigo;
    
    @Column(unique = true, nullable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 3, max = 80, message = "El nombre debe tener entre 3 y 80 caracteres")
    private String nombre;

    // Constructor con parametros antes de guardarlo en la base de datos
    public Especialidad(String codigo, String nombre) {
        this.codigo = codigo;
        this.nombre = nombre;
    }


    public int compareTo(Especialidad especialidad) {
        return this.nombre.compareTo(especialidad.getNombre());
       
    }
    
}

