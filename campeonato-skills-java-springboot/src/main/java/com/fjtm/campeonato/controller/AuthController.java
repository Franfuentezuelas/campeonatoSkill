package com.fjtm.campeonato.controller;


import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.boot.autoconfigure.jms.JmsProperties.Listener.Session;
import org.springframework.boot.autoconfigure.security.oauth2.resource.OAuth2ResourceServerProperties.Jwt;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ObservationAuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fjtm.campeonato.dto.EspecialidadDto;
import com.fjtm.campeonato.dto.LoginDto;
import com.fjtm.campeonato.dto.LoginRequest;
import com.fjtm.campeonato.dto.UserDto;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.modelo.User;
import com.fjtm.campeonato.service.JwtService;
import com.fjtm.campeonato.service.RedisService;
import com.fjtm.campeonato.service.UserService;

import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api")
@Tag(name = "Auth", description = "API de autenticación y autorización")
public class AuthController {

    private AuthenticationManager authentication;
    private RedisService redisService;
    private UserService userService;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;

    private AuthController(AuthenticationManager authenticationManager, RedisService redisService, UserService userService, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.authentication = authenticationManager;
        this.redisService = redisService;
        this.userService = userService;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register") // POST /register
    @Operation(summary = "Registrar un nuevo usuario", description = "Permite registrar un nuevo usuario en el sistema")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Usuario registrado correctamente"),
        @ApiResponse(responseCode = "400", description = "Solicitud inválida", content = @Content)
    })
    public ResponseEntity<?> register(@RequestBody UserDto userDto, HttpServletRequest request, HttpServletResponse httpHeaders) {
        return (ResponseEntity<?>) ResponseEntity.ok();
    }

    @PostMapping("/login") // POST /login
    @Operation(summary = "Iniciar sesión", description = "Permite a un usuario iniciar sesión y obtener un token JWT")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Inicio de sesión exitoso", 
            headers = @Header(name = "Authorization", description = "Token JWT de autenticación"),
            content = @Content(schema = @Schema(implementation = LoginDto.class))
        ),
        @ApiResponse(responseCode = "401", description = "Credenciales incorrectas o token no válido", content = @Content)
    })
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpServletResponse httpHeaders) {
        
            String tokenHeader = request.getHeader("Authorization");
            System.out.println("Token recibido en header: " + tokenHeader);
            if(tokenHeader != null && tokenHeader !="") {
            String usuario = jwtService.extractUsername(tokenHeader);
            System.out.println("Usuario extraído del token: " + usuario);
            
            String tokenRedis = jwtService.getTokenFromRedis(usuario);
            System.out.println("Token en Redis: " + tokenRedis);
            
            if (tokenHeader != null && tokenRedis != null && jwtService.validateToken(tokenHeader, usuario)) {
                if (tokenHeader.equals(tokenRedis)) {
                    System.out.println("Token válido, devolviendo respuesta OK");
                    return ResponseEntity.ok("Token igual encontrado en Redis");
                } else {
                    System.out.println("Token no coincide con el de Redis");
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token no coincide con el enviado");
                }
            }
        }
            // AUTENTICACIÓN MANUAL
            String nombre = loginRequest.getUsername();
            System.out.println("Intentando autenticar usuario: " + nombre);
    
            Optional<User> user = userService.findByUsuario(nombre);
            if (user.isEmpty()) {
                System.out.println("Usuario no encontrado en BD");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
            }
    
            User user1 = user.get();
            System.out.println("Usuario encontrado: " + user1.getUsuario());
    
            if (user1.getPassword() != null && passwordEncoder.matches(loginRequest.getPassword(), user1.getPassword())) {
                String jwtToken = jwtService.generateToken(user1);
                System.out.println("Token generado correctamente para usuario: " + user1.getUsuario());
    
                HttpHeaders responseHeaders = new HttpHeaders();
                responseHeaders.set("Authorization", jwtToken);
                
                EspecialidadDto especialidad = new EspecialidadDto(user1.getEspecialidad().getCodigo(), user1.getEspecialidad().getNombre());
                LoginDto loginDto = new LoginDto(user1.getUsuario(), user1.getNombre(), user1.getRol(), jwtToken, especialidad);
    
                return ResponseEntity.ok().headers(responseHeaders).body(loginDto);
            } else {
                System.out.println("Credenciales incorrectas para usuario: " + nombre);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales incorrectas");
            }
    }

    @GetMapping("/logout")
    @Operation(summary = "Cerrar sesión", description = "Invalida el token JWT y cierra la sesión del usuario")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Sesión cerrada correctamente"),
        @ApiResponse(responseCode = "401", description = "Token no válido", content = @Content)
    })
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String token) {
        jwtService.invalidateToken(token);
        return ResponseEntity.ok("Sesión cerrada");
    }

    @GetMapping("/admin/hola")
    @Operation(summary = "Saludo para Admin", description = "Devuelve un saludo si el usuario es administrador")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saludo para administrador"),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    public ResponseEntity<String> adminHola(@RequestHeader("Authorization") String token) {
        
        return ResponseEntity.ok("Hola admin");
    }

    @GetMapping("/admin/experto")
    @Operation(summary = "Saludo para Experto", description = "Devuelve un saludo si el usuario tiene rol de experto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Saludo para experto"),
        @ApiResponse(responseCode = "401", description = "No autorizado", content = @Content)
    })
    public ResponseEntity<String> ExpertoHola(@RequestHeader("Authorization") String token) {
        
        return ResponseEntity.ok("Hola Experto");
    }

}

