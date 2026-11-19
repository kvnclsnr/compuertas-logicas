package com.ui.icons;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Path2D;
import java.awt.geom.RoundRectangle2D;

import com.engine.gates.BulbGate;
import com.ui.styles.Palette;

// DIBUJO DEL FOCO, MUESTRA SI SU ENTRADA ESTA ENCENDIDA O APAGADA
public class Bulb extends LogicGate {
    private static final int[] SINGLE_INPUT_PIN_Y = { CENTER_Y };
    
    private static final BasicStroke WIRE_STROKE = new BasicStroke(1.1f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final BasicStroke BASE_STROKE = new BasicStroke(0.75f);
    private static final BasicStroke ENVELOPE_STROKE = new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final BasicStroke FILAMENT_STROKE = new BasicStroke(0.75f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    private static final BasicStroke RAY_STROKE = new BasicStroke(1.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);
    
    // COLORES DEL FOCO SEGUN SI ESTA ENCENDIDO O APAGADO
    private static final Color ACTIVE_FILL = new Color(255, 238, 88);
    private static final Color ACTIVE_BORDER = new Color(245, 160, 0);
    private static final Color ACTIVE_FILAMENT = new Color(230, 81, 0);
    private static final Color ACTIVE_RAY = new Color(255, 214, 0);
    private static final Color INACTIVE_FILL = new Color(255, 255, 255, 0);
    
    private static int counter = 0;
    
    // CREA EL FOCO CON SU NOMBRE NUMERADO
    public Bulb(int size) {
        super(size, ElementType.OUTPUT);
        this.engineGate = new BulbGate("OUT-" + (counter++));
    }
    
    // CREA EL FOCO CON EL TAMAÑO POR DEFECTO
    public Bulb() {
        this(DEFAULT_SIZE);
    }
    
    // EL FOCO NO TIENE PIN DE SALIDA, SOLO MUESTRA EL RESULTADO
    @Override
    protected boolean hasOutputPin() {
        return false;
    }
    
    // EL FOCO TIENE UNA SOLA ENTRADA, EN EL CENTRO
    @Override
    protected int[] getInputPinYPositions() {
        return SINGLE_INPUT_PIN_Y;
    }
    
    // DIBUJA EL CABLE DE ENTRADA, LA BASE, EL VIDRIO, EL FILAMENTO Y LOS RAYOS SI ESTA ENCENDIDO
    @Override
    protected void drawGate(Graphics2D g2d) {
        boolean active = isActive();
        
        drawInputWire(g2d, active);
        drawBase(g2d, active);
        drawEnvelope(g2d, active);
        drawFilament(g2d, active);
        
        if (active) {
            drawLightRays(g2d);
        }
    }
    
    // DIBUJA EL CABLE QUE UNE EL PIN DE ENTRADA CON LA BASE DEL FOCO
    private void drawInputWire(Graphics2D g2d, boolean active) {
        g2d.setColor(active ? Palette.ACCENT : Palette.INK);
        g2d.setStroke(WIRE_STROKE);
        g2d.drawLine(2, 14, 6, 14);
    }
    
    // DIBUJA LA ROSCA METALICA DONDE SE CONECTA EL FOCO
    private void drawBase(Graphics2D g2d, boolean active) {
        RoundRectangle2D base = new RoundRectangle2D.Double(6.0, 11.5, 3.5, 5.0, 1.5, 1.5);
        
        g2d.setColor(Palette.INK);
        g2d.fill(base);
        
        g2d.setColor(Palette.INK);
        g2d.setStroke(BASE_STROKE);
        g2d.draw(base);
    }
    
    // DIBUJA EL VIDRIO DEL FOCO, RELLENO Y CON SU BORDE
    private void drawEnvelope(Graphics2D g2d, boolean active) {
        Path2D.Double envelope = createEnvelope();
        
        g2d.setColor(active ? ACTIVE_FILL : INACTIVE_FILL);
        g2d.fill(envelope);
        
        g2d.setColor(active ? ACTIVE_BORDER : Palette.MUTED);
        g2d.setStroke(ENVELOPE_STROKE);
        g2d.draw(envelope);
    }
    
    // CREA LA CURVA DEL VIDRIO DEL FOCO
    private Path2D.Double createEnvelope() {
        Path2D.Double envelope = new Path2D.Double();
        
        envelope.moveTo(9.5, 11.5);
        envelope.curveTo(11.0, 11.5, 12.5, 8.0, 16.5, 8.0);
        envelope.curveTo(20.5, 8.0, 23.5, 10.5, 23.5, 14.0);
        envelope.curveTo(23.5, 17.5, 20.5, 20.0, 16.5, 20.0);
        envelope.curveTo(12.5, 20.0, 11.0, 16.5, 9.5, 16.5);
        envelope.closePath();
        
        return envelope;
    }
    
    // DIBUJA EL FILAMENTO EN ZIGZAG DENTRO DEL VIDRIO
    private void drawFilament(Graphics2D g2d, boolean active) {
        Path2D.Double filament = new Path2D.Double();
        
        filament.moveTo(9.5, 13.0);
        filament.lineTo(13.0, 13.0);
        filament.lineTo(14.5, 11.5);
        filament.lineTo(16.0, 14.0);
        filament.lineTo(14.5, 16.5);
        filament.lineTo(13.0, 15.0);
        filament.lineTo(9.5, 15.0);
        
        g2d.setColor(active ? ACTIVE_FILAMENT : Palette.LINE);
        g2d.setStroke(FILAMENT_STROKE);
        g2d.draw(filament);
    }
    
    // DIBUJA LOS RAYOS DE LUZ ALREDEDOR DEL FOCO CUANDO ESTA ENCENDIDO, TODOS CON EL MISMO LARGO Y LA MISMA DISTANCIA AL VIDRIO
    private void drawLightRays(Graphics2D g2d) {
        g2d.setColor(ACTIVE_RAY);
        g2d.setStroke(RAY_STROKE);
        
        g2d.drawLine(16, 6, 16, 3);     // Arriba
        g2d.drawLine(16, 22, 16, 25);   // Abajo
        g2d.drawLine(25, 14, 28, 14);   // Derecha
        g2d.drawLine(22, 8, 24, 6);     // Diagonal superior derecha
        g2d.drawLine(22, 20, 24, 22);   // Diagonal inferior derecha
    }
}