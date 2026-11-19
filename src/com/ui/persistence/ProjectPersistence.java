package com.ui.persistence;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import com.ui.export.CircuitTextWriter;
import com.ui.importer.CircuitData;
import com.ui.importer.CircuitLoader;
import com.ui.importer.CircuitTextReader;
import com.ui.layouts.canvas.Canvas;

// GUARDA Y CARGA EL LIENZO EN UN ARCHIVO FIJO PARA QUE EL PROYECTO QUEDE GUARDADO ENTRE SESIONES
public final class ProjectPersistence {
    private static final String FOLDER_NAME = ".compuertas-logicas";
    private static final String FILE_NAME = "proyecto.txt";
    
    private ProjectPersistence() { }
    
    // DEVUELVE LA RUTA FIJA DONDE SE GUARDA EL PROYECTO, DENTRO DE LA CARPETA DEL USUARIO
    private static Path getProjectFile() {
        String home = System.getProperty("user.home");
        
        return Paths.get(home, FOLDER_NAME, FILE_NAME);
    }
    
    // GUARDA EL LIENZO EN EL ARCHIVO FIJO, CREANDO LA CARPETA SI HACE FALTA
    public static void save(Canvas canvas) {
        Path file = getProjectFile();
        String text = CircuitTextWriter.write(canvas.getElements(), canvas.getWires());
        
        try {
            Files.createDirectories(file.getParent());
            Files.writeString(file, text, StandardCharsets.UTF_8);
        }
        
        catch (IOException exception) {
            System.err.println("No se pudo guardar el proyecto: " + exception.getMessage());
        }
    }
    
    // CARGA EL LIENZO DESDE EL ARCHIVO FIJO, SI NO EXISTE O ESTA DAÑADO EL LIENZO QUEDA EN BLANCO
    public static void load(Canvas canvas) {
        Path file = getProjectFile();
        
        if (!Files.exists(file)) return;
        
        try {
            List<String> lines = Files.readAllLines(file, StandardCharsets.UTF_8);
            CircuitData data = CircuitTextReader.read(lines);
            
            CircuitLoader.load(data, canvas);
        }
        
        catch (IOException | IllegalArgumentException exception) {
            System.err.println("No se pudo cargar el proyecto: " + exception.getMessage());
        }
    }
}