package com.fjtm.campeonato.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.modelo.User;
import com.fjtm.campeonato.repository.UserRepository;
import com.fjtm.campeonato.service.base.BaseService;

@Service
public class UserService extends BaseService<User, Long, UserRepository>{
 
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public boolean autenticarUsuario(String username, String password) {
        User user = userRepository.findByUsuario(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));
        return passwordEncoder.matches(password, user.getPassword());
    }

    public User guardarUsuario(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Optional<User> findByUsuario(String username) {
        return userRepository.findByUsuario(username);
    }

    public boolean existeUsuario(String username) {
        return userRepository.existsByUsuario(username);
    }

    public List<User> saveAllUsers(List<User> users) {
    // no se tiene que implementar en el repositorio ya que jpa lo implementa directamente(saveAllAndFlush)
       return userRepository.saveAllAndFlush(users);
    }

    public List<User> findAllByEspecialidad(Especialidad especialidad) { 
        return userRepository.findAllByEspecialidad(especialidad);
    }

}


    