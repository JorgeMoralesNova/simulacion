package com.trabajo.simulacion.service;

import com.trabajo.simulacion.model.Simulacion;
import com.trabajo.simulacion.repository.SimulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;

@Service
public class SimulacionService {

    @Autowired
    private SimulacionRepository simulacionRepository;



    public List<BigInteger> cuadradosMedios(BigInteger semilla, int iteraciones) {
        if (semilla.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("La semilla debe ser un número positivo.");
        }

        int numDigitos = semilla.toString().length(); // Número de dígitos de la semilla

        List<BigInteger> resultados = new ArrayList<>();
        Set<BigInteger> vistos = new HashSet<>(); // Para detectar ciclos repetitivos
        BigInteger actual = semilla;

        for (int i = 0; i < iteraciones; i++) {
            // Elevar al cuadrado
            BigInteger cuadrado = actual.multiply(actual);

            // Calcular el número total de dígitos esperado en el cuadrado
            int totalDigitos = numDigitos * 2;

            // Convertir el cuadrado en una cadena y asegurar que tenga el número total de dígitos
            String cuadradoStr = String.format("%0" + totalDigitos + "d", cuadrado);

            // Calcular el índice de inicio para extraer los dígitos centrales
            int inicio = (cuadradoStr.length() - numDigitos) / 2;

            // Extraer los dígitos centrales
            String central = cuadradoStr.substring(inicio, inicio + numDigitos);

            // Convertir de nuevo a BigInteger y actualizar el valor actual
            actual = new BigInteger(central);

            // Comprobar si ya hemos visto este número
            if (vistos.contains(actual)) {
                System.out.println("Ciclo detectado en la iteración " + i + ": " + actual);
                break;
            }

            // Añadir a las listas de resultados y vistos
            resultados.add(actual);
            vistos.add(actual);
        }

        guardarSimulacion("Cuadrados Medios Generalizados", semilla, iteraciones, resultados.toString());
        return resultados;
    }





    public List<BigInteger> congruencialCuadratico(BigInteger a, BigInteger b, BigInteger c, BigInteger semilla, int iteraciones, BigInteger modulo) {
        // Validaciones iniciales
        if (a == null || b == null || c == null || semilla == null || modulo == null) {
            throw new IllegalArgumentException("Ninguno de los parámetros puede ser nulo.");
        }
        if (modulo.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("El módulo debe ser un número positivo.");
        }
        if (iteraciones <= 0) {
            throw new IllegalArgumentException("El número de iteraciones debe ser mayor a cero.");
        }

        List<BigInteger> resultados = new ArrayList<>();
        Set<BigInteger> vistos = new HashSet<>(); // Para detectar ciclos
        BigInteger actual = semilla;

        for (int i = 0; i < iteraciones; i++) {
            // Fórmula congruencial cuadrática
            actual = (a.multiply(actual).multiply(actual).add(b.multiply(actual)).add(c)).mod(modulo);

            // Registro detallado de cada iteración
            System.out.println("Iteración " + i + ": Resultado=" + actual);

            // Detectar ciclos
            if (vistos.contains(actual)) {
                System.out.println("Ciclo detectado en la iteración " + i + ": Número repetido: " + actual);
                break;
            }

            resultados.add(actual);
            vistos.add(actual);
        }

        // Manejo de errores en guardarSimulacion
        try {
            guardarSimulacion("Congruencial Cuadrático", semilla, iteraciones, resultados.toString());
        } catch (Exception e) {
            System.err.println("Error al guardar la simulación: " + e.getMessage());
        }

        return resultados;
    }

    // Algoritmo Blum Blum Shub
    // Algoritmo Blum Blum Shub mejorado
    public List<BigInteger> blumBlumShub(BigInteger semilla, int iteraciones, BigInteger p, BigInteger q) {
        // Validaciones iniciales
        if (p == null || q == null || semilla == null) {
            throw new IllegalArgumentException("Ninguno de los parámetros puede ser nulo.");
        }
        if (!p.isProbablePrime(100) || !q.isProbablePrime(100)) {
            throw new IllegalArgumentException("Los valores de p y q deben ser primos.");
        }
        if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) ||
                !q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            throw new IllegalArgumentException("Los valores de p y q deben ser congruentes a 3 mod 4.");
        }
        if (semilla.compareTo(BigInteger.ZERO) <= 0 || semilla.compareTo(p.multiply(q)) >= 0) {
            throw new IllegalArgumentException("La semilla debe estar en el rango (0, p*q).");
        }
        if (iteraciones <= 0) {
            throw new IllegalArgumentException("El número de iteraciones debe ser mayor a cero.");
        }

        // Inicialización del módulo n
        BigInteger n = p.multiply(q);

        // Variables para el cálculo
        List<BigInteger> resultados = new ArrayList<>();
        Set<BigInteger> vistos = new HashSet<>(); // Para detectar ciclos
        BigInteger actual = semilla;

        for (int i = 0; i < iteraciones; i++) {
            // Fórmula Blum Blum Shub
            actual = actual.multiply(actual).mod(n);

            // Extraer el bit menos significativo como resultado
            BigInteger bit = actual.mod(BigInteger.TWO);
            resultados.add(bit);

            // Log detallado de cada iteración
            System.out.println("Iteración " + i + ": Número generado=" + actual + ", Bit=" + bit);

            // Detectar ciclos
            if (vistos.contains(actual)) {
                System.out.println("Ciclo detectado en la iteración " + i + ": Número repetido: " + actual);
                break;
            }

            vistos.add(actual);
        }

        // Manejo de errores en guardarSimulacion
        try {
            guardarSimulacion("Blum Blum Shub", semilla, iteraciones, resultados.toString());
        } catch (Exception e) {
            System.err.println("Error al guardar la simulación: " + e.getMessage());
        }

        return resultados;
    }

    // Guardar simulación en la base de datos
    private void guardarSimulacion(String algoritmo, BigInteger semilla, int iteraciones, String resultados) {
        Simulacion simulacion = new Simulacion(algoritmo, semilla, iteraciones, resultados);
        simulacionRepository.save(simulacion);
    }
    public List<Simulacion> obtenerTodasSimulaciones() {
        return simulacionRepository.findAll();
    }






    public File generarExcelTemporal() throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet hoja = workbook.createSheet("Simulaciones");

        // Cabecera
        Row cabecera = hoja.createRow(0);
        cabecera.createCell(0).setCellValue("ID");
        cabecera.createCell(1).setCellValue("Algoritmo");
        cabecera.createCell(2).setCellValue("Semilla");
        cabecera.createCell(3).setCellValue("Iteraciones");
        cabecera.createCell(4).setCellValue("Resultados");

        // Datos
        List<Simulacion> simulaciones = obtenerTodasSimulaciones();
        int numFila = 1;
        for (Simulacion sim : simulaciones) {
            Row fila = hoja.createRow(numFila++);
            fila.createCell(0).setCellValue(sim.getId());
            fila.createCell(1).setCellValue(sim.getAlgoritmo());
            fila.createCell(2).setCellValue(sim.getSemilla().intValue());
            fila.createCell(3).setCellValue(sim.getIteraciones());
            fila.createCell(4).setCellValue(sim.getResultados());
        }

        // Crear archivo temporal
        File archivoTemporal = File.createTempFile("simulaciones_", ".xlsx");
        try (FileOutputStream fos = new FileOutputStream(archivoTemporal)) {
            workbook.write(fos);
        }
        workbook.close();
        return archivoTemporal;
    }
}
