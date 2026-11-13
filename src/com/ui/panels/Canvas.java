package com.ui.panels;

import com.ui.events.GateDragHandler;
import com.ui.gates.AndGate;
import com.ui.gates.NandGate;
import com.ui.gates.NorGate;
import com.ui.gates.NotGate;
import com.ui.gates.OrGate;
import com.ui.gates.XnorGate;
import com.ui.gates.XorGate;
import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class Canvas extends JPanel {
    private static final int ICON_SIZE = 96;
    
    public Canvas() {
        super(null);
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setBorder(this, Palette.LINE);
        installGateDrop();
    }
    
    private void addGate(String name, JComponent icon, Point location) {
        icon.setToolTipText(name);
        
        Styles.setBorder(icon, Palette.ACCENT);
        
        icon.setBounds(
            Math.max(0, location.x - ICON_SIZE / 2),
            Math.max(0, location.y - ICON_SIZE / 2),
            ICON_SIZE,
            ICON_SIZE
        );
        
        new GateDragHandler(icon);
        
        add(icon);
        revalidate();
        repaint();
    }
    
    private void installGateDrop() {
        setTransferHandler(new TransferHandler() {
            @Override
            public boolean canImport(TransferSupport support) {
                return support.isDrop() && support.isDataFlavorSupported(DataFlavor.stringFlavor);
            }
            
            @Override
            public boolean importData(TransferSupport support) {
                if (!canImport(support)) {
                    return false;
                }
                
                try {
                    String name = (String) support.getTransferable().getTransferData(DataFlavor.stringFlavor);
                    JComponent icon = createGateIcon(name);
                    
                    if (icon == null) {
                        return false;
                    }
                    
                    addGate(name, icon, support.getDropLocation().getDropPoint());
                    
                    return true;
                }
                
                catch (java.awt.datatransfer.UnsupportedFlavorException | java.io.IOException exception) {
                    System.err.println("Could not add the dragged gate: " + exception.getMessage());
                    return false;
                }
            }
        });
    }
    
    private JComponent createGateIcon(String name) {
        switch (name) {
            case "AND":
                return new AndGate(ICON_SIZE);
            case "NAND":
                return new NandGate(ICON_SIZE);
            case "OR":
                return new OrGate(ICON_SIZE);
            case "NOR":
                return new NorGate(ICON_SIZE);
            case "NOT":
                return new NotGate(ICON_SIZE);
            case "XOR":
                return new XorGate(ICON_SIZE);
            case "XNOR":
                return new XnorGate(ICON_SIZE);
            default:
                return null;
        }
    }
}
