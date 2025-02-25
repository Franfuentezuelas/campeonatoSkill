package com.fjtm.campeonato.modelo;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "item")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Item {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 300)
    @Size(min = 10, max = 300, message = "El contenido debe tener entre 10 y 300 caracteres")
    private String contenido;
    
    @Column(nullable = false)
    private int peso;

    @Column(nullable = false)
    private int gradosConsecuencia;
    
    @ManyToOne
    @JoinColumn(name = "prueba_idprueba")
    private Prueba prueba;

    // Constructor con parametros antes de guardarlo en la base de datos
    public Item(String contenido, int peso, int gradosConsecuencia) {
        this.contenido = contenido;
        this.peso = peso;
        this.gradosConsecuencia = gradosConsecuencia;
    }
}
