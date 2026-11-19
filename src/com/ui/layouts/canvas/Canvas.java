package com.ui.layouts.canvas;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JViewport;
import javax.swing.Scrollable;
import javax.swing.SwingConstants;

import com.engine.circuit.Circuit;
import com.engine.circuit.CircuitManager;
import com.engine.circuit.TruthTablePrinter;
import com.ui.circuit.pin.Pin;
import com.ui.circuit.wire.PendingWire;
import com.ui.circuit.wire.Wire;
import com.ui.circuit.wire.WireList;
import com.ui.events.circuit.CircuitChangeListener;
import com.ui.events.drag.ElementDragHandler;
import com.ui.events.drag.GateDropHandler;
import com.ui.icons.CircuitElement;
import com.ui.icons.ElementFactory;
import com.ui.icons.ElementType;
import com.ui.styles.Palette;

// LIENZO DONDE SE COLOCAN LOS ELEMENTOS Y SE CONECTAN CON CABLES, CRECE SEGUN LO QUE CONTIENE
public class Canvas extends JPanel implements Scrollable {
    private static final int ELEMENT_SIZE = 96;
    private static final int CONTENT_MARGIN = 48;
    private static final int SCROLL_UNIT = 16;
    private static final int OUTPUT_PIN_INDEX = 0;
    
    private static final String CYCLE_ERROR_TITLE = "Conexión inválida";
    private static final String CYCLE_ERROR_MESSAGE = "No se pueden conectar compuertas cíclicas";
    
    private final WireList wires = new WireList();
    private final PendingWire pendingWire = new PendingWire();
    private final CircuitManager circuits = new CircuitManager();
    
    private CircuitChangeListener circuitChangeListener = null;
    private Component deleteZone = null;
    
    // CREA EL LIENZO VACIO QUE ACEPTA ELEMENTOS ARRASTRADOS DESDE LA PALETA
    public Canvas() {
        super(null);
        
        setBackground(Palette.SURFACE);
        setTransferHandler(new GateDropHandler(this));
    }
    
    // GUARDA QUIEN DEBE ENTERARSE CUANDO CAMBIA EL CIRCUITO
    public void setCircuitChangeListener(CircuitChangeListener listener) {
        circuitChangeListener = listener;
    }
    
    // GUARDA EL COMPONENTE SOBRE EL QUE SE SUELTA UN ELEMENTO PARA BORRARLO
    public void setDeleteZone(Component zone) {
        deleteZone = zone;
    }
    
    // DEVUELVE UNA TABLA DE VERDAD POR CADA CIRCUITO SEPARADO DEL LIENZO
    public List<String[][]> getCircuitTruthTables() {
        return circuits.getTruthTables();
    }
    
    // CREA UN ELEMENTO DEL TIPO PEDIDO Y LO COLOCA CENTRADO EN EL PUNTO INDICADO
    public void addElement(ElementType type, Point location) {
        addElementAt(type, location.x - ELEMENT_SIZE / 2, location.y - ELEMENT_SIZE / 2);
    }
    
    // CREA UN ELEMENTO DEL TIPO PEDIDO CON SU ESQUINA SUPERIOR IZQUIERDA EN LA POSICION INDICADA Y LO DEVUELVE
    public CircuitElement addElementAt(ElementType type, int x, int y) {
        CircuitElement element = ElementFactory.create(type, ELEMENT_SIZE);
        
        element.setBounds(Math.max(0, x), Math.max(0, y), ELEMENT_SIZE, ELEMENT_SIZE);
        element.markAsPlaced();
        element.setToolTipText(element.getElementName());
        
        ElementDragHandler dragHandler = new ElementDragHandler(element, this);
        element.addMouseListener(dragHandler);
        element.addMouseMotionListener(dragHandler);
        
        element.addToCircuits(circuits);
        add(element);
        
        revalidate();
        repaint();
        notifyCircuitChange();
        
        return element;
    }
    
    // SUELTA UN ELEMENTO ARRASTRADO, SE BORRA SI QUEDO SOBRE LA ZONA DE BORRADO Y SI NO SE MANTIENE EN EL AREA POSITIVA
    public void dropElement(CircuitElement element, Point screenPoint) {
        if (isOverDeleteZone(screenPoint)) {
            removeElement(element);
        }
        
        else {
            moveToPositiveArea(element);
        }
    }
    
    // EMPIEZA UN CABLE DESDE UN PIN, SI ES UNA ENTRADA CON CABLE LO DESENCHUFA Y LO DEJA EN LA MANO
    public void startWiring(Pin pin) {
        Wire existingWire = wires.findEndingAt(pin);
        Pin startPin = pin;
        
        if (existingWire != null) {
            startPin = existingWire.getSource();
            disconnectWire(existingWire);
        }
        
        pendingWire.start(startPin, pin.getLocationOnCanvas());
        repaint();
    }
    
    // MUEVE EL EXTREMO LIBRE DEL CABLE HASTA EL MOUSE
    public void dragWiring(Point canvasPoint) {
        pendingWire.moveTo(canvasPoint);
        repaint();
    }
    
    // TERMINA EL CABLE, SI SE SOLTO SOBRE UN PIN DE OTRO ELEMENTO SE CONECTA
    public void finishWiring(Point canvasPoint) {
        if (!pendingWire.isActive()) return;
        
        Pin startPin = pendingWire.getStartPin();
        Pin endPin = findPinOnCanvas(canvasPoint);
        
        pendingWire.clear();
        
        if (endPin != null && endPin.getOwner() != startPin.getOwner()) {
            connectPins(startPin, endPin);
        }
        
        repaint();
    }
    
    // QUITA LOS CABLES DE UN PIN, EN UNA ENTRADA ES UNO Y EN UNA SALIDA SON TODOS LOS QUE SALEN
    public void disconnectPin(Pin pin) {
        if (pin.getType() == Pin.Type.INPUT) {
            disconnectInput(pin);
        }
        
        else {
            disconnectOutputs(pin.getOwner());
        }
        
        rebuildCircuits();
    }
    
    // CONECTA LA SALIDA DE UN ELEMENTO CON UNA ENTRADA DE OTRO
    public void connectElements(CircuitElement source, CircuitElement target, int inputIndex) {
        Pin outputPin = source.getPin(Pin.Type.OUTPUT, OUTPUT_PIN_INDEX);
        Pin inputPin = target.getPin(Pin.Type.INPUT, inputIndex);
        
        connectWire(outputPin, inputPin);
    }
    
    // QUITA TODOS LOS ELEMENTOS DEL LIENZO JUNTO CON SUS CABLES
    public void clear() {
        for (CircuitElement element : getElements()) {
            removeElement(element);
        }
    }
    
    // EL TAMAÑO DEL LIENZO ES EL QUE NECESITA EL ELEMENTO MAS LEJANO MAS UN MARGEN
    @Override
    public Dimension getPreferredSize() {
        int width = 0;
        int height = 0;
        
        for (CircuitElement element : getElements()) {
            width = Math.max(width, element.getX() + element.getWidth());
            height = Math.max(height, element.getY() + element.getHeight());
        }
        
        return new Dimension(width + CONTENT_MARGIN, height + CONTENT_MARGIN);
    }
    
    // TAMAÑO VISIBLE POR DEFECTO DENTRO DE LA VISTA
    @Override
    public Dimension getPreferredScrollableViewportSize() {
        return getPreferredSize();
    }
    
    // CUANTO SE MUEVE LA VISTA CON CADA PASO DE LA RUEDA
    @Override
    public int getScrollableUnitIncrement(Rectangle visibleRect, int orientation, int direction) {
        return SCROLL_UNIT;
    }
    
    // CUANTO SE MUEVE LA VISTA EN UN SALTO GRANDE
    @Override
    public int getScrollableBlockIncrement(Rectangle visibleRect, int orientation, int direction) {
        return orientation == SwingConstants.VERTICAL ? visibleRect.height : visibleRect.width;
    }
    
    // SE ESTIRA A LO ANCHO DE LA VISTA CUANDO EL CONTENIDO CABE
    @Override
    public boolean getScrollableTracksViewportWidth() {
        return getParent() instanceof JViewport viewport && viewport.getWidth() > getPreferredSize().width;
    }
    
    // SE ESTIRA A LO ALTO DE LA VISTA CUANDO EL CONTENIDO CABE
    @Override
    public boolean getScrollableTracksViewportHeight() {
        return getParent() instanceof JViewport viewport && viewport.getHeight() > getPreferredSize().height;
    }
    
    // DIBUJA LOS CABLES DEBAJO DE LOS ELEMENTOS Y EL CABLE QUE SIGUE AL MOUSE
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        wires.draw(g2d);
        pendingWire.draw(g2d);
        
        g2d.dispose();
    }
    
    // CONECTA DOS PINES CON UN CABLE SI UNO ES SALIDA Y EL OTRO ENTRADA, SIN IMPORTAR EL ORDEN
    private void connectPins(Pin first, Pin second) {
        if (first.getType() == Pin.Type.OUTPUT && second.getType() == Pin.Type.INPUT) {
            connectWire(first, second);
        }
        
        else if (first.getType() == Pin.Type.INPUT && second.getType() == Pin.Type.OUTPUT) {
            connectWire(second, first);
        }
    }
    
    // CREA UN CABLE DE UNA SALIDA A UNA ENTRADA, REEMPLAZA EL QUE LA ENTRADA YA TUVIERA
    private void connectWire(Pin outputPin, Pin inputPin) {
        if (createsCycle(outputPin.getOwner(), inputPin.getOwner())) {
            JOptionPane.showMessageDialog(this, CYCLE_ERROR_MESSAGE, CYCLE_ERROR_TITLE, JOptionPane.WARNING_MESSAGE);
            return;
        }
        
        Wire existingWire = wires.findEndingAt(inputPin);
        
        if (existingWire != null) {
            removeWire(existingWire);
        }
        
        Wire wire = new Wire(outputPin, inputPin);
        wire.attach();
        wires.add(wire);
        
        rebuildCircuits();
    }
    
    // DICE SI CONECTAR LA SALIDA DE UN ELEMENTO A LA ENTRADA DE OTRO CERRARIA UN CICLO
    private boolean createsCycle(CircuitElement source, CircuitElement target) {
        Set<CircuitElement> visited = new HashSet<>();
        Deque<CircuitElement> pending = new ArrayDeque<>();
        pending.push(target);
        
        while (!pending.isEmpty()) {
            CircuitElement current = pending.pop();
            
            if (current == source) return true;
            if (!visited.add(current)) continue;
            
            for (Wire wire : wires.findStartingAt(current)) {
                pending.push(wire.getTarget().getOwner());
            }
        }
        
        return false;
    }
    
    // QUITA UN CABLE Y ACTUALIZA LOS CIRCUITOS
    private void disconnectWire(Wire wire) {
        removeWire(wire);
        rebuildCircuits();
    }
    
    // QUITA EL CABLE QUE LLEGA A UNA ENTRADA, SI TIENE
    private void disconnectInput(Pin inputPin) {
        Wire wire = wires.findEndingAt(inputPin);
        
        if (wire != null) {
            removeWire(wire);
        }
    }
    
    // QUITA TODOS LOS CABLES QUE SALEN DE UN ELEMENTO
    private void disconnectOutputs(CircuitElement element) {
        for (Wire wire : wires.findStartingAt(element)) {
            removeWire(wire);
        }
    }
    
    // DESCONECTA LA SEÑAL DE UN CABLE Y LO SACA DE LA LISTA, SIN AVISAR A NADIE
    private void removeWire(Wire wire) {
        wire.detach();
        wires.remove(wire);
    }
    
    // QUITA UN ELEMENTO DEL LIENZO JUNTO CON TODOS SUS CABLES
    private void removeElement(CircuitElement element) {
        for (Wire wire : wires.findConnectedTo(element)) {
            removeWire(wire);
        }
        
        element.removeFromCircuits(circuits);
        remove(element);
        
        revalidate();
        repaint();
        notifyCircuitChange();
    }
    
    // SI EL ELEMENTO QUEDO EN POSICIONES NEGATIVAS LO TRAE AL BORDE, LO DEMAS HACE CRECER EL LIENZO
    private void moveToPositiveArea(CircuitElement element) {
        element.setLocation(Math.max(0, element.getX()), Math.max(0, element.getY()));
        
        revalidate();
        repaint();
    }
    
    // DICE SI UN PUNTO DE LA PANTALLA ESTA SOBRE LA ZONA DE BORRADO
    private boolean isOverDeleteZone(Point screenPoint) {
        if (deleteZone == null || !deleteZone.isShowing()) {
            return false;
        }
        
        Rectangle zoneBounds = new Rectangle(deleteZone.getLocationOnScreen(), deleteZone.getSize());
        
        return zoneBounds.contains(screenPoint);
    }
    
    // BUSCA EL PIN QUE ESTA SOBRE UN PUNTO DEL LIENZO, O DEVUELVE NULL SI NO HAY
    private Pin findPinOnCanvas(Point canvasPoint) {
        for (CircuitElement element : getElements()) {
            for (Pin pin : element.getPins()) {
                if (pin.containsOnCanvas(canvasPoint)) {
                    return pin;
                }
            }
        }
        
        return null;
    }
    
    // DEVUELVE UNA COPIA CON LOS CABLES DEL LIENZO
    public List<Wire> getWires() {
        return wires.getAll();
    }
    
    // DEVUELVE LOS ELEMENTOS QUE HAY EN EL LIENZO
    public List<CircuitElement> getElements() {
        List<CircuitElement> elements = new ArrayList<>();
        
        for (Component component : getComponents()) {
            if (component instanceof CircuitElement element) {
                elements.add(element);
            }
        }
        
        return elements;
    }
    
    // REORGANIZA LOS CIRCUITOS PORQUE LAS CONEXIONES CAMBIARON Y AVISA AL LISTENER
    private void rebuildCircuits() {
        circuits.reorganize();
        notifyCircuitChange();
        repaint();
    }
    
    // AVISA AL LISTENER, SI HAY UNO, QUE EL CIRCUITO CAMBIO
    private void notifyCircuitChange() {
        if (circuitChangeListener != null) {
            circuitChangeListener.circuitChanged();
        }
    }
    
    // DEVUELVE EL TEXTO DE LA TABLA DE VERDAD DE TODOS LOS CIRCUITOS, IGUAL AL QUE SE IMPRIME EN LA CONSOLA
    public String getTruthTableText() {
        StringBuilder text = new StringBuilder();
        
        for (Circuit circuit : circuits.getCircuits()) {
            text.append(TruthTablePrinter.format(circuit));
        }
        
        return text.toString();
    }
}