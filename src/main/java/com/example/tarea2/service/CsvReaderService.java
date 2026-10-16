package com.example.tarea2.service;

import com.example.tarea2.model.CompraCsv;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

@Service
public class CsvReaderService {

    // --- Configuracion ---
    @Value("${app.csv.path}")
    private String csvPath;

    // --- Operaciones de lectura ---
    public List<CompraCsv> leerCompras() {
        List<CompraCsv> compras = new ArrayList<>();
        Path path = Paths.get(csvPath);

        try {
            List<String> lineas = Files.readAllLines(path);

            for (int i = 1; i < lineas.size(); i++) {
                String linea = lineas.get(i);
                if (linea == null || linea.trim().isEmpty()) {
                    continue;
                }

                String[] datos = linea.split(",");
                if (datos.length >= 5) {
                    compras.add(new CompraCsv(
                            Integer.parseInt(datos[0].trim()),
                            datos[1].trim(),
                            Double.parseDouble(datos[2].trim()),
                            datos[3].trim(),
                            datos[4].trim()
                    ));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error procesando el archivo CSV", e);
        }

        return compras;
    }
}