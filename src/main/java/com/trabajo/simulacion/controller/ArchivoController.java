// Paquete que contiene los controladores relacionados con la gestión de archivos
package com.trabajo.simulacion.controller;

// Importa las clases necesarias para manejar recursos del sistema de archivos y respuestas HTTP
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

// Importación para trabajar con archivos en el sistema
import java.io.File;

// Anotación que marca esta clase como un controlador REST
@RestController
public class ArchivoController {

    // Método para manejar solicitudes GET para descargar un archivo
    @GetMapping("/archivos/{nombreArchivo}")
    public ResponseEntity<FileSystemResource> descargarArchivo(@PathVariable("nombreArchivo") String nombreArchivo) {
        // Construye la ruta completa del archivo temporal utilizando el directorio del sistema
        File archivo = new File(System.getProperty("java.io.tmpdir") + "/" + nombreArchivo);

        // Verifica si el archivo existe en el sistema
        if (!archivo.exists()) {
            // Si el archivo no existe, responde con un código HTTP 404 (Not Found)
            return ResponseEntity.notFound().build();
        }

        // Si el archivo existe, responde con el archivo adjunto para descargar
        return ResponseEntity.ok()
                // Agrega un encabezado HTTP que especifica que el archivo es para descarga
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archivo.getName())
                // Incluye el archivo como el cuerpo de la respuesta
                .body(new FileSystemResource(archivo));
    }
}
