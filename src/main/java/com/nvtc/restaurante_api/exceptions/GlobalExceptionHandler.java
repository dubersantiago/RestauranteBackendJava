package com.nvtc.restaurante_api.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ItemAgotadoException.class)
    public ResponseEntity<Map<String, String>> handleItemAgotado(ItemAgotadoException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT) // Retorna 409 en lugar de 500
            .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntime(RuntimeException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST) // 400 para otros errores genéricos
            .body(Map.of("error", ex.getMessage()));
    }
}