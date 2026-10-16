package com.example.tarea2.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class MindicadorService {

    private static final String BASE_URL = "https://mindicador.cl/api";
    private static final DateTimeFormatter INPUT_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter API_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private final RestClient restClient;

    public MindicadorService() {
        this.restClient = RestClient.builder()
                .baseUrl(BASE_URL)
                .build();
    }

    public double obtenerValorPorFecha(String unidad, String fechaYmd) {
        String serieNombre = resolverIndicador(unidad);
        String fechaFormateada = formatearFecha(fechaYmd);

        try {
            IndicadorResponse response = restClient.get()
                    .uri("/{indicador}/{fecha}", serieNombre, fechaFormateada)
                    .retrieve()
                    .body(IndicadorResponse.class);

            if (response != null && response.serie() != null && !response.serie().isEmpty()) {
                return response.serie().getFirst().valor();
            }
        } catch (Exception e) {
            return 0.0;
        }

        return 0.0;
    }

    private String resolverIndicador(String unidad) {
        String u = unidad.trim().toLowerCase();
        if ("dolar".equals(u) || "usd".equals(u)) {
            return "dolar";
        }
        return "uf";
    }

    private String formatearFecha(String fechaYmd) {
        LocalDate date = LocalDate.parse(fechaYmd.trim(), INPUT_FORMAT);
        return date.format(API_FORMAT);
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record IndicadorResponse(
        @JsonProperty("serie") List<SerieItem> serie
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    private record SerieItem(
        @JsonProperty("fecha") String fecha,
        @JsonProperty("valor") double valor
    ) {}
}