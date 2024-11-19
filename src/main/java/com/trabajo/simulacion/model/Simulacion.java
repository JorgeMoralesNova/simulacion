package com.trabajo.simulacion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.math.BigInteger;

@Entity
public class Simulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String algoritmo;
    private BigInteger semilla; // Cambiado a BigInteger
    private int iteraciones;
    private String resultados;

    // Constructor, Getters y Setters
    public Simulacion() {}

    public Simulacion(String algoritmo, BigInteger semilla, int iteraciones, String resultados) {
        this.algoritmo = algoritmo;
        this.semilla = semilla;
        this.iteraciones = iteraciones;
        this.resultados = resultados;
    }

    // Getters y setters aquí

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAlgoritmo() {
        return algoritmo;
    }

    public void setAlgoritmo(String algoritmo) {
        this.algoritmo = algoritmo;
    }

    public BigInteger getSemilla() {
        return semilla;
    }

    public void setSemilla(BigInteger semilla) {
        this.semilla = semilla;
    }

    public int getIteraciones() {
        return iteraciones;
    }

    public void setIteraciones(int iteraciones) {
        this.iteraciones = iteraciones;
    }

    public String getResultados() {
        return resultados;
    }

    public void setResultados(String resultados) {
        this.resultados = resultados;
    }
}
