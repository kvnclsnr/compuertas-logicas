package com.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import com.ui.layouts.main.Content;
import com.ui.layouts.main.TopBar;

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
        
        window.setTitle("Compuertas Lógicas");
        
        Image icono = new ImageIcon("src/assets/icon.png").getImage();
        window.setIconImage(icono);
        
        window.setVisible(true);
    }
}