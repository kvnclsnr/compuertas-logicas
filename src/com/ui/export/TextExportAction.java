package com.ui.export;

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

// ACCION DE EXPORTAR EL LIENZO A UN ARCHIVO DE TEXTO
public class TextExportAction implements ActionListener {
    private static final String DEFAULT_FILE_NAME = "circuito.txt";
    private static final String EXTENSION = "txt";
    private static final String FILTER_DESCRIPTION = "Texto (*.txt)";
    private static final String ERROR_TITLE = "Exportar";
    private static final String ERROR_MESSAGE = "No se pudo guardar el archivo";
    
    private final Canvas canvas;
    
    // CREA LA ACCION PARA EXPORTAR UN LIENZO
    public TextExportAction(Canvas canvas) {
        this.canvas = canvas;
    }
    
    // PIDE DONDE GUARDAR Y, SI SE ELIGIO UN ARCHIVO, ESCRIBE EL LIENZO EN EL
    @Override
    public void actionPerformed(ActionEvent event) {
        File file = chooseFile();
        
        if (file != null) {
            save(file);
        }
    }
    
    // MUESTRA EL DIALOGO DE GUARDAR Y DEVUELVE EL ARCHIVO ELEGIDO, O NULL SI SE CANCELO
    private File chooseFile() {
        JFileChooser chooser = new JFileChooser();
        
        chooser.setSelectedFile(new File(DEFAULT_FILE_NAME));
        chooser.setFileFilter(new FileNameExtensionFilter(FILTER_DESCRIPTION, EXTENSION));
        
        if (chooser.showSaveDialog(canvas) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        return addExtension(chooser.getSelectedFile());
    }
    
    // AGREGA .txt AL NOMBRE SI EL USUARIO NO LO ESCRIBIO
    private File addExtension(File file) {
        if (file.getName().toLowerCase().endsWith("." + EXTENSION)) {
            return file;
        }
        
        return new File(file.getPath() + "." + EXTENSION);
    }
    
    // ESCRIBE EL TEXTO DEL LIENZO EN EL ARCHIVO Y AVISA SI NO SE PUDO
    private void save(File file) {
        String text = CircuitTextWriter.write(canvas.getElements(), canvas.getWires());
        
        try {
            Files.writeString(file.toPath(), text, StandardCharsets.UTF_8);
        }
        
        catch (IOException exception) {
            JOptionPane.showMessageDialog(canvas, ERROR_MESSAGE, ERROR_TITLE, JOptionPane.ERROR_MESSAGE);
        }
    }
}