// Paquete que contiene la clase modelo para representar una simulación en la base de datos
package com.trabajo.simulacion.model;

// Importaciones necesarias para las anotaciones de JPA (Jakarta Persistence)
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// Importación para manejar números grandes
import java.math.BigInteger;

// Anotación que marca esta clase como una entidad de JPA (se mapeará a una tabla en la base de datos)
@Entity
public class Simulacion {

    // Anotación que marca este campo como la clave primaria de la entidad
    @Id
    // Generación automática del valor del ID utilizando la estrategia de identidad
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Identificador único para cada simulación

    // Campo para almacenar el nombre del algoritmo utilizado en la simulación
    private String algoritmo;

    // Campo para almacenar la semilla utilizada, de tipo BigInteger para soportar números grandes
    private BigInteger semilla;

    // Campo para almacenar el número de iteraciones realizadas en la simulación
    private int iteraciones;

    // Campo para almacenar los resultados generados como una cadena de texto
    private String resultados;

    // Constructor vacío requerido por JPA
    public Simulacion() {}

    // Constructor que inicializa todos los campos, utilizado para crear objetos Simulacion fácilmente
    public Simulacion(String algoritmo, BigInteger semilla, int iteraciones, String resultados) {
        this.algoritmo = algoritmo;
        this.semilla = semilla;
        this.iteraciones = iteraciones;
        this.resultados = resultados;
    }

    // Métodos Getter y Setter para cada campo, utilizados para acceder y modificar los valores de los atributos

    public Long getId() {
        return id; // Devuelve el identificador único de la simulación
    }

    public void setId(Long id) {
        this.id = id; // Establece el identificador único de la simulación
    }

    public String getAlgoritmo() {
        return algoritmo; // Devuelve el nombre del algoritmo utilizado
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo; // Establece el nombre del algoritmo utilizado
    }

    public BigInteger getSemilla() {
        return semilla; // Devuelve la semilla utilizada en la simulación
    }

    public void setSemilla(BigInteger semilla) {
        this.semilla = semilla; // Establece la semilla utilizada en la simulación
    }

    public int getIteraciones() {
        return iteraciones; // Devuelve el número de iteraciones realizadas
    }

    public void setIteraciones(int iteraciones) {
        this.iteraciones = iteraciones; // Establece el número de iteraciones realizadas
    }

    public String getResultados() {
        return resultados; // Devuelve los resultados generados como cadena de texto
    }

    public void setResultados(String resultados) {
        this.resultados = resultados; // Establece los resultados generados como cadena de texto
    }
}
