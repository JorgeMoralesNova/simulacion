package com.trabajo.simulacion.controller;

import com.trabajo.simulacion.service.SimulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Controller
public class SimulacionController {

    @Autowired
    private SimulacionService simulacionService;

    @GetMapping("/")
    public String inicio() {
        return "index";
    }

    @PostMapping("/ejecutar")
    public String ejecutarSimulacion(
            @RequestParam("algoritmo") String algoritmo,
            @RequestParam("semilla") int semilla,
            @RequestParam("iteraciones") int iteraciones,
            @RequestParam(value = "a", required = false, defaultValue = "0") int a,
            @RequestParam(value = "b", required = false, defaultValue = "0") int b,
            @RequestParam(value = "c", required = false, defaultValue = "0") int c,
            Model modelo
    ) {
        List<Integer> resultados;
        switch (algoritmo) {
            case "cuadradosMedios":
                resultados = simulacionService.cuadradosMedios(semilla, iteraciones);
                break;
            case "congruencialCuadratico":
                resultados = simulacionService.congruencialCuadratico(a, b, c, semilla, iteraciones);
                break;
            case "blumBlumShub":
                resultados = simulacionService.blumBlumShub(semilla, iteraciones);
                break;
            default:
                resultados = new ArrayList<>();
        }
        modelo.addAttribute("resultados", resultados);
        return "resultados";
    }

    @GetMapping("/exportar")
    public String exportarExcel(Model modelo) throws IOException {
        // Genera el archivo Excel temporal
        File archivoExcel = simulacionService.generarExcelTemporal();
        modelo.addAttribute("archivoExcel", archivoExcel.getName());
        return "descargar";
    }
}
