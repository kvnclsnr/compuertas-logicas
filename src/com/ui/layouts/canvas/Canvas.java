package com.ui.layouts.canvas;

import com.engine.circuit.CircuitManager;
import com.ui.events.circuit.CircuitChangeListener;
import com.ui.circuit.wire.PendingWire;
import com.ui.circuit.pin.Pin;
import com.ui.circuit.wire.Wire;
import com.ui.events.drag.GateDragHandler;
import com.ui.events.drag.GateDropHandler;
import com.ui.icons.GateIconFactory;
import com.ui.icons.LogicGate;
import com.ui.icons.Switch;
import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JPanel;

public class Canvas extends JPanel {
    private static final int ICON_SIZE = 96;
    private static final int EVALUATION_PASSES = 5;
    
    private final List<Wire> wires = new ArrayList<>();
    private final PendingWire pendingWire = new PendingWire();
    
    private final CircuitManager circuits = new CircuitManager();
    private CircuitChangeListener circuitChangeListener = null;
    
    public Canvas() {
        super(null);
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setBorder(this, Palette.LINE);
        setTransferHandler(new GateDropHandler(this));
    }
    
    // GUARDA QUIÉN DEBE ENTERARSE CUANDO CAMBIA EL CIRCUITO
    public void setCircuitChangeListener(CircuitChangeListener listener) {
        circuitChangeListener = listener;
    }
    
    // DEVUELVE UNA TABLA DE VERDAD POR CADA CIRCUITO SEPARADO DEL LIENZO
    public List<String[][]> getCircuitTruthTables() {
        return circuits.getTruthTables();
    }
    
    // AVISA AL LISTENER, SI HAY UNO, QUE EL CIRCUITO CAMBIÓ
    private void notifyCircuitChange() {
        if (circuitChangeListener != null) {
            circuitChangeListener.circuitChanged();
        }
    }
    
    // PINS
    
    public Pin findPinAt(JComponent comp, Point pointInComp) {
        for (Pin pin : getPins(comp)) {
            if (pin.containsInComponent(pointInComp)) {
                return pin;
            }
        }
        return null;
    }
    
    public Pin findPinOnCanvas(Point canvasPoint) {
        for (Component component : getComponents()) {
            for (Pin pin : getPins(component)) {
                if (pin.containsOnCanvas(canvasPoint)) {
                    return pin;
                }
            }
        }
        return null;
    }
    
    // Un Switch tiene un solo pin (la salida); una compuerta tiene los suyos
    private List<Pin> getPins(Component component) {
        List<Pin> pins = new ArrayList<>();
        
        if (component instanceof Switch switchIcon) {
            pins.add(switchIcon.getOutputPin());
        }
        
        else if (component instanceof LogicGate gate) {
            for (Pin pin : gate.getPins()) {
                pins.add(pin);
            }
        }
        
        return pins;
    }
    
    // WIRES
    
    public Wire findWireConnectedToInput(Pin inputPin) {
        for (Wire w : wires) {
            if (w.getTarget().getOwner() == inputPin.getOwner() && w.getTarget().getIndex() == inputPin.getIndex()) {
                return w;
            }
        }
        return null;
    }
    
    public void startWiring(Pin pin) {
        Pin startPin = pin;
        
        // Si el usuario hace clic en un pin de entrada que ya tiene un cable conectado:
        // "Desenchufamos" el cable de esa entrada y lo dejamos en la mano del usuario para reubicarlo.
        if (pin.getType() == Pin.Type.INPUT) {
            Wire existingWire = findWireConnectedToInput(pin);
            
            if (existingWire != null) {
                startPin = existingWire.getSource();
                disconnectWire(existingWire);
            }
        }
        
        pendingWire.start(startPin, pin.getLocationOnCanvas());
        repaint();
    }
    
    public void dragWiring(Point canvasPoint) {
        pendingWire.moveTo(canvasPoint);
        repaint();
    }
    
    public void finishWiring(Point canvasPoint) {
        if (!pendingWire.isActive()) {
            return;
        }
        
        Pin activePin = pendingWire.getPin();
        Pin targetPin = findPinOnCanvas(canvasPoint);
        
        if (targetPin != null && targetPin != activePin && targetPin.getOwner() != activePin.getOwner()) {
            if (activePin.getType() == Pin.Type.OUTPUT && targetPin.getType() == Pin.Type.INPUT) {
                connectWire(activePin, targetPin);
            }
            
            else if (activePin.getType() == Pin.Type.INPUT && targetPin.getType() == Pin.Type.OUTPUT) {
                connectWire(targetPin, activePin);
            }
        }
        
        pendingWire.clear();
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
        if (inputPin.getOwner() instanceof LogicGate gate) {
            gate.setPinInput(inputPin.getIndex(), outputPin.getLogicSource());
        }
        
        evaluateCircuit();
        
        circuits.reorganize();
        notifyCircuitChange();
    }
    
    public void disconnectWire(Wire wire) {
        wires.remove(wire);
        clearTargetInput(wire);
        evaluateCircuit();
        
        circuits.reorganize();
        notifyCircuitChange();
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
    
    // Deja libre la entrada de la compuerta a la que llegaba el cable
    private void clearTargetInput(Wire wire) {
        if (wire.getTarget().getOwner() instanceof LogicGate gate) {
            gate.clearPinInput(wire.getTarget().getIndex());
        }
    }
    
    // CIRCUIT
    
    public void evaluateCircuit() {
        for (int pass = 0; pass < EVALUATION_PASSES; pass++) {
            for (Component component : getComponents()) {
                if (component instanceof LogicGate gate) {
                    gate.getOutputValue();
                }
            }
        }
        
        repaint();
    }
    
    // GATES
    
    // AUN NO <-- ...
    public void removeGate(JComponent gate) {
        for (Wire wire : new ArrayList<>(wires)) {
            if (wire.isConnectedTo(gate)) {
                clearTargetInput(wire);
                wires.remove(wire);
            }
        }
        
        if (gate instanceof LogicGate logicGate) {
            circuits.removeGateByName(logicGate.getEngineGate().getName());
        }
        
        else {
            circuits.reorganize();
        }
        
        remove(gate);
        
        evaluateCircuit();
        notifyCircuitChange();
    }
    
    // Lo llama GateDropHandler; devuelve false si el nombre no es una compuerta conocida
    public boolean addGate(String name, Point location) {
        JComponent icon = GateIconFactory.create(name, ICON_SIZE);
        
        if (icon == null) {
            return false;
        }
        
        icon.setToolTipText(name);
        // Styles.setBorder(icon, Palette.ACCENT);
        
        icon.setBounds(
            Math.max(0, location.x - ICON_SIZE / 2),
            Math.max(0, location.y - ICON_SIZE / 2),
            ICON_SIZE,
            ICON_SIZE
        );
        
        new GateDragHandler(icon);
        
        if (icon instanceof LogicGate logicGate) {
            circuits.addGate(logicGate.getEngineGate());
        }
        
        add(icon);
        revalidate();
        repaint();
        
        notifyCircuitChange();
        
        return true;
    }
    
    // PAINT
    
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
        pendingWire.draw(g2d);
        
        g2d.dispose();
    }
}