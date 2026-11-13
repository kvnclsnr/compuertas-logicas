package com.ui;

import com.ui.panels.Content;
import com.ui.panels.TopBar;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class App {
    public static void main(String[] args) {
        UIManager.put("ScrollPane.border", BorderFactory.createEmptyBorder());
        SwingUtilities.invokeLater(App::showWindow);
    }
    
    private static void showWindow() {
        JFrame window = new JFrame();
        JPanel root = new JPanel(new BorderLayout());
        
        root.add(new TopBar(), BorderLayout.NORTH);
        root.add(new Content(), BorderLayout.CENTER);
        
        window.setContentPane(root);
        
        window.setSize(1000, 750);
        window.setMinimumSize(new Dimension(800, 600));
        
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setLocationRelativeTo(null);
        window.setVisible(true);
        
        window.setTitle("Compuertas Lógicas");
    }
}