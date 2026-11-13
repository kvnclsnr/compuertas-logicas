package com.ui.panels;

import com.ui.gates.AndGate;
import com.ui.gates.NandGate;
import com.ui.gates.NorGate;
import com.ui.gates.NotGate;
import com.ui.gates.OrGate;
import com.ui.gates.XnorGate;
import com.ui.gates.XorGate;
import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.TransferHandler;

public class GatePalette extends JScrollPane {
    private static final String[] GATE_NAMES = {
        "AND", "OR", "NOT", "NAND", "NOR", "XOR", "XNOR"
    };
    
    private static final int ICON_SIZE = 64;
    private static final int ROW_HEIGHT = 72;
    private static final int PALETTE_WIDTH = 192;
    private static final int SCROLL_UNIT = 16;
    
    private final JPanel gatesPanel = new JPanel();
    
    public GatePalette() {
        gatesPanel.setLayout(new BoxLayout(gatesPanel, BoxLayout.Y_AXIS));
        
        Styles.setBackground(gatesPanel, Palette.SURFACE);
        Styles.setBorder(gatesPanel, Palette.LINE);
        Styles.setPadding(gatesPanel, 8);
        
        for (int i = 0; i < GATE_NAMES.length; i++) {
            
            if (i > 0) {
                gatesPanel.add(Box.createVerticalStrut(8));
            }
            
            String name = GATE_NAMES[i];
            gatesPanel.add(createGateItem(name));
        }
        
        gatesPanel.setPreferredSize(new Dimension(PALETTE_WIDTH, gatesPanel.getPreferredSize().height));
        
        setViewportView(gatesPanel);
        
        setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_NEVER);
        
        getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT);
    }
    
    private JPanel createGateItem(String name) {
        JPanel item = new JPanel(new BorderLayout(16, 0));
        
        Styles.setBackground(item, Palette.SURFACE);
        Styles.setBorder(item, Palette.ACCENT);
        Styles.setPadding(item, 8, 16);
        
        item.setPreferredSize(new Dimension(Integer.MAX_VALUE, ROW_HEIGHT));
        item.setMaximumSize(new Dimension(Integer.MAX_VALUE, ROW_HEIGHT));
        
        item.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        
        JComponent icon = createIcon(name);
        JLabel label = new JLabel(name);
        
        Styles.setFont(label, 16f, true);
        
        item.add(icon, BorderLayout.WEST);
        item.add(label, BorderLayout.CENTER);
        
        TransferHandler transferHandler = new TransferHandler() {
            @Override
            protected java.awt.datatransfer.Transferable createTransferable(JComponent component) {
                return new StringSelection(name);
            }
            
            @Override
            public int getSourceActions(JComponent component) {
                return COPY;
            }
        };
        
        installDrag(item, transferHandler);
        installDrag(icon, transferHandler);
        installDrag(label, transferHandler);
        
        return item;
    }
    
    private void installDrag(JComponent component, TransferHandler transferHandler) {
        component.setTransferHandler(transferHandler);
        
        MouseAdapter dragListener = new MouseAdapter() {
            private Point pressPoint;
            
            @Override
            public void mousePressed(MouseEvent event) {
                pressPoint = event.getPoint();
            }
            
            @Override
            public void mouseReleased(MouseEvent event) {
                pressPoint = null;
            }
            
            @Override
            public void mouseDragged(MouseEvent event) {
                if (pressPoint != null && pressPoint.distance(event.getPoint()) >= 5) {
                    transferHandler.exportAsDrag(component, event, TransferHandler.COPY);
                    pressPoint = null;
                }
            }
        };
        
        component.addMouseListener(dragListener);
        component.addMouseMotionListener(dragListener);
    }
    
    private JComponent createIcon(String name) {
        switch (name) {
            case "AND":
                return new AndGate(ICON_SIZE);
            case "OR":
                return new OrGate(ICON_SIZE);
            case "NOT":
                return new NotGate(ICON_SIZE);
            case "NAND":
                return new NandGate(ICON_SIZE);
            case "NOR":
                return new NorGate(ICON_SIZE);
            case "XOR":
                return new XorGate(ICON_SIZE);
            case "XNOR":
                return new XnorGate(ICON_SIZE);
            default:
                throw new IllegalArgumentException("Unknown gate: " + name);
        }
    }
}
