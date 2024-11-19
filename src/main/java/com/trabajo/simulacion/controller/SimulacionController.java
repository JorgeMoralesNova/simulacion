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
import java.math.BigInteger;
import java.util.List;

@Controller
public class SimulacionController {

    @Autowired
    private SimulacionService simulacionService;

    @GetMapping("")
    public String inicio() {
        return "index";
    }

    @PostMapping("/ejecutar")
    public String ejecutarSimulacion(
            @RequestParam("algoritmo") String algoritmo,
            @RequestParam("semilla") String semillaStr,
            @RequestParam("iteraciones") int iteraciones,
            @RequestParam(value = "a", required = false, defaultValue = "0") String aStr,
            @RequestParam(value = "b", required = false, defaultValue = "0") String bStr,
            @RequestParam(value = "c", required = false, defaultValue = "0") String cStr,
            @RequestParam(value = "modulo", required = false, defaultValue = "10000") String moduloStr,
            @RequestParam(value = "p", required = false, defaultValue = "11") String pStr,
            @RequestParam(value = "q", required = false, defaultValue = "19") String qStr,
            Model modelo
    ) {
        try {
            // Convertir los parámetros de cadena a BigInteger
            BigInteger semilla = new BigInteger(semillaStr);
            List<BigInteger> resultados;

            // Selección del algoritmo
            switch (algoritmo) {
                case "cuadradosMedios":
                    resultados = simulacionService.cuadradosMedios(semilla, iteraciones);
                    break;
                case "congruencialCuadratico":
                    BigInteger a = new BigInteger(aStr);
                    BigInteger b = new BigInteger(bStr);
                    BigInteger c = new BigInteger(cStr);
                    BigInteger modulo = new BigInteger(moduloStr);
                    resultados = simulacionService.congruencialCuadratico(a, b, c, semilla, iteraciones, modulo);
                    break;
                case "blumBlumShub":
                    BigInteger p = new BigInteger(pStr);
                    BigInteger q = new BigInteger(qStr);

                    // Validaciones específicas para Blum Blum Shub
                    if (!p.isProbablePrime(100) || !q.isProbablePrime(100)) {
                        throw new IllegalArgumentException("p y q deben ser números primos.");
                    }
                    if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) ||
                            !q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                        throw new IllegalArgumentException("p y q deben ser congruentes a 3 mod 4.");
                    }

                    resultados = simulacionService.blumBlumShub(semilla, iteraciones, p, q);
                    break;
                default:
                    throw new IllegalArgumentException("Algoritmo no reconocido: " + algoritmo);
            }

            // Agregar resultados al modelo
            modelo.addAttribute("resultados", resultados);
            modelo.addAttribute("algoritmo", algoritmo);
            return "resultados";

        } catch (Exception e) {
            // Manejo del error
            modelo.addAttribute("error", "Error al ejecutar la simulación: " + e.getMessage());
            return "error"; // Redirige a la vista de error
        }
    }

    @GetMapping("/exportar")
    public String exportarExcel(Model modelo) throws IOException {
        try {
            // Genera el archivo Excel temporal
            File archivoExcel = simulacionService.generarExcelTemporal();
            modelo.addAttribute("archivoExcel", archivoExcel.getName());
            return "descargar";
        } catch (Exception e) {
            // Manejo del error en la exportación
            modelo.addAttribute("error", "Error al exportar a Excel: " + e.getMessage());
            return "error";
        }
    }
}
