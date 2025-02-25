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
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "evaluacion_item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EvaluacionItem  {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "evaluacion_id")
    private Evaluacion evaluacion;
    
    @ManyToOne
    @JoinColumn(name = "item_id")
    private Item item;
    
    @Column(nullable = true)
    // el rango de la valoracion es de 0 a gradosConsecuencia del item
    private int valoracion;
    
    @Column(nullable = true)
    @Size(min=0,max=150, message = "El comentario debe tener entre 0 y 150 caracteres")
    private String comentario;

    // se ejecuta antes de guardar el objeto en la base de datos para inicializar la variable con la fecha actual
    @PrePersist 
    protected void onCreate() {
        this.valoracion = 0;
        this.comentario = "";
    }

    // Constructor con parametros antes de guardarlo en la base de datos
    public EvaluacionItem(Evaluacion evaluacion, Item item) {
        this.evaluacion = evaluacion;
        this.item = item;
    }
}