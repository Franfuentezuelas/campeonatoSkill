package com.fjtm.campeonato.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fjtm.campeonato.repository.UserRepository;
import com.fjtm.campeonato.util.validacion.JwtAuthenticationFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final UserRepository userRepository;
    private final CorsConfig corsConfig;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // no quiero sessiones ya que es una api
        http.sessionManagement(management -> management.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
        // aqui hay que dejar pasar los cors
        .cors(cors -> cors.configurationSource(corsConfig.corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // Desactiva CSRF
            .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(  "/competidor/all","/css/**", "/js/**", "/images/**", "/favicon.png", "/swagger-ui.html", "/v3/api-docs/**", "/swagger-ui/**", "/v3/api-docs").permitAll() // Excluir del filtro JWT
                //.requestMatchers("/api/login", "/api/register").permitAll() // Permitir POST sin autenticación
                .requestMatchers(HttpMethod.POST,"/api/login").permitAll() // Permitir POST sin autenticación
                .requestMatchers("api/admin/**", "/experto/admin/**", "/api/register").hasRole("ADMIN")
                //.requestMatchers().hasRole("EXPERTO")
                .requestMatchers("/especialidad/**", "/competidor/**").hasAnyRole("ADMIN","EXPERTO")
                .requestMatchers("/evaluacion/**", "/prueba/**").hasRole("EXPERTO")
                //.requestMatchers(HttpMethod.POST,"/especialidad/delete", "/especialidad/update").hasRole("ADMIN")
                .requestMatchers("api/logout").authenticated()
                .anyRequest().authenticated() // Cualquier otra ruta requiere autenticación
            )
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class); // Filtro JWT para rutas autenticadas
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Para encriptar contraseñas
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) 
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
}

