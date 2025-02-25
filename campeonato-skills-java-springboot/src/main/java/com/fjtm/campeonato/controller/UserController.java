package com.fjtm.campeonato.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fjtm.campeonato.dto.EspecialidadDto;
import com.fjtm.campeonato.dto.UserDto;
import com.fjtm.campeonato.dto.converter.UserDtoConverter;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.modelo.User;
import com.fjtm.campeonato.service.EspecialidadService;
import com.fjtm.campeonato.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/experto/admin")
@RequiredArgsConstructor
public class UserController {

    private final UserService serviceUser;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;
    private final UserDtoConverter userDtoConverter;
    private final EspecialidadService serviceEspecialidad;
    
    @PostMapping("/registerall")
    public ResponseEntity<List<User>> registerAllUser(@RequestHeader("Authorization") String token,
     @RequestBody List<UserDto> usersDto) { 
        List<User> users = usersDto.stream().map(userDto -> {
            User user = userDtoConverter.convertToEntity(userDto);
            //user.setEspecialidad(serviceEspecialidad.findByNombre(userDto.getEspecialidad()).get());
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setId(null); // Asegurar que el ID sea nulo para evitar conflictos en la BD
            return user;
            }).toList();
        // Guardar los usuarios en la base de datos
        List<User> savedUsers= serviceUser.saveAllUsers(users);

        return ResponseEntity.ok(users);
    }

    @PostMapping("/register")
    public ResponseEntity<User> registerUser(@RequestHeader("Authorization") String token, @RequestBody UserDto userDto) {
        
        User user = userDtoConverter.convertToEntity(userDto);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setId(null); // Asegurar que el ID sea nulo para evitar conflictos en la BD
        User savedUser = serviceUser.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @GetMapping("/all")
    public ResponseEntity<List<User>> getAll() {
        // obtengo toda la lista de especialidades, y filtro por codigo no ADM para que no este administrador
        List<User> usuarios = serviceUser.findAll().stream()
                                        .filter(a -> !a.getRol().equals("ROLE_ADMIN")) // Filtrar los usuarios que no sean ADMIN
                                        .map(user -> { 
                                            user.setPassword(null); // Establecer la contraseña como null
                                            return user;
                                        })
                                        .toList();
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/save")
    public ResponseEntity<User> saveUser(@RequestHeader("Authorization") String token, @RequestBody UserDto userDto) {
        
        User user = userDtoConverter.convertToEntity(userDto);
   
        user.setPassword(passwordEncoder.encode(user.getPassword()));
      
        user.setId(null); // Asegurar que el ID sea nulo para evitar conflictos en la BD
        User savedUser = serviceUser.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestHeader("Authorization") String token, @RequestBody UserDto userDto) {
        
        if(userDto.getPassword()==null){
            String password = serviceUser.findByUsuario(userDto.getUsuario()).get().getPassword();
            userDto.setPassword(password);
        }

        User user = userDtoConverter.convertToEntity(userDto);
        if(userDto.getPassword()!=null){
            user.setPassword(passwordEncoder.encode(userDto.getPassword()));
        }
        user.setId(serviceUser.findByUsuario(user.getUsuario()).get().getId());
        serviceUser.edit(user);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/delete")
    public ResponseEntity<?> delete(@RequestBody UserDto userDto) {
        Optional<User> optionalUser = serviceUser.findByUsuario(userDto.getUsuario());
        
        if (optionalUser.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "No existe un experto con estos datos"));
        }
        
        serviceUser.deleteById(optionalUser.get().getId());
        
        return ResponseEntity.ok(Map.of("success", true, "message", "Experto eliminado correctamente"));
    }

}
