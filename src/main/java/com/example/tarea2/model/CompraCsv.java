package com.example.tarea2.model;

// --- Estructura CSV ---
public record CompraCsv(
        Integer id,
        String producto,
        Double monto,
        String unidad,
        String fecha
) {}