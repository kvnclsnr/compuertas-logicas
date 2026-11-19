package com.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ui.layouts.main.Content;
import com.ui.layouts.main.TopBar;
import com.ui.persistence.ProjectPersistence;
import com.ui.persistence.SaveOnExitListener;
import com.ui.layouts.canvas.Canvas;

// ARRANCA LA APLICACION Y MUESTRA LA VENTANA PRINCIPAL
public class App {
    private static final String WINDOW_TITLE = "Compuertas Lógicas";
    private static final String SCROLL_PANE_BORDER_KEY = "ScrollPane.border";
    private static final int WINDOW_WIDTH = 1488;
    private static final int WINDOW_HEIGHT = 837;
    private static final int MIN_WINDOW_WIDTH = 800;
    private static final int MIN_WINDOW_HEIGHT = 600;
    
    // QUITA EL BORDE A LOS SCROLL Y ABRE LA VENTANA EN EL HILO DE SWING
    public static void main(String[] args) {
        UIManager.put(SCROLL_PANE_BORDER_KEY, BorderFactory.createEmptyBorder());
        SwingUtilities.invokeLater(App::showWindow);
    }
    
    // CREA LA VENTANA, LA CENTRA Y LA MUESTRA
    private static void showWindow() {
        JFrame window = new JFrame(WINDOW_TITLE);
        Canvas canvas = new Canvas();
        
        ProjectPersistence.load(canvas);
        
        window.setContentPane(createRootPanel(canvas));
        window.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        window.setMinimumSize(new Dimension(MIN_WINDOW_WIDTH, MIN_WINDOW_HEIGHT));
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.addWindowListener(new SaveOnExitListener(canvas));
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    
    // ARMA EL PANEL PRINCIPAL CON LA BARRA ARRIBA Y EL CONTENIDO AL CENTRO
    private static JPanel createRootPanel(Canvas canvas) {
        JPanel root = new JPanel(new BorderLayout());
        
        root.add(new TopBar(canvas), BorderLayout.NORTH);
        root.add(new Content(canvas), BorderLayout.CENTER);
        
        return root;
    }
}