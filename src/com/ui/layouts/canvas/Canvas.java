package com.ui.layouts.canvas;

import com.engine.circuit.CircuitManager;
import com.ui.events.circuit.CircuitChangeListener;
import com.ui.circuit.wire.PendingWire;
import com.ui.circuit.pin.Pin;
import com.ui.circuit.wire.Wire;
import com.ui.events.drag.GateDragHandler;
import com.ui.events.drag.GateDropHandler;
import com.ui.history.AddComponentCommand;
import com.ui.history.MoveComponentCommand;
import com.ui.history.RemoveComponentCommand;
import com.ui.history.UndoRedoManager;
import com.ui.history.WireChangeCommand;
import com.ui.icons.Bulb;
import com.ui.icons.GateIconFactory;
import com.ui.icons.LogicGate;
import com.ui.icons.Switch;
import com.ui.styles.Palette;
import com.ui.styles.Styles;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.KeyboardFocusManager;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;

public class Canvas extends JPanel {
    private static final int ICON_SIZE = 96;
    private static final int EVALUATION_PASSES = 5;
    
    private static final double MIN_ZOOM = 0.4;
    private static final double MAX_ZOOM = 2.5;
    private static final double ZOOM_STEP = 1.25;
    private double zoomFactor = 1.0;
    
    private boolean spacePressed = false;
    private Point panStartPoint = null;
    
    private final List<Wire> wires = new ArrayList<>();
    private final PendingWire pendingWire = new PendingWire();
    private Wire unpluggedWire = null;
    
    private final CircuitManager circuits = new CircuitManager();
    private final UndoRedoManager undoRedoManager = new UndoRedoManager();
    private CircuitChangeListener circuitChangeListener = null;
    private Component deleteZone = null;
    
    public Canvas() {
        super(null);
        Styles.setBackground(this, Palette.SURFACE);
        Styles.setBorder(this, Palette.LINE);
        setTransferHandler(new GateDropHandler(this));
        setupKeyBindings();
        setupPanAndZoom();
    }
    
    private void setupPanAndZoom() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(event -> {
            if (event.getKeyCode() == KeyEvent.VK_SPACE) {
                if (event.getID() == KeyEvent.KEY_PRESSED) {
                    setSpacePressed(true);
                } else if (event.getID() == KeyEvent.KEY_RELEASED) {
                    setSpacePressed(false);
                }
            }
            return false;
        });
        
        MouseAdapter panAdapter = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (spacePressed && SwingUtilities.isRightMouseButton(e)) {
                    panStartPoint = e.getLocationOnScreen();
                    setCursor(Cursor.getPredefinedCursor(Cursor.MOVE_CURSOR));
                }
            }
            
            @Override
            public void mouseDragged(MouseEvent e) {
                if (spacePressed && SwingUtilities.isRightMouseButton(e) && panStartPoint != null) {
                    Point current = e.getLocationOnScreen();
                    int dx = current.x - panStartPoint.x;
                    int dy = current.y - panStartPoint.y;
                    panStartPoint = current;
                    pan(dx, dy);
                }
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                if (SwingUtilities.isRightMouseButton(e)) {
                    panStartPoint = null;
                    setCursor(spacePressed ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor());
                }
            }
        };
        addMouseListener(panAdapter);
        addMouseMotionListener(panAdapter);
    }
    
    private void setupKeyBindings() {
        int shortcutKeyMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMaskEx();
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcutKeyMask), "undoAction");
        getActionMap().put("undoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                undo();
            }
        });
        
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Y, shortcutKeyMask), "redoAction");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_Z, shortcutKeyMask | KeyEvent.SHIFT_DOWN_MASK), "redoAction");
        getActionMap().put("redoAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                redo();
            }
        });
        
        // Zoom in: Ctrl + PLUS, Ctrl + EQUALS, Ctrl + ADD
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_PLUS, shortcutKeyMask), "zoomInAction");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_EQUALS, shortcutKeyMask), "zoomInAction");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_ADD, shortcutKeyMask), "zoomInAction");
        getActionMap().put("zoomInAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomIn();
            }
        });
        
        // Zoom out: Ctrl + MINUS, Ctrl + SUBTRACT
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_MINUS, shortcutKeyMask), "zoomOutAction");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_SUBTRACT, shortcutKeyMask), "zoomOutAction");
        getActionMap().put("zoomOutAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                zoomOut();
            }
        });
        
        // Reset zoom: Ctrl + 0, Ctrl + NUMPAD0
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_0, shortcutKeyMask), "resetZoomAction");
        getInputMap(WHEN_IN_FOCUSED_WINDOW).put(
            KeyStroke.getKeyStroke(KeyEvent.VK_NUMPAD0, shortcutKeyMask), "resetZoomAction");
        getActionMap().put("resetZoomAction", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetZoom();
            }
        });
    }
    
    public UndoRedoManager getUndoRedoManager() {
        return undoRedoManager;
    }
    
    public void undo() {
        undoRedoManager.undo();
    }
    
    public void redo() {
        undoRedoManager.redo();
    }
    
    public boolean canUndo() {
        return undoRedoManager.canUndo();
    }
    
    public boolean canRedo() {
        return undoRedoManager.canRedo();
    }
    
    public boolean hasUnsavedChanges() {
        return undoRedoManager.hasUnsavedChanges();
    }
    
    public void markSaved() {
        undoRedoManager.markSaved();
    }
    
    // NAVEGACIÓN Y ZOOM
    
    public boolean isSpacePressed() {
        return spacePressed;
    }
    
    public void setSpacePressed(boolean pressed) {
        if (this.spacePressed != pressed) {
            this.spacePressed = pressed;
            Cursor cursor = pressed ? Cursor.getPredefinedCursor(Cursor.HAND_CURSOR) : Cursor.getDefaultCursor();
            setCursor(cursor);
            for (Component comp : getComponents()) {
                comp.setCursor(cursor);
            }
        }
    }
    
    public void pan(int dx, int dy) {
        if (dx == 0 && dy == 0) {
            return;
        }
        for (Component comp : getComponents()) {
            comp.setLocation(comp.getX() + dx, comp.getY() + dy);
        }
        repaint();
    }
    
    public double getZoomFactor() {
        return zoomFactor;
    }
    
    public void setZoomFactor(double factor) {
        applyZoom(factor);
    }
    
    public void zoomIn() {
        applyZoom(zoomFactor * ZOOM_STEP);
    }
    
    public void zoomOut() {
        applyZoom(zoomFactor / ZOOM_STEP);
    }
    
    public void resetZoom() {
        applyZoom(1.0);
    }
    
    private void applyZoom(double targetZoom) {
        targetZoom = Math.max(MIN_ZOOM, Math.min(MAX_ZOOM, targetZoom));
        if (Math.abs(targetZoom - zoomFactor) < 0.001) {
            return;
        }
        
        double ratio = targetZoom / zoomFactor;
        zoomFactor = targetZoom;
        
        int centerX = getWidth() > 0 ? getWidth() / 2 : 0;
        int centerY = getHeight() > 0 ? getHeight() / 2 : 0;
        int newSize = (int) Math.round(ICON_SIZE * zoomFactor);
        
        for (Component comp : getComponents()) {
            int oldX = comp.getX();
            int oldY = comp.getY();
            int newX = centerX + (int) Math.round((oldX - centerX) * ratio);
            int newY = centerY + (int) Math.round((oldY - centerY) * ratio);
            comp.setBounds(newX, newY, newSize, newSize);
        }
        
        revalidate();
        repaint();
    }
    
    // PERSISTENCIA (CLASES 14 Y 15 POO)
    public void saveToFile(java.nio.file.Path path) throws java.io.IOException {
        new com.engine.persistence.RepositorioTexto().guardarCanvas(this, path);
        markSaved();
    }
    
    public void loadFromFile(java.nio.file.Path path) throws java.io.IOException, com.engine.persistence.FormatoCircuitoException {
        new com.engine.persistence.RepositorioTexto().cargarEnCanvas(this, path);
        undoRedoManager.reset();
        markSaved();
    }
    
    public void clearCanvas() {
        wires.clear();
        pendingWire.clear();
        unpluggedWire = null;
        removeAll();
        circuits.reorganize();
        evaluateCircuit();
        notifyCircuitChange();
        undoRedoManager.reset();
        resetZoom();
        setSpacePressed(false);
        repaint();
    }
    
    public List<Wire> getWires() {
        return Collections.unmodifiableList(wires);
    }
    
    public CircuitManager getCircuitManager() {
        return circuits;
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
    public void notifyCircuitChange() {
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
    
    public void reorganizeAndNotify() {
        circuits.reorganize();
        notifyCircuitChange();
    }
    
    public void restoreComponentDirect(JComponent icon) {
        add(icon);
        if (icon instanceof LogicGate logicGate && !(icon instanceof Bulb)) {
            circuits.addGate(logicGate.getEngineGate());
        } else {
            circuits.reorganize();
        }
        revalidate();
        evaluateCircuit();
        notifyCircuitChange();
        repaint();
    }
    
    public void removeComponentDirect(JComponent gate) {
        for (Wire wire : new ArrayList<>(wires)) {
            if (wire.isConnectedTo(gate)) {
                clearTargetInput(wire);
                wires.remove(wire);
            }
        }
        
        if (gate instanceof LogicGate logicGate && !(gate instanceof Bulb)) {
            circuits.removeGate(logicGate.getEngineGate());
        } else {
            circuits.reorganize();
        }
        
        remove(gate);
        evaluateCircuit();
        notifyCircuitChange();
        revalidate();
        repaint();
    }
    
    public boolean wouldCreateCycle(Pin outputPin, Pin inputPin) {
        if (outputPin == null || inputPin == null) {
            return false;
        }
        
        JComponent sourceComp = outputPin.getOwner();
        JComponent targetComp = inputPin.getOwner();
        
        if (sourceComp == targetComp) {
            return true;
        }
        
        // Find existing wire on inputPin that would be replaced
        Wire wireToReplace = findWireConnectedToInput(inputPin);
        
        Set<JComponent> visited = new HashSet<>();
        Queue<JComponent> queue = new ArrayDeque<>();
        
        queue.add(targetComp);
        visited.add(targetComp);
        
        while (!queue.isEmpty()) {
            JComponent current = queue.poll();
            if (current == sourceComp) {
                return true;
            }
            
            for (Wire w : wires) {
                if (w == wireToReplace) {
                    continue;
                }
                if (w.getSource().getOwner() == current) {
                    JComponent next = w.getTarget().getOwner();
                    if (next == sourceComp) {
                        return true;
                    }
                    if (visited.add(next)) {
                        queue.add(next);
                    }
                }
            }
        }
        
        return false;
    }
    
    public void addWireDirect(Wire wire) {
        if (wire == null) {
            return;
        }
        if (wouldCreateCycle(wire.getSource(), wire.getTarget())) {
            return;
        }
        Wire existingWire = findWireConnectedToInput(wire.getTarget());
        if (existingWire != null) {
            disconnectWireDirect(existingWire);
        }
        
        wires.add(wire);
        
        if (wire.getTarget().getOwner() instanceof LogicGate gate) {
            gate.setPinInput(wire.getTarget().getIndex(), wire.getSource().getLogicSource());
        }
    }
    
    public void connectWireDirect(Pin outputPin, Pin inputPin) {
        if (wouldCreateCycle(outputPin, inputPin)) {
            return;
        }
        addWireDirect(new Wire(outputPin, inputPin));
    }
    
    public void disconnectWireDirect(Wire wire) {
        wires.remove(wire);
        wires.removeIf(w -> w.getTarget() == wire.getTarget() && w.getSource() == wire.getSource());
        clearTargetInput(wire);
    }
    
    public void recordMove(JComponent component, Point oldLocation, Point newLocation) {
        if (!undoRedoManager.isExecuting() && oldLocation != null && newLocation != null && !oldLocation.equals(newLocation)) {
            undoRedoManager.push(new MoveComponentCommand(this, component, oldLocation, newLocation));
        }
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
        unpluggedWire = null;
        
        // Si el usuario hace clic en un pin de entrada que ya tiene un cable conectado:
        // "Desenchufamos" el cable de esa entrada y lo dejamos en la mano del usuario para reubicarlo.
        if (pin.getType() == Pin.Type.INPUT) {
            Wire existingWire = findWireConnectedToInput(pin);
            
            if (existingWire != null) {
                startPin = existingWire.getSource();
                disconnectWireDirect(existingWire);
                evaluateCircuit();
                circuits.reorganize();
                notifyCircuitChange();
                unpluggedWire = existingWire;
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
        
        boolean connected = false;
        if (targetPin != null && targetPin != activePin && targetPin.getOwner() != activePin.getOwner()) {
            Pin outPin = null;
            Pin inPin = null;
            if (activePin.getType() == Pin.Type.OUTPUT && targetPin.getType() == Pin.Type.INPUT) {
                outPin = activePin;
                inPin = targetPin;
            } else if (activePin.getType() == Pin.Type.INPUT && targetPin.getType() == Pin.Type.OUTPUT) {
                outPin = targetPin;
                inPin = activePin;
            }
            
            if (outPin != null && inPin != null) {
                if (wouldCreateCycle(outPin, inPin)) {
                    if (unpluggedWire != null) {
                        addWireDirect(unpluggedWire);
                        evaluateCircuit();
                        circuits.reorganize();
                        notifyCircuitChange();
                        unpluggedWire = null;
                    }
                    pendingWire.clear();
                    repaint();
                    
                    JOptionPane.showMessageDialog(
                        this,
                        "No se permiten circuitos con retroalimentación (bucles o ciclos).",
                        "Conexión no permitida",
                        JOptionPane.WARNING_MESSAGE
                    );
                    return;
                }
                
                connectWiringResult(outPin, inPin, unpluggedWire);
                connected = true;
            }
        }
        
        if (!connected && unpluggedWire != null) {
            if (!undoRedoManager.isExecuting()) {
                undoRedoManager.push(new WireChangeCommand(this, Collections.emptyList(), List.of(unpluggedWire)));
            }
        }
        
        unpluggedWire = null;
        pendingWire.clear();
        repaint();
    }
    
    private void connectWiringResult(Pin outputPin, Pin inputPin, Wire priorUnplugged) {
        Wire existingWire = findWireConnectedToInput(inputPin);
        if (existingWire != null) {
            disconnectWireDirect(existingWire);
        }
        
        Wire wire = new Wire(outputPin, inputPin);
        wires.add(wire);
        
        if (inputPin.getOwner() instanceof LogicGate gate) {
            gate.setPinInput(inputPin.getIndex(), outputPin.getLogicSource());
        }
        
        evaluateCircuit();
        circuits.reorganize();
        notifyCircuitChange();
        
        if (!undoRedoManager.isExecuting()) {
            List<Wire> removed = new ArrayList<>();
            if (priorUnplugged != null) {
                removed.add(priorUnplugged);
            }
            if (existingWire != null && existingWire != priorUnplugged) {
                removed.add(existingWire);
            }
            undoRedoManager.push(new WireChangeCommand(this, List.of(wire), removed));
        }
    }
    
    public void connectWire(Pin outputPin, Pin inputPin) {
        if (wouldCreateCycle(outputPin, inputPin)) {
            return;
        }
        connectWiringResult(outputPin, inputPin, null);
    }
    
    public void disconnectWire(Wire wire) {
        disconnectWireDirect(wire);
        evaluateCircuit();
        circuits.reorganize();
        notifyCircuitChange();
        
        if (!undoRedoManager.isExecuting()) {
            undoRedoManager.push(new WireChangeCommand(this, Collections.emptyList(), List.of(wire)));
        }
    }
    
    public void disconnectPin(Pin pin) {
        List<Wire> toRemove = new ArrayList<>();
        
        if (pin.getType() == Pin.Type.INPUT) {
            Wire wire = findWireConnectedToInput(pin);
            if (wire != null) {
                toRemove.add(wire);
            }
        } else {
            for (Wire w : wires) {
                if (w.getSource().getOwner() == pin.getOwner()) {
                    toRemove.add(w);
                }
            }
        }
        
        if (!toRemove.isEmpty()) {
            for (Wire w : toRemove) {
                disconnectWireDirect(w);
            }
            evaluateCircuit();
            circuits.reorganize();
            notifyCircuitChange();
            
            if (!undoRedoManager.isExecuting()) {
                undoRedoManager.push(new WireChangeCommand(this, Collections.emptyList(), toRemove));
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
        
        for (Component component : getComponents()) {
            if (component instanceof Bulb bulb) {
                bulb.updateState();
            }
        }
        
        repaint();
    }
    
    public void evaluateCircuits() {
        evaluateCircuit();
    }
    
    // GATES
    
    public void removeGate(JComponent gate) {
        List<Wire> connected = new ArrayList<>();
        for (Wire wire : wires) {
            if (wire.isConnectedTo(gate)) {
                connected.add(wire);
            }
        }
        
        removeComponentDirect(gate);
        
        if (!undoRedoManager.isExecuting()) {
            undoRedoManager.push(new RemoveComponentCommand(this, gate, connected));
        }
    }
    
    // GUARDA EL COMPONENTE SOBRE EL QUE SE SUELTA UNA COMPUERTA PARA BORRARLA
    public void setDeleteZone(Component zone) {
        deleteZone = zone;
    }
    
    // DICE SI UN PUNTO DE LA PANTALLA ESTÁ SOBRE LA ZONA DE BORRADO
    public boolean isOverDeleteZone(Point screenPoint) {
        if (deleteZone == null || !deleteZone.isShowing()) {
            return false;
        }
        
        Rectangle zoneBounds = new Rectangle(deleteZone.getLocationOnScreen(), deleteZone.getSize());
        
        return zoneBounds.contains(screenPoint);
    }
    
    // SI LA COMPUERTA QUEDÓ FUERA DEL LIENZO, LA VUELVE A METER PARA QUE NO SE PIERDA
    public void keepInside(JComponent gate) {
        int maxX = Math.max(0, getWidth() - gate.getWidth());
        int maxY = Math.max(0, getHeight() - gate.getHeight());
        
        int x = Math.min(Math.max(0, gate.getX()), maxX);
        int y = Math.min(Math.max(0, gate.getY()), maxY);
        
        gate.setLocation(x, y);
        repaint();
    }
    
    // Lo llama GateDropHandler; devuelve false si el nombre no es una compuerta conocida
    public boolean addGate(String name, Point location) {
        JComponent icon = GateIconFactory.create(name, ICON_SIZE);
        
        if (icon == null) {
            return false;
        }
        
        // Styles.setBorder(icon, Palette.ACCENT);
        
        int currentSize = (int) Math.round(ICON_SIZE * zoomFactor);
        icon.setBounds(
            Math.max(0, location.x - currentSize / 2),
            Math.max(0, location.y - currentSize / 2),
            currentSize,
            currentSize
        );
        
        new GateDragHandler(icon);
        
        if (icon instanceof LogicGate logicGate) {
            icon.setToolTipText(logicGate.getEngineGate().getName());
            if (!(icon instanceof Bulb)) {
                circuits.addGate(logicGate.getEngineGate());
            }
        }
        
        else if (icon instanceof Switch switchIcon) {
            icon.setToolTipText(switchIcon.getLogicInput().getName());
        }
        
        add(icon);
        revalidate();
        repaint();
        
        notifyCircuitChange();
        
        if (!undoRedoManager.isExecuting()) {
            undoRedoManager.push(new AddComponentCommand(this, icon));
        }
        
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