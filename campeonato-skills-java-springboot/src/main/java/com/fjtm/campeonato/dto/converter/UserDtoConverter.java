package com.fjtm.campeonato.dto.converter;


import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fjtm.campeonato.dto.EspecialidadDto;
import com.fjtm.campeonato.dto.UserDto;
import com.fjtm.campeonato.modelo.Especialidad;
import com.fjtm.campeonato.modelo.User;
import com.fjtm.campeonato.service.EspecialidadService;
import com.fjtm.campeonato.service.UserService;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UserDtoConverter {

    private final ModelMapper modelMapper;

    @Autowired
    private EspecialidadService especialidadService;
    private UserService userService;

    @PostConstruct
    public void init() {
        modelMapper.addMappings(new PropertyMap<UserDto, User>() {

            @Override
            protected void configure() {

            }
        });
    }

    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto(
                user.getNombre(),
                user.getUsuario(),
                user.getPassword(),
                user.getRol(),
                user.getEspecialidad().getNombre()
        );
        userDto.setPassword(null);
        return userDto;

    }

    public User convertToEntity(UserDto userDto) {
        User user = new User(
                userDto.getNombre(),
                userDto.getUsuario(),
                userDto.getPassword(),
                null,
                especialidadService.findByNombre(userDto.getEspecialidad()).get()
        );
        return user;
    }

}
