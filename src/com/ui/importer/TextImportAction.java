package com.ui.importer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.ui.layouts.canvas.Canvas;

// ACCION DE ABRIR UN ARCHIVO DE TEXTO EXPORTADO Y CARGARLO EN EL LIENZO
public class TextImportAction implements ActionListener {
    private static final String EXTENSION = "txt";
    private static final String FILTER_DESCRIPTION = "Texto (*.txt)";
    private static final String ERROR_TITLE = "Abrir";
    private static final String READ_ERROR_MESSAGE = "No se pudo leer el archivo";
    private static final String FORMAT_ERROR_MESSAGE = "El archivo no es valido: ";
    
    private final Canvas canvas;
    
    // CREA LA ACCION PARA CARGAR EN UN LIENZO
    public TextImportAction(Canvas canvas) {
        this.canvas = canvas;
    }
    
    // PIDE EL ARCHIVO Y, SI SE ELIGIO UNO, LO CARGA EN EL LIENZO
    @Override
    public void actionPerformed(ActionEvent event) {
        File file = chooseFile();
        
        if (file != null) {
            open(file);
        }
    }
    
    // MUESTRA EL DIALOGO DE ABRIR Y DEVUELVE EL ARCHIVO ELEGIDO, O NULL SI SE CANCELO
    private File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        
        chooser.setFileFilter(new FileNameExtensionFilter(FILTER_DESCRIPTION, EXTENSION));
        
        if (chooser.showOpenDialog(canvas) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        return chooser.getSelectedFile();
    }
    
    // LEE Y COMPRUEBA EL ARCHIVO ANTES DE TOCAR EL LIENZO Y AVISA SI ALGO FALLA
    private void open(File file) {
        try {
            CircuitData data = CircuitTextReader.read(Files.readAllLines(file.toPath(), StandardCharsets.UTF_8));
            
            CircuitLoader.load(data, canvas);
        }
        
        catch (IOException exception) {
            showError(READ_ERROR_MESSAGE);
        }
        
        catch (IllegalArgumentException exception) {
            showError(FORMAT_ERROR_MESSAGE + exception.getMessage());
        }
    }
    
    // MUESTRA UN MENSAJE DE ERROR SOBRE EL LIENZO
    private void showError(String message) {
        JOptionPane.showMessageDialog(canvas, message, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
    }
}