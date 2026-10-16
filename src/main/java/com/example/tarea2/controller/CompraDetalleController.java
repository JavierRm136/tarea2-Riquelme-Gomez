package com.example.tarea2.controller;

import com.example.tarea2.model.CompraResponse;
import com.example.tarea2.service.ComprasDetalleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/compras")
public class CompraDetalleController {

    // --- Dependencias ---
    private final ComprasDetalleService compraDetalleService;

    // --- Constructor ---
    public CompraDetalleController(ComprasDetalleService compraDetalleService) {
        this.compraDetalleService = compraDetalleService;
    }

    // --- Endpoints ---
    @GetMapping("/detalle/{id}")
    public ResponseEntity<CompraResponse> obtenerDetalle(@PathVariable Integer id) {
        CompraResponse response = compraDetalleService.obtenerDetalleCompra(id);
        return ResponseEntity.ok(response);
    }
}