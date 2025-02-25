package com.fjtm.campeonato.modelo;

import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.Builder.Default;

@Entity
@Table(name = "evaluacion")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Evaluacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = true)
    private float puntuacionObtenida;
    
    @Temporal(TemporalType.DATE)
    @Column(nullable = false, updatable = false)// de esta forma la fecha actual no se puede modificar
    private Date fechaActual;

    @Column(nullable = true)
    private String estado;

    // se ejecuta antes de guardar el objeto en la base de datos para inicializar la variable con la fecha actual
    @PrePersist 
    protected void onCreate() {
        this.fechaActual = new Date();
        this.estado = "pendiente"; // hay cuatro estados: pendiente/ seleccionado/ borrador/ evaluado
        this.puntuacionObtenida = 0;
    }
    
    @ManyToOne
    @JoinColumn(name = "competidor_idcompetidor", nullable = false, updatable = false)
    private Competidor competidor;
    
    @ManyToOne
    @JoinColumn(name = "prueba_idprueba", nullable = false, updatable = false)
    private Prueba prueba;
    
    @ManyToOne
    @JoinColumn(name = "user_idexperto", nullable = true)
    private User experto;

    // Constructor con parametros antes de guardarlo en la base de datos
    public Evaluacion(Competidor competidor, Prueba prueba) {
        this.competidor = competidor;
        this.prueba = prueba;
        // this.experto = experto;
    }
}
