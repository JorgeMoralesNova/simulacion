package com.trabajo.simulacion.controller;


import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;

@RestController
public class ArchivoController {

    @GetMapping("/archivos/{nombreArchivo}")
    public ResponseEntity<FileSystemResource> descargarArchivo(@PathVariable("nombreArchivo") String nombreArchivo) {
        File archivo = new File(System.getProperty("java.io.tmpdir") + "/" + nombreArchivo);
        if (!archivo.exists()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + archivo.getName())
                .body(new FileSystemResource(archivo));
    }
}
