package com.example.tarea2.controller;

import com.example.tarea2.model.ComprasUnidadResponse;
import com.example.tarea2.service.CompraUnidadService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compras")
public class CompraUnidadController {

    private final CompraUnidadService compraUnidadService;

    public CompraUnidadController(CompraUnidadService compraUnidadService) {
        this.compraUnidadService = compraUnidadService;
    }

    @GetMapping("/{unidad}")
    public ResponseEntity<ComprasUnidadResponse> obtenerComprasPorUnidad(@PathVariable String unidad) {
        ComprasUnidadResponse response = compraUnidadService.procesarComprasPorUnidad(unidad);
        return ResponseEntity.ok(response);
    }
}