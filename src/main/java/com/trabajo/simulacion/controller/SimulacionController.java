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
// Anotación para definir esta clase como un controlador en Spring
@Controller
public class SimulacionController {

    // Inyección de dependencia para usar los métodos de SimulacionService
    @Autowired
    private SimulacionService simulacionService;

    // Manejo de la ruta raíz ("") con el método HTTP GET
    @GetMapping("")
    public String inicio() {
        // Devuelve la vista "index" (página principal)
        return "index";
    }

    // Manejo de la ruta "/ejecutar" con el método HTTP POST
    @PostMapping("/ejecutar")
    public String ejecutarSimulacion(
            // Recibe el nombre del algoritmo seleccionado como parámetro de solicitud
            @RequestParam("algoritmo") String algoritmo,
            // Recibe la semilla como cadena (será convertida más adelante a BigInteger)
            @RequestParam("semilla") String semillaStr,
            // Recibe el número de iteraciones
            @RequestParam("iteraciones") int iteraciones,
            // Recibe parámetros específicos para el algoritmo "Congruencial Cuadrático"
            @RequestParam(value = "a", required = false, defaultValue = "0") String aStr,
            @RequestParam(value = "b", required = false, defaultValue = "0") String bStr,
            @RequestParam(value = "c", required = false, defaultValue = "0") String cStr,
            @RequestParam(value = "modulo", required = false, defaultValue = "10000") String moduloStr,
            // Recibe parámetros específicos para el algoritmo "Blum Blum Shub"
            @RequestParam(value = "p", required = false, defaultValue = "11") String pStr,
            @RequestParam(value = "q", required = false, defaultValue = "19") String qStr,
            // Modelo para agregar atributos que serán accesibles en la vista
            Model modelo
    ) {
        try {
            // Conversión de los parámetros recibidos a BigInteger
            BigInteger semilla = new BigInteger(semillaStr);
            List<BigInteger> resultados;

            // Selección del algoritmo según el parámetro "algoritmo"
            switch (algoritmo) {
                case "cuadradosMedios":
                    // Llama al servicio para ejecutar el algoritmo de Cuadrados Medios
                    resultados = simulacionService.cuadradosMedios(semilla, iteraciones);
                    break;
                case "congruencialCuadratico":
                    // Convierte los parámetros específicos del algoritmo a BigInteger
                    BigInteger a = new BigInteger(aStr);
                    BigInteger b = new BigInteger(bStr);
                    BigInteger c = new BigInteger(cStr);
                    BigInteger modulo = new BigInteger(moduloStr);
                    // Llama al servicio para ejecutar el algoritmo Congruencial Cuadrático
                    resultados = simulacionService.congruencialCuadratico(a, b, c, semilla, iteraciones, modulo);
                    break;
                case "blumBlumShub":
                    // Convierte los parámetros específicos del algoritmo a BigInteger
                    BigInteger p = new BigInteger(pStr);
                    BigInteger q = new BigInteger(qStr);

                    // Validación: p y q deben ser primos
                    if (!p.isProbablePrime(100) || !q.isProbablePrime(100)) {
                        throw new IllegalArgumentException("p y q deben ser números primos.");
                    }

                    // Validación: p y q deben ser congruentes a 3 mod 4
                    if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) ||
                            !q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
                        throw new IllegalArgumentException("p y q deben ser congruentes a 3 mod 4.");
                    }

                    // Llama al servicio para ejecutar el algoritmo Blum Blum Shub
                    resultados = simulacionService.blumBlumShub(semilla, iteraciones, p, q);
                    break;
                default:
                    // Lanza una excepción si el algoritmo no es reconocido
                    throw new IllegalArgumentException("Algoritmo no reconocido: " + algoritmo);
            }

            // Agrega los resultados y el nombre del algoritmo al modelo para mostrarlos en la vista
            modelo.addAttribute("resultados", resultados);
            modelo.addAttribute("algoritmo", algoritmo);
            // Devuelve la vista "resultados" con los datos generados
            return "resultados";

        } catch (Exception e) {
            // Captura cualquier excepción y agrega un mensaje de error al modelo
            modelo.addAttribute("error", "Error al ejecutar la simulación: " + e.getMessage());
            // Redirige a la vista "error"
            return "error";
        }
    }

    // Manejo de la ruta "/exportar" con el método HTTP GET
    @GetMapping("/exportar")
    public String exportarExcel(Model modelo) throws IOException {
        try {
            // Genera un archivo Excel temporal utilizando el servicio
            File archivoExcel = simulacionService.generarExcelTemporal();
            // Agrega el nombre del archivo al modelo para que pueda ser descargado
            modelo.addAttribute("archivoExcel", archivoExcel.getName());
            // Devuelve la vista "descargar"
            return "descargar";
        } catch (Exception e) {
            // Captura cualquier excepción durante la generación del archivo Excel
            modelo.addAttribute("error", "Error al exportar a Excel: " + e.getMessage());
            // Redirige a la vista "error"
            return "error";
        }
    }
}

