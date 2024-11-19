// Paquete principal de la aplicación, donde reside la clase de entrada
package com.trabajo.simulacion;

// Importa las clases necesarias para iniciar la aplicación Spring Boot
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Anotación que marca esta clase como la clase principal de la aplicación Spring Boot
@SpringBootApplication
public class SimulacionApplication {

	// Método principal (main): Punto de entrada de la aplicación
	public static void main(String[] args) {
		// Llama a SpringApplication.run para iniciar la aplicación Spring Boot
		SpringApplication.run(SimulacionApplication.class, args);
		// Spring Boot inicializa el contenedor de contexto de aplicación, configura componentes
		// y levanta el servidor web embebido
	}
}
