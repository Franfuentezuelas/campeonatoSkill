package com.fjtm.campeonato.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import com.fjtm.campeonato.modelo.User;

import java.security.Key;
import java.util.Date;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class JwtService {

    private static final String TOKEN_PREFIX = "jwt_token:";  // Prefijo para los tokens en Redis

    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private static final String SECRET_KEY = "me-gusta-como-caza-la-perra-me-gusta-como-caza-la-perra-me-gusta-como-caza-la-perra"; // Debe ser de al menos 256 bits

    private Key getSigningKey() {
        // Decodificar la clave en Base64 URL-safe
        byte[] keyBytes = Decoders.BASE64URL.decode(SECRET_KEY); // Usa BASE64URL para garantizar URL-safe
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(User user) {
        // eliminar el token de Redis si existe
        String tokenRedis = getTokenFromRedis(user.getUsuario());
        if (tokenRedis != null) {
            redisTemplate.delete(TOKEN_PREFIX + user.getUsuario());
        }
        // Generar el token JWT
        String token = Jwts.builder()
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .setHeaderParam("typ", "JWT")
                .setSubject(user.getNombre())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .claim("username", user.getUsuario())
                .claim("roles", List.of(user.getRol())) // Incluir roles en formato lista
                .claim("especialidad", user.getEspecialidad().getNombre())
                // aqui puedo meter todos los claims que quieras con los datos que necesite en el token
                .compact();

        // Guardar el token en Redis con una clave única para cada usuario
       saveTokenInRedis(user.getUsuario(), token);
        return token;
    }

    public void saveTokenInRedis(String username, String token) {
        String redisKey = TOKEN_PREFIX + username;
        redisTemplate.opsForValue().set(redisKey, token);
    }

    public String getTokenFromRedis(String username) {
        String redisKey = TOKEN_PREFIX + username;
        return redisTemplate.opsForValue().get(redisKey);  // Recupera el token del Redis
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }
    
    public String extractEspecialidad(String token) {
        Claims claims = Jwts.parser()
                            .setSigningKey(getSigningKey())
                            .build()
                            .parseClaimsJws(token)
                            .getBody();
        return claims.get("especialidad", String.class);  // Extrae la especialidad como String
    }

    
    public boolean validateToken(String token, String username) {
        return username.equals(extractUsername(token)) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractClaim(token, Claims::getExpiration).before(new Date());
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }

    public List<GrantedAuthority> extractRoles(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();

        List<String> roles = claims.get("roles", List.class);
        return roles.stream()
                    .map(SimpleGrantedAuthority::new)
                    .collect(Collectors.toList());
    }

    public boolean isTokenValid(String token) {
        String redisToken = getTokenFromRedis(this.extractUsername(token));
        if (redisToken != null) {
            return token.equals(redisToken);
        }
        return false;
    }

    public void invalidateToken(String token) {
        String tokenRedis = getTokenFromRedis(this.extractUsername(token));
        if (tokenRedis != null) {
            redisTemplate.delete(TOKEN_PREFIX + this.extractUsername(token));
        }  // Eliminar el token por su valor, no solo por el usuario
    }

}

