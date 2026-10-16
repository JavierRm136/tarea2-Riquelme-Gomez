package com.example.tarea2.model;

// --- Respuesta de Compra ---
public record CompraResponse(
        Integer id,
        String producto,
        Double monto,
        String unidad,
        String fecha,
        Double valor_unidad,
        Integer monto_pesos
) {}