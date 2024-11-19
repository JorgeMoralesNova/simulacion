package com.trabajo.simulacion.service;



import com.trabajo.simulacion.model.Simulacion;
import com.trabajo.simulacion.repository.SimulacionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


//
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.OutputStream;

@Service
public class SimulacionService {

    @Autowired
    private SimulacionRepository simulacionRepository;

    // Algoritmo de Cuadrados Medios
    public List<Integer> cuadradosMedios(int semilla, int iteraciones) {
        List<Integer> resultados = new ArrayList<>();
        int actual = semilla;
        for (int i = 0; i < iteraciones; i++) {
            actual = (actual * actual) % 10000; // Toma los últimos 4 dígitos
            resultados.add(actual);
        }
        guardarSimulacion("Cuadrados Medios", semilla, iteraciones, resultados.toString());
        return resultados;
    }

    // Algoritmo Congruencial Cuadrático
    public List<Integer> congruencialCuadratico(int a, int b, int c, int semilla, int iteraciones) {
        List<Integer> resultados = new ArrayList<>();
        int actual = semilla;
        for (int i = 0; i < iteraciones; i++) {
            actual = (a * actual * actual + b * actual + c) % 10000;
            resultados.add(actual);
        }
        guardarSimulacion("Congruencial Cuadrático", semilla, iteraciones, resultados.toString());
        return resultados;
    }

    // Algoritmo Blum Blum Shub
    public List<Integer> blumBlumShub(int semilla, int iteraciones) {
        List<Integer> resultados = new ArrayList<>();
        int p = 11, q = 19; // Primos grandes
        int n = p * q;
        int actual = semilla;
        for (int i = 0; i < iteraciones; i++) {
            actual = (actual * actual) % n;
            resultados.add(actual % 2);
        }
        guardarSimulacion("Blum Blum Shub", semilla, iteraciones, resultados.toString());
        return resultados;
    }

    // Guardar simulación en la base de datos
    private void guardarSimulacion(String algoritmo, int semilla, int iteraciones, String resultados) {
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
            fila.createCell(2).setCellValue(sim.getSemilla());
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
            fila.createCell(2).setCellValue(sim.getSemilla());
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
