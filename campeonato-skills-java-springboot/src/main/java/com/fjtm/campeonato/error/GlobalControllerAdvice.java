package com.fjtm.campeonato.error;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import com.fjtm.campeonato.error.ApiError;

@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {
	
	@ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Map<String, Object>> handleNotFoundException(NotFoundException ex, WebRequest request) {
        Map<String, Object> errorResponse = new HashMap<>();
        errorResponse.put("timestamp", LocalDateTime.now());
        errorResponse.put("status", HttpStatus.NOT_FOUND.value());
        errorResponse.put("error", ex.getInfo());
        errorResponse.put("message", ex.getMessage());
        errorResponse.put("path", ex.getRuta()); // Opcionalmente dinámico si necesitas más precisión
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    // @ExceptionHandler(MethodArgumentNotValidException.class)
    // public ResponseEntity<Map<String, Object>> handleValidationExceptions(MethodArgumentNotValidException ex) {
    //     Map<String, Object> errors = new HashMap<>();
    //     ex.getBindingResult().getFieldErrors().forEach(error -> 
    //         errors.put(error.getField(), error.getDefaultMessage())
    //     );
    //     errors.put("error", "Error de validación");
    //     errors.put("timestamp", LocalDateTime.now());
    //     errors.put("status", HttpStatus.BAD_REQUEST.value());
    //     return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    // }

    @Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, Object body, HttpHeaders headers,
			HttpStatusCode status, WebRequest request) {
		ApiError  apiError = new ApiError(status, ex.getMessage());
		return ResponseEntity.status(status).headers(headers).body(apiError);
	}

}
