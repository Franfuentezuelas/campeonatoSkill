package com.fjtm.campeonato.util.validacion;

import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fjtm.campeonato.modelo.User;
import com.fjtm.campeonato.repository.UserRepository;
import com.fjtm.campeonato.service.JwtService;
import com.fjtm.campeonato.service.RedisService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();

        // Excluir solo las rutas públicas del filtro JWT (pero no todo "/api/")
        if (requestURI.equals("/competidor/all") 
        || requestURI.equals("/api/login") 
        || requestURI.equals("/api/register")
        //|| requestURI.equals("/swagger-ui.html")
        || requestURI.startsWith("/v3/api-docs")
        || requestURI.startsWith("/swagger-ui")) {
            filterChain.doFilter(request, response);
            return;
        }        

        // Obtener el token JWT de la solicitud (cabecera Authorization)
        String jwt = getJwtFromRequest(request);

        // 🚨 Si el token no está en Redis, rechazar la solicitud con 401
        if (jwt != null && !jwtService.isTokenValid(jwt)) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token no autorizado o inválido");
            response.getWriter().flush();
            return;
        }

        // Validar el token y autenticar al usuario
        if (jwtService.validateToken(jwt, jwtService.extractUsername(jwt))) {
            String username = jwtService.extractUsername(jwt);

            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

        


    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null) {
            return bearerToken;  // Extrae el token JWT de la cabecera Authorization
        }
        return null;
    }

    private boolean isPublicRoute(String requestURI) {
        // Excluir rutas públicas, como Swagger, login, y registro
        return requestURI.equals("/api/login") 
                || requestURI.equals("/api/register")
                || requestURI.equals("/swagger-ui.html")
                || requestURI.equals("/api-docs")
                || requestURI.startsWith("/swagger-ui/")
                || requestURI.startsWith("/api-docs/");
    }
}

    