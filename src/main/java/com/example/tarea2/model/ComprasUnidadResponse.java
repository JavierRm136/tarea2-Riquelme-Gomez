package com.example.tarea2.model;

import java.util.List;

public record ComprasUnidadResponse(
    String unidad,
    List<ItemCompra> compras,
    long total_pesos
) {

    public record ItemCompra(
        int id,
        String producto,
        double monto,
        String fecha,
        double valor_unidad,
        long monto_pesos
    ) {}
}