package com.example.tarea2.service;

import com.example.tarea2.exception.ResourceNotFoundException;
import com.example.tarea2.model.CompraCsv;
import com.example.tarea2.model.CompraResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class ComprasDetalleService {

    // --- Dependencias ---
    private final CsvReaderService csvReaderService;
    private final RestTemplate restTemplate;

    // --- Constructor ---
    public ComprasDetalleService(CsvReaderService csvReaderService) {
        this.csvReaderService = csvReaderService;
        this.restTemplate = new RestTemplate();
    }

    // --- Lógica de negocio ---
    public CompraResponse obtenerDetalleCompra(Integer id) {
        CompraCsv compra = csvReaderService.leerCompras().stream()
                .filter(c -> c.id().equals(id))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("Compra no encontrada con id: " + id));

        double valorUnidad = obtenerValorMoneda(compra.unidad(), compra.fecha());
        int montoPesos = (int) Math.round(compra.monto() * valorUnidad);

        return new CompraResponse(
                compra.id(),
                compra.producto(),
                compra.monto(),
                compra.unidad(),
                compra.fecha(),
                valorUnidad,
                montoPesos
        );
    }

    // --- Integración API externa ---
    private double obtenerValorMoneda(String unidad, String fecha) {
        String unidadApi = unidad.equalsIgnoreCase("USD") ? "dolar" : "uf";

        LocalDate date = LocalDate.parse(fecha, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        String fechaApi = date.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));

        String url = "https://mindicador.cl/api/" + unidadApi + "/" + fechaApi;

        try {
            @SuppressWarnings("unchecked")
            Map<String, Object> response = restTemplate.getForObject(url, Map.class);

            if (response != null && response.containsKey("serie")) {
                @SuppressWarnings("unchecked")
                List<Map<String, Object>> serie = (List<Map<String, Object>>) response.get("serie");
                if (!serie.isEmpty()) {
                    return Double.parseDouble(serie.get(0).get("valor").toString());
                }
            }
            return 0.0;
        } catch (Exception e) {
            return 0.0;
        }
    }
}