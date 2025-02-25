package com.fjtm.campeonato.modelo;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

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
@Table(name = "users")  // Ajusta según el nombre real de la tabla
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User implements UserDetails{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 3, max = 50, message = "El nombre debe tener entre 3 y 50 caracteres")
    private String nombre;
    
    @Column(unique = true, nullable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 3, max = 45, message = "El usuario debe tener entre 3 y 45 caracteres")
    private String usuario;
    
    @Column(nullable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 3, max = 100, message = "La contraseña debe tener entre 3 y 100 caracteres")
    private String password;
    
    @Column(nullable = false)
    // si no cumplemos las restricciones del atributo, se lanzará una excepción con el mensaje definido en el atributo
    @Size(min = 3, max = 45, message = "El rol debe tener entre 3 y 45 caracteres")
    private String rol;
    
    @ManyToOne
    @JoinColumn(name = "especialidades_id")
    private Especialidad especialidad;

    // Constructor con parametros antes de guardarlo en la base de datos
    public User(String nombre, String usuario, String password, String rol, Especialidad especialidad) {
        this.nombre = nombre;
        this.usuario = usuario;
        this.password = password;
        // de esta forma me aseguro que si no se para rol se asigna rol de experto y si se para un rol se ajusta al formato indicado
        this.rol = (rol != null ? (rol.startsWith("ROLE_") ? rol : "ROLE_" + rol) : "ROLE_EXPERTO");
        this.especialidad = especialidad;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(
            this.rol.startsWith("ROLE_") ? this.rol : "ROLE_" + this.rol
        ));
    }

    @Override
    public String getUsername() {
        return this.usuario;
    }
}
