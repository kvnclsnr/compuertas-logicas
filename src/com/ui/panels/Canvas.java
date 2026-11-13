package com.ui.panels;

import com.engine.circuit.Circuit;
import com.ui.circuit.Pin;
import com.ui.circuit.Wire;
import com.ui.events.GateDragHandler;
import com.ui.icons.And;
import com.ui.icons.LogicGate;
import com.ui.icons.Nand;
import com.ui.icons.Nor;
import com.ui.icons.Not;
import com.ui.icons.Or;
import com.ui.icons.Switch;
import com.ui.icons.Xnor;
import com.ui.icons.Xor;
import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.BasicStroke;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.datatransfer.DataFlavor;
import java.awt.geom.CubicCurve2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.TransferHandler;

public class Canvas extends JPanel {
    private static final int ICON_SIZE = 96;
    
    private final List<Wire> wires = new ArrayList<>();
    private final Circuit circuit = new Circuit("MainCircuit");
    
    private Pin activePin = null;
    private Point currentMousePoint = null;
    
    public Canvas() {
        super(null);
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setBorder(this, Palette.LINE);
        installGateDrop();
    }
    
    public Pin findPinAt(JComponent comp, Point pointInComp) {
        if (comp instanceof Switch) {
            Pin out = ((Switch) comp).getOutputPin();
            if (out.containsInComponent(pointInComp)) {
                return out;
            }
        } else if (comp instanceof LogicGate) {
            for (Pin pin : ((LogicGate) comp).getPins()) {
                if (pin.containsInComponent(pointInComp)) {
                    return pin;
                }
            }
        }
        return null;
    }
    
    public Pin findPinOnCanvas(Point canvasPoint) {
        for (Component c : getComponents()) {
            if (c instanceof Switch) {
                Pin out = ((Switch) c).getOutputPin();
                if (out.containsOnCanvas(canvasPoint)) {
                    return out;
                }
            } else if (c instanceof LogicGate) {
                for (Pin pin : ((LogicGate) c).getPins()) {
                    if (pin.containsOnCanvas(canvasPoint)) {
                        return pin;
                    }
                }
            }
        }
        return null;
    }
    
    public Wire findWireConnectedToInput(Pin inputPin) {
        for (Wire w : wires) {
            if (w.getTarget().getOwner() == inputPin.getOwner() && w.getTarget().getIndex() == inputPin.getIndex()) {
                return w;
            }
        }
        return null;
    }
    
    public void startWiring(Pin pin) {
        // Si el usuario hace clic en un pin de entrada que ya tiene un cable conectado:
        // "Desenchufamos" el cable de esa entrada y lo dejamos en la mano del usuario para reubicarlo.
        if (pin.getType() == Pin.Type.INPUT) {
            Wire existingWire = findWireConnectedToInput(pin);
            if (existingWire != null) {
                Pin source = existingWire.getSource();
                disconnectWire(existingWire);
                this.activePin = source;
                this.currentMousePoint = pin.getLocationOnCanvas();
                repaint();
                return;
            }
        }
        
        this.activePin = pin;
        this.currentMousePoint = pin.getLocationOnCanvas();
        repaint();
    }
    
    public void dragWiring(Point canvasPoint) {
        this.currentMousePoint = canvasPoint;
        repaint();
    }
    
    public void finishWiring(Point canvasPoint) {
        if (activePin == null) {
            return;
        }
        
        Pin targetPin = findPinOnCanvas(canvasPoint);
        if (targetPin != null && targetPin != activePin && targetPin.getOwner() != activePin.getOwner()) {
            if (activePin.getType() == Pin.Type.OUTPUT && targetPin.getType() == Pin.Type.INPUT) {
                connectWire(activePin, targetPin);
            } else if (activePin.getType() == Pin.Type.INPUT && targetPin.getType() == Pin.Type.OUTPUT) {
                connectWire(targetPin, activePin);
            }
        }
        
        this.activePin = null;
        this.currentMousePoint = null;
        repaint();
    }
    
    public void connectWire(Pin outputPin, Pin inputPin) {
        // Si la entrada ya tenía un cable, desconectarlo y limpiar el motor
        Wire existingWire = findWireConnectedToInput(inputPin);
        if (existingWire != null) {
            disconnectWire(existingWire);
        }
        
        // Creamos y agregamos el nuevo cable
        Wire wire = new Wire(outputPin, inputPin);
        wires.add(wire);
        
        // Conectar lógicamente en el motor
        if (inputPin.getOwner() instanceof LogicGate) {
            ((LogicGate) inputPin.getOwner()).setPinInput(inputPin.getIndex(), outputPin.getLogicSource());
        }
        
        evaluateCircuit();
    }
    
    public void disconnectWire(Wire wire) {
        wires.remove(wire);
        if (wire.getTarget().getOwner() instanceof LogicGate) {
            ((LogicGate) wire.getTarget().getOwner()).clearPinInput(wire.getTarget().getIndex());
        }
        evaluateCircuit();
    }
    
    public void disconnectPin(Pin pin) {
        if (pin.getType() == Pin.Type.INPUT) {
            Wire wire = findWireConnectedToInput(pin);
            if (wire != null) {
                disconnectWire(wire);
            }
        } else {
            List<Wire> toRemove = new ArrayList<>();
            for (Wire w : wires) {
                if (w.getSource().getOwner() == pin.getOwner()) {
                    toRemove.add(w);
                }
            }
            for (Wire w : toRemove) {
                disconnectWire(w);
            }
        }
    }
    
    public void evaluateCircuit() {
        for (int pass = 0; pass < 5; pass++) {
            for (Component c : getComponents()) {
                if (c instanceof LogicGate) {
                    ((LogicGate) c).getOutputValue();
                }
            }
        }
        repaint();
    }
    
    public void removeGate(JComponent gate) {
        wires.removeIf(w -> w.isConnectedTo(gate));
        remove(gate);
        evaluateCircuit();
    }
    
    private void addGate(String name, JComponent icon, Point location) {
        icon.setToolTipText(name);
        // Styles.setBorder(icon, Palette.ACCENT);
        
        icon.setBounds(
            Math.max(0, location.x - ICON_SIZE / 2),
            Math.max(0, location.y - ICON_SIZE / 2),
            ICON_SIZE,
            ICON_SIZE
        );
        
        new GateDragHandler(icon);
        
        if (icon instanceof LogicGate) {
            circuit.addGate(((LogicGate) icon).getEngineGate());
        }
        
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
                } catch (java.awt.datatransfer.UnsupportedFlavorException | java.io.IOException exception) {
                    System.err.println("Could not add the dragged gate: " + exception.getMessage());
                    return false;
                }
            }
        });
    }
    
    private JComponent createGateIcon(String name) {
        switch (name) {
            case "INPUT":
                return new Switch(ICON_SIZE);
            case "AND":
                return new And(ICON_SIZE);
            case "NAND":
                return new Nand(ICON_SIZE);
            case "OR":
                return new Or(ICON_SIZE);
            case "NOR":
                return new Nor(ICON_SIZE);
            case "NOT":
                return new Not(ICON_SIZE);
            case "XOR":
                return new Xor(ICON_SIZE);
            case "XNOR":
                return new Xnor(ICON_SIZE);
            default:
                return null;
        }
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        // 1. Dibujar los cables existentes debajo de los componentes
        for (Wire wire : wires) {
            wire.draw(g2d);
        }
        
        // 2. Dibujar el cable provisional curvado que sigue al ratón
        if (activePin != null && currentMousePoint != null) {
            Point p1 = activePin.getLocationOnCanvas();
            g2d.setColor(Palette.ACCENT);
            g2d.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND, 10.0f, new float[]{6.0f, 6.0f}, 0.0f));
            
            int dx = Math.abs(currentMousePoint.x - p1.x);
            int controlDist = Math.max(25, dx / 2);
            CubicCurve2D curve = new CubicCurve2D.Double(
                p1.x, p1.y,
                p1.x + controlDist, p1.y,
                currentMousePoint.x - controlDist, currentMousePoint.y,
                currentMousePoint.x, currentMousePoint.y
            );
            g2d.draw(curve);
            
            // Punto guía en el cursor
            g2d.fillOval(currentMousePoint.x - 4, currentMousePoint.y - 4, 8, 8);
        }
        
        g2d.dispose();
    }
}
