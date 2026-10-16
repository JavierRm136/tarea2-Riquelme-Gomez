package com.example.tarea2.exception;

// --- Excepcion de recurso no encontrado ---
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String message) {
        super(message);
    }
}