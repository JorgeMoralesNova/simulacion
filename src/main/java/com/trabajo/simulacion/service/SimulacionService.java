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
// Anotación que marca esta clase como un servicio de Spring para la lógica de negocio
@Service
public class SimulacionService {

    // Inyección de dependencia para utilizar el repositorio (para guardar datos en una base de datos)
    @Autowired
    private SimulacionRepository simulacionRepository;

    // Método que implementa el algoritmo de Cuadrados Medios para generar números pseudoaleatorios
    public List<BigInteger> cuadradosMedios(BigInteger semilla, int iteraciones) {
        // Validación inicial: la semilla debe ser mayor que cero
        if (semilla.compareTo(BigInteger.ZERO) <= 0) {
            throw new IllegalArgumentException("La semilla debe ser un número positivo.");
        }

        // Determina la cantidad de dígitos de la semilla (esto se usará para calcular los dígitos centrales)
        int numDigitos = semilla.toString().length();

        // Lista que almacenará los resultados generados
        List<BigInteger> resultados = new ArrayList<>();

        // Conjunto utilizado para detectar ciclos (valores repetidos)
        Set<BigInteger> vistos = new HashSet<>();

        // Variable que almacena el valor actual en cada iteración, comenzando con la semilla
        BigInteger actual = semilla;

        // Bucle para generar números pseudoaleatorios durante el número especificado de iteraciones
        for (int i = 0; i < iteraciones; i++) {
            // Calcula el cuadrado del valor actual
            BigInteger cuadrado = actual.multiply(actual);

            // Calcula el número total de dígitos esperado en el cuadrado (doble del número de dígitos de la semilla)
            int totalDigitos = numDigitos * 2;

            // Convierte el cuadrado a una cadena de texto, rellenando con ceros si es necesario
            String cuadradoStr = String.format("%0" + totalDigitos + "d", cuadrado);

            // Determina el índice de inicio para extraer los dígitos centrales del cuadrado
            int inicio = (cuadradoStr.length() - numDigitos) / 2;

            // Extrae los dígitos centrales como una subcadena
            String central = cuadradoStr.substring(inicio, inicio + numDigitos);

            // Convierte la subcadena de vuelta a un BigInteger y actualiza el valor actual
            actual = new BigInteger(central);

            // Comprueba si el número actual ya ha sido generado (ciclo detectado)
            if (vistos.contains(actual)) {
                System.out.println("Ciclo detectado en la iteración " + i + ": " + actual);
                break; // Detiene el bucle si se detecta un ciclo
            }

            // Agrega el valor actual a las listas de resultados y al conjunto de valores vistos
            resultados.add(actual);
            vistos.add(actual);
        }

        // Guarda los resultados de la simulación (esto probablemente implique persistir los datos en una base de datos)
        guardarSimulacion("Cuadrados Medios Generalizados", semilla, iteraciones, resultados.toString());

        // Devuelve la lista de números generados
        return resultados;
    }







    public List<BigInteger> congruencialCuadratico(BigInteger a, BigInteger b, BigInteger c, BigInteger semilla, int iteraciones, BigInteger modulo) {
        // Validaciones iniciales de los parámetros
        if (a == null || b == null || c == null || semilla == null || modulo == null) {
            // Si algún parámetro es nulo, se lanza una excepción
            throw new IllegalArgumentException("Ninguno de los parámetros puede ser nulo.");
        }
        if (modulo.compareTo(BigInteger.ZERO) <= 0) {
            // El módulo debe ser un número positivo, de lo contrario se lanza una excepción
            throw new IllegalArgumentException("El módulo debe ser un número positivo.");
        }
        if (iteraciones <= 0) {
            // El número de iteraciones debe ser mayor que cero, de lo contrario se lanza una excepción
            throw new IllegalArgumentException("El número de iteraciones debe ser mayor a cero.");
        }

        // Lista para almacenar los resultados generados por el algoritmo
        List<BigInteger> resultados = new ArrayList<>();

        // Conjunto utilizado para detectar ciclos y evitar valores repetidos
        Set<BigInteger> vistos = new HashSet<>();

        // Variable que almacena el valor actual en cada iteración, inicializada con la semilla
        BigInteger actual = semilla;

        // Bucle para realizar las iteraciones del algoritmo
        for (int i = 0; i < iteraciones; i++) {
            // Aplica la fórmula del generador congruencial cuadrático:
            // actual = (a * actual^2 + b * actual + c) % modulo
            actual = (a.multiply(actual).multiply(actual).add(b.multiply(actual)).add(c)).mod(modulo);

            // Imprime el resultado de cada iteración en la consola para depuración
            System.out.println("Iteración " + i + ": Resultado=" + actual);

            // Detecta si el valor actual ya ha sido generado (ciclo detectado)
            if (vistos.contains(actual)) {
                // Si hay un ciclo, imprime un mensaje en la consola y detiene el bucle
                System.out.println("Ciclo detectado en la iteración " + i + ": Número repetido: " + actual);
                break;
            }

            // Agrega el valor actual a las listas de resultados y al conjunto de valores vistos
            resultados.add(actual);
            vistos.add(actual);
        }

        // Guarda los resultados de la simulación en un repositorio o sistema de almacenamiento
        try {
            // Llama al método para guardar la simulación, manejando errores si ocurren
            guardarSimulacion("Congruencial Cuadrático", semilla, iteraciones, resultados.toString());
        } catch (Exception e) {
            // Si ocurre un error al guardar, imprime el mensaje de error en la consola
            System.err.println("Error al guardar la simulación: " + e.getMessage());
        }

        // Devuelve la lista de resultados generados por el algoritmo
        return resultados;
    }


    // Algoritmo Blum Blum Shub: Generador de números pseudoaleatorios basado en propiedades criptográficas
    public List<BigInteger> blumBlumShub(BigInteger semilla, int iteraciones, BigInteger p, BigInteger q) {
        // Validaciones iniciales de los parámetros
        if (p == null || q == null || semilla == null) {
            // Si alguno de los parámetros es nulo, se lanza una excepción
            throw new IllegalArgumentException("Ninguno de los parámetros puede ser nulo.");
        }
        if (!p.isProbablePrime(100) || !q.isProbablePrime(100)) {
            // Valida que p y q sean números primos utilizando el test probabilístico
            throw new IllegalArgumentException("Los valores de p y q deben ser primos.");
        }
        if (!p.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3)) ||
                !q.mod(BigInteger.valueOf(4)).equals(BigInteger.valueOf(3))) {
            // Valida que p y q sean congruentes a 3 mod 4, una condición necesaria para el algoritmo
            throw new IllegalArgumentException("Los valores de p y q deben ser congruentes a 3 mod 4.");
        }
        if (semilla.compareTo(BigInteger.ZERO) <= 0 || semilla.compareTo(p.multiply(q)) >= 0) {
            // La semilla debe estar en el rango (0, p*q), de lo contrario se lanza una excepción
            throw new IllegalArgumentException("La semilla debe estar en el rango (0, p*q).");
        }
        if (iteraciones <= 0) {
            // El número de iteraciones debe ser mayor que cero
            throw new IllegalArgumentException("El número de iteraciones debe ser mayor a cero.");
        }

        // Inicialización del módulo n como el producto de p y q
        BigInteger n = p.multiply(q);

        // Inicialización de la lista de resultados y el conjunto para detectar ciclos
        List<BigInteger> resultados = new ArrayList<>();
        Set<BigInteger> vistos = new HashSet<>(); // Conjunto para rastrear valores repetidos
        BigInteger actual = semilla; // Valor inicial tomado de la semilla

        // Bucle principal para generar los números pseudoaleatorios
        for (int i = 0; i < iteraciones; i++) {
            // Fórmula de Blum Blum Shub:
            // actual = (actual^2) mod n
            actual = actual.multiply(actual).mod(n);

            // Extraer el bit menos significativo del número generado
            BigInteger bit = actual.mod(BigInteger.TWO);
            resultados.add(bit); // Agregar el bit generado a la lista de resultados

            // Imprime el número generado y el bit menos significativo en la consola (para depuración)
            System.out.println("Iteración " + i + ": Número generado=" + actual + ", Bit=" + bit);

            // Detecta si el valor actual ya ha sido generado (ciclo detectado)
            if (vistos.contains(actual)) {
                // Si hay un ciclo, imprime un mensaje en la consola y detiene el bucle
                System.out.println("Ciclo detectado en la iteración " + i + ": Número repetido: " + actual);
                break;
            }

            // Agrega el número generado al conjunto para rastrear valores únicos
            vistos.add(actual);
        }

        // Guarda los resultados de la simulación en algún repositorio o sistema de almacenamiento
        try {
            // Llama al método guardarSimulacion para registrar los datos generados
            guardarSimulacion("Blum Blum Shub", semilla, iteraciones, resultados.toString());
        } catch (Exception e) {
            // Si ocurre un error al guardar los resultados, imprime el error en la consola
            System.err.println("Error al guardar la simulación: " + e.getMessage());
        }

        // Devuelve la lista de bits generados como resultado de la simulación
        return resultados;
    }


    // Método para guardar una simulación en la base de datos
    private void guardarSimulacion(String algoritmo, BigInteger semilla, int iteraciones, String resultados) {
        // Crea un objeto de tipo Simulacion con los datos proporcionados
        Simulacion simulacion = new Simulacion(algoritmo, semilla, iteraciones, resultados);
        // Guarda el objeto simulacion en la base de datos utilizando el repositorio
        simulacionRepository.save(simulacion);
    }

    // Método para obtener todas las simulaciones almacenadas en la base de datos
    public List<Simulacion> obtenerTodasSimulaciones() {
        // Recupera todas las simulaciones almacenadas utilizando el repositorio
        return simulacionRepository.findAll();
    }

    // Método para generar un archivo Excel temporal con las simulaciones almacenadas
    public File generarExcelTemporal() throws IOException {
        // Crea un nuevo libro de Excel
        Workbook workbook = new XSSFWorkbook();
        // Crea una hoja llamada "Simulaciones"
        Sheet hoja = workbook.createSheet("Simulaciones");

        // Agrega una fila de cabecera en la primera fila
        Row cabecera = hoja.createRow(0);
        cabecera.createCell(0).setCellValue("ID"); // Columna para el ID de la simulación
        cabecera.createCell(1).setCellValue("Algoritmo"); // Columna para el algoritmo utilizado
        cabecera.createCell(2).setCellValue("Semilla"); // Columna para la semilla inicial
        cabecera.createCell(3).setCellValue("Iteraciones"); // Columna para el número de iteraciones
        cabecera.createCell(4).setCellValue("Resultados"); // Columna para los resultados generados

        // Obtiene todas las simulaciones de la base de datos
        List<Simulacion> simulaciones = obtenerTodasSimulaciones();
        int numFila = 1; // Inicializa el índice de fila (la fila 0 es para la cabecera)

        // Itera sobre cada simulación para llenar las filas de la hoja de Excel
        for (Simulacion sim : simulaciones) {
            // Crea una nueva fila por cada simulación
            Row fila = hoja.createRow(numFila++);
            // Agrega los datos de la simulación a las columnas correspondientes
            fila.createCell(0).setCellValue(sim.getId()); // ID de la simulación
            fila.createCell(1).setCellValue(sim.getAlgoritmo()); // Algoritmo utilizado
            fila.createCell(2).setCellValue(sim.getSemilla().intValue()); // Semilla inicial como entero
            fila.createCell(3).setCellValue(sim.getIteraciones()); // Número de iteraciones
            fila.createCell(4).setCellValue(sim.getResultados()); // Resultados generados
        }

        // Crea un archivo temporal con el prefijo "simulaciones_" y extensión ".xlsx"
        File archivoTemporal = File.createTempFile("simulaciones_", ".xlsx");

        // Escribe el contenido del libro de Excel en el archivo temporal
        try (FileOutputStream fos = new FileOutputStream(archivoTemporal)) {
            workbook.write(fos);
        }
        // Cierra el libro de Excel para liberar recursos
        workbook.close();

        // Devuelve el archivo temporal generado
        return archivoTemporal;
    }

}
