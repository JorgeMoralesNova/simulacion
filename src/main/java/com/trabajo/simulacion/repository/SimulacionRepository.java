package com.trabajo.simulacion.repository;

import com.trabajo.simulacion.model.Simulacion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SimulacionRepository extends JpaRepository<Simulacion, Long> {
}