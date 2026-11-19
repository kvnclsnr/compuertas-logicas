package com.ui.icons;

import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;

import com.engine.LogicSource;
import com.engine.circuit.CircuitManager;
import com.ui.circuit.pin.Pin;
import com.ui.styles.Palette;

// BASE DE LOS ELEMENTOS DEL CIRCUITO, DIBUJA SU CUERPO ESCALADO Y SABE DONDE ESTAN SUS PINES
public abstract class CircuitElement extends JPanel {
    protected static final int VIEW_SIZE = 28;
    protected static final int CENTER_Y = 14;
    protected static final int OUTPUT_PIN_X = 26;
    
    private static final int INPUT_PIN_X = 2;
    private static final double TERMINAL_RADIUS = 1.5;
    private static final BasicStroke BODY_STROKE = new BasicStroke(1.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final BasicStroke TERMINAL_STROKE = new BasicStroke(0.5f);
    
    private final int iconSize;
    private final ElementType type;
    private boolean placed = false;
    
    // CREA EL ELEMENTO CUADRADO DEL TAMAÑO INDICADO Y DEL TIPO INDICADO
    protected CircuitElement(int size, ElementType type) {
        iconSize = size;
        this.type = type;
        
        setPreferredSize(new Dimension(size, size));
        setMinimumSize(new Dimension(size, size));
        setOpaque(false);
    }
    
    // DEVUELVE EL TIPO DEL ELEMENTO
    public ElementType getType() {
        return type;
    }
    
    // DEVUELVE EL NOMBRE QUE IDENTIFICA AL ELEMENTO
    public abstract String getElementName();
    
    // DEVUELVE EL VALOR ACTUAL DE LA SALIDA
    public abstract boolean getOutputValue();
    
    // DEVUELVE LA FUENTE DEL ENGINE DE DONDE SALE LA SEÑAL
    public abstract LogicSource getOutputSource();
    
    // REGISTRA EL ELEMENTO EN LOS CIRCUITOS CUANDO LO NECESITAN
    public abstract void addToCircuits(CircuitManager circuits);
    
    // QUITA EL ELEMENTO DE LOS CIRCUITOS Y LOS REORGANIZA
    public abstract void removeFromCircuits(CircuitManager circuits);
    
    // DEVUELVE LA POSICION VERTICAL DE CADA PIN DE ENTRADA
    protected abstract int[] getInputPinYPositions();
    
    // DIBUJA EL CUERPO DEL ELEMENTO EN EL LIENZO ESCALADO
    protected abstract void drawBody(Graphics2D g2d);
    
    // CONECTA UNA FUENTE A LA ENTRADA INDICADA, LOS ELEMENTOS SIN ENTRADAS NO LA ADMITEN
    public void setInput(int index, LogicSource source) {
        throw new UnsupportedOperationException("This element has no inputs");
    }
    
    // REACCIONA A UN CLIC, POR DEFECTO NO HACE NADA
    public void click() { }
    
    // AVISA QUE EL ELEMENTO YA ESTA EN EL LIENZO, DESDE AHI MUESTRA SUS PINES Y SU ESTADO
    public void markAsPlaced() {
        placed = true;
    }
    
    // CALCULA LOS PINES DEL ELEMENTO CON SU POSICION ACTUAL
    public List<Pin> getPins() {
        List<Pin> pins = new ArrayList<>();
        int[] inputYPositions = getInputPinYPositions();
        
        for (int index = 0; index < inputYPositions.length; index++) {
            pins.add(createPin(Pin.Type.INPUT, INPUT_PIN_X, inputYPositions[index], index));
        }
        
        if (hasOutputPin()) {
            pins.add(createPin(Pin.Type.OUTPUT, OUTPUT_PIN_X, CENTER_Y, 0));
        }
        
        return pins;
    }
    
    // DICE SI EL ELEMENTO TIENE PIN DE SALIDA, LOS QUE SOLO MUESTRAN UN RESULTADO NO LO TIENEN
    protected boolean hasOutputPin() {
        return true;
    }
    
    // BUSCA EL PIN QUE ESTA SOBRE UN PUNTO DEL ELEMENTO, O DEVUELVE NULL SI NO HAY
    public Pin findPinAt(Point pointInElement) {
        for (Pin pin : getPins()) {
            if (pin.containsInOwner(pointInElement)) {
                return pin;
            }
        }
        
        return null;
    }
    
    // BUSCA EL PIN DEL TIPO E INDICE PEDIDOS, O DEVUELVE NULL SI NO EXISTE
    public Pin getPin(Pin.Type type, int index) {
        for (Pin pin : getPins()) {
            if (pin.getType() == type && pin.getIndex() == index) {
                return pin;
            }
        }
        
        return null;
    }
    
    // DICE SI EL ELEMENTO ESTA EN EL LIENZO Y SU SALIDA VALE 1
    protected boolean isActive() {
        return placed && getOutputValue();
    }
    
    // DIBUJA EL CUERPO Y, SI ESTA EN EL LIENZO, LOS TERMINALES DE LOS PINES
    @Override
    protected final void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        Graphics2D g2d = (Graphics2D) g.create();
        
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_STROKE_CONTROL, RenderingHints.VALUE_STROKE_PURE);
        
        g2d.translate(getOffsetX(), getOffsetY());
        g2d.scale(getScale(), getScale());
        
        g2d.setColor(isActive() ? Palette.ACCENT : Palette.INK);
        g2d.setStroke(BODY_STROKE);
        drawBody(g2d);
        
        if (placed) {
            drawPinTerminals(g2d);
        }
        
        g2d.dispose();
    }
    
    // CALCULA CUANTO SE AMPLIA EL DIBUJO PARA LLENAR EL ELEMENTO
    private double getScale() {
        double renderedSize = Math.min(iconSize, Math.min(getWidth(), getHeight()));
        
        return renderedSize / VIEW_SIZE;
    }
    
    // CALCULA EL ESPACIO HORIZONTAL QUE SOBRA A LA IZQUIERDA PARA CENTRAR EL DIBUJO
    private double getOffsetX() {
        return (getWidth() - VIEW_SIZE * getScale()) / 2;
    }
    
    // CALCULA EL ESPACIO VERTICAL QUE SOBRA ARRIBA PARA CENTRAR EL DIBUJO
    private double getOffsetY() {
        return (getHeight() - VIEW_SIZE * getScale()) / 2;
    }
    
    // CREA UN PIN CONVIRTIENDO SU POSICION DEL DIBUJO A PIXELES DEL ELEMENTO
    private Pin createPin(Pin.Type type, int viewX, int viewY, int index) {
        int x = (int) Math.round(getOffsetX() + viewX * getScale());
        int y = (int) Math.round(getOffsetY() + viewY * getScale());
        
        return new Pin(this, type, x, y, index);
    }
    
    // DIBUJA UN TERMINAL EN CADA PIN DE ENTRADA Y EN EL DE SALIDA SI TIENE
    private void drawPinTerminals(Graphics2D g2d) {
        g2d.setStroke(TERMINAL_STROKE);
        
        for (int y : getInputPinYPositions()) {
            drawTerminal(g2d, INPUT_PIN_X, y);
        }
        
        if (hasOutputPin()) {
            drawTerminal(g2d, OUTPUT_PIN_X, CENTER_Y);
        }
    }
    
    // DIBUJA UN CIRCULO BLANCO CON BORDE EN LA POSICION INDICADA
    private void drawTerminal(Graphics2D g2d, double x, double y) {
        double diameter = TERMINAL_RADIUS * 2;
        Ellipse2D terminal = new Ellipse2D.Double(x - TERMINAL_RADIUS, y - TERMINAL_RADIUS, diameter, diameter);
        
        g2d.setColor(Palette.SURFACE);
        g2d.fill(terminal);
        
        g2d.setColor(Palette.LINE);
        g2d.draw(terminal);
    }
}