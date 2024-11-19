// Paquete que contiene las clases relacionadas con el acceso a datos y repositorios
package com.trabajo.simulacion.repository;

// Importa la clase modelo Simulacion que representa las simulaciones en la base de datos
import com.trabajo.simulacion.model.Simulacion;

// Importa JpaRepository, una interfaz de Spring Data JPA que proporciona métodos para operaciones CRUD
import org.springframework.data.jpa.repository.JpaRepository;

// Define una interfaz para interactuar con la base de datos de simulaciones
public interface SimulacionRepository extends JpaRepository<Simulacion, Long> {
    // Esta interfaz hereda los métodos de JpaRepository, lo que incluye:
    // - save(): Guardar o actualizar una entidad
    // - findById(): Buscar una entidad por su ID
    // - findAll(): Obtener todas las entidades
    // - deleteById(): Eliminar una entidad por su ID
    // - count(): Contar el número total de entidades

    // Al heredar de JpaRepository, no es necesario implementar manualmente los métodos básicos,
    // ya que Spring Data JPA los genera automáticamente en tiempo de ejecución.
}
