package com.fjtm.campeonato.error;

import java.time.LocalDateTime;

import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.HttpStatusCode;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiError {
    
    public ApiError(HttpStatusCode status2, String message) {
        //TODO Auto-generated constructor stub
    }
    private String error;
    @Builder.Default
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm:ss")
    private LocalDateTime timestamp= LocalDateTime.now();
    private String status;

}