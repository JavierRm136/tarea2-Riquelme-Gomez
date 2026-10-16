package com.example.tarea2.service;

import com.example.tarea2.model.CompraCsv;
import com.example.tarea2.model.ComprasUnidadResponse;
import com.example.tarea2.model.ComprasUnidadResponse.ItemCompra;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
public class CompraUnidadService {

    private final CsvReaderService csvReaderService;
    private final MindicadorService mindicadorService;

    public CompraUnidadService(CsvReaderService csvReaderService, MindicadorService mindicadorService) {
        this.csvReaderService = csvReaderService;
        this.mindicadorService = mindicadorService;
    }

    public ComprasUnidadResponse procesarComprasPorUnidad(String unidadParam) {
        validarUnidad(unidadParam);

        String unidadFiltro = normalizarUnidadParaFiltro(unidadParam);
        String unidadRespuesta = normalizarUnidadParaRespuesta(unidadParam);

        List<CompraCsv> todasLasCompras = csvReaderService.obtenerCompras();
        List<ItemCompra> comprasConvertidas = new ArrayList<>();
        long totalPesos = 0;

        for (CompraCsv compra : todasLasCompras) {
            if (compra.unidad() != null && compra.unidad().trim().equalsIgnoreCase(unidadFiltro)) {
                double valorUnidad = mindicadorService.obtenerValorPorFecha(unidadParam, compra.fecha());
                long montoPesos = Math.round(compra.monto() * valorUnidad);
                totalPesos += montoPesos;

                comprasConvertidas.add(new ItemCompra(
                        compra.id(),
                        compra.producto(),
                        compra.monto(),
                        compra.fecha(),
                        valorUnidad,
                        montoPesos
                ));
            }
        }

        return new ComprasUnidadResponse(unidadRespuesta, comprasConvertidas, totalPesos);
    }

    private void validarUnidad(String unidad) {
        if (unidad == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unidad no especificada");
        }
        String u = unidad.trim().toLowerCase();
        if (!"uf".equals(u) && !"dolar".equals(u)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unidad invalida");
        }
    }

    private String normalizarUnidadParaFiltro(String unidad) {
        return "uf".equalsIgnoreCase(unidad.trim()) ? "UF" : "USD";
    }

    private String normalizarUnidadParaRespuesta(String unidad) {
        return "uf".equalsIgnoreCase(unidad.trim()) ? "UF" : "USD";
    }
}