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

    /**
     * Genera números pseudoaleatorios utilizando el método de Cuadrados Medios.
     *
     * @param semilla Semilla inicial para el generador.
     * @param iteraciones Número máximo de iteraciones.
     * @return Lista de números generados.
     */


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





    // Algoritmo Congruencial Cuadrático
    public List<BigInteger> congruencialCuadratico(BigInteger a, BigInteger b, BigInteger c, BigInteger semilla, int iteraciones) {
        List<BigInteger> resultados = new ArrayList<>();
        BigInteger actual = semilla;
        BigInteger mod = BigInteger.valueOf(10000); // Modulo arbitrario
        for (int i = 0; i < iteraciones; i++) {
            actual = (a.multiply(actual).multiply(actual).add(b.multiply(actual)).add(c)).mod(mod);
            resultados.add(actual);
        }
        guardarSimulacion("Congruencial Cuadrático", semilla, iteraciones, resultados.toString());
        return resultados;
    }

    // Algoritmo Blum Blum Shub
    public List<BigInteger> blumBlumShub(BigInteger semilla, int iteraciones) {
        List<BigInteger> resultados = new ArrayList<>();
        BigInteger p = BigInteger.valueOf(11), q = BigInteger.valueOf(19); // Primos grandes
        BigInteger n = p.multiply(q);
        BigInteger actual = semilla;
        for (int i = 0; i < iteraciones; i++) {
            actual = actual.multiply(actual).mod(n);
            resultados.add(actual.mod(BigInteger.TWO));
        }
        guardarSimulacion("Blum Blum Shub", semilla, iteraciones, resultados.toString());
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


    public void exportarExcel(OutputStream outputStream) throws IOException {
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

        workbook.write(outputStream);
        workbook.close();
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
