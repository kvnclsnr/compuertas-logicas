package com.ui;

import com.ui.gates.AndGate;
import com.ui.gates.NandGate;
import com.ui.gates.NorGate;
import com.ui.gates.NotGate;
import com.ui.gates.OrGate;
import com.ui.gates.XnorGate;
import com.ui.gates.XorGate;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class App {
    private static final int ICON_SIZE = 120;
    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(App::showWindow);
    }
    
    private static void showWindow() {
        JFrame window = new JFrame("Compuertas Lógicas");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        JPanel content = new JPanel(new BorderLayout(0, 18));
        content.setBorder(BorderFactory.createEmptyBorder(24, 28, 24, 28));
        
        JLabel heading = new JLabel("Compuertas Lógicas", SwingConstants.CENTER);
        heading.setFont(heading.getFont().deriveFont(Font.BOLD, 24f));
        content.add(heading, BorderLayout.NORTH);
        
        JPanel gates = new JPanel(new GridLayout(2, 1, 0, 14));
        
        JPanel firstRow = new JPanel(new GridLayout(1, 4, 14, 0));
        firstRow.add(createGateCard("AND", new AndGate(ICON_SIZE)));
        firstRow.add(createGateCard("NAND", new NandGate(ICON_SIZE)));
        firstRow.add(createGateCard("OR", new OrGate(ICON_SIZE)));
        firstRow.add(createGateCard("NOR", new NorGate(ICON_SIZE)));
        
        JPanel secondRow = new JPanel(new GridLayout(1, 3, 14, 0));
        secondRow.add(createGateCard("NOT", new NotGate(ICON_SIZE)));
        secondRow.add(createGateCard("XOR", new XorGate(ICON_SIZE)));
        secondRow.add(createGateCard("XNOR", new XnorGate(ICON_SIZE)));
        
        gates.add(firstRow);
        gates.add(secondRow);
        
        content.add(gates, BorderLayout.CENTER);
        
        window.add(content);
        
        window.setSize(800, 600);
        window.setMinimumSize(new Dimension(800, 600));
        window.setLocationRelativeTo(null);
        window.setVisible(true);
    }
    
    private static JPanel createGateCard(String name, JComponent icon) {
        JPanel card = new JPanel(new BorderLayout(0, 8));
        
        card.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(210, 216, 224)),
            BorderFactory.createEmptyBorder(12, 8, 10, 8))
        );
        
        JLabel label = new JLabel(name, SwingConstants.CENTER);
        
        label.setFont(label.getFont().deriveFont(Font.BOLD, 15f));
        
        card.add(icon, BorderLayout.CENTER);
        card.add(label, BorderLayout.SOUTH);
        
        return card;
    }
}
